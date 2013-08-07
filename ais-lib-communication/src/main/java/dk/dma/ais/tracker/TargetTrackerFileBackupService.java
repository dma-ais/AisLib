/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.ais.tracker;

import static java.util.Objects.requireNonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AbstractScheduledService;

import dk.dma.ais.packet.AisPacketSource;
import dk.dma.ais.tracker.TargetTracker.MmsiTarget;
import dk.dma.commons.util.io.IoUtil;

/**
 * Takes care of backing up and restoring in case of a crash.
 * 
 * @author Kasper Nielsen
 */
public class TargetTrackerFileBackupService extends AbstractScheduledService {

    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(TargetTrackerFileBackupService.class);

    /** The folder to backup files to. */
    private final Path backupFolder;

    BackupFile f;

    /** A random string appended to each backup file. To distinguish them from backup files created by former runs. */
    private final String prefix = Integer.toString(ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE), 36)
            .toUpperCase();

    /** The tracker that we are make backups and restoring from. */
    private final TargetTracker tracker;

    /**
     * Creates a new backup service.
     * 
     * @param tracker
     *            the target tracker that we are make backups and restoring from
     * @param backupFolder
     *            the folder to backup and restore from
     */
    public TargetTrackerFileBackupService(TargetTracker tracker, Path backupFolder) {
        this.tracker = requireNonNull(tracker);
        this.backupFolder = requireNonNull(backupFolder);
    }

    void restoreBackupFiles() throws IOException, ClassNotFoundException {
        List<Path> paths = new ArrayList<>();
        for (Path path : Files.newDirectoryStream(backupFolder)) {
            if (path.getFileName().toString().endsWith("-00")) {
                paths.add(path);
            }
        }
        Path latestFull = IoUtil.findLatestModified(paths);
        if (latestFull != null) {
            LOG.info("Trying to restore backup files " + latestFull);
            String format = latestFull.getFileName().toString();
            format = format.substring(0, format.length() - 3);// strip -00

            ArrayList<Path> files = new ArrayList<>();
            for (Path path : Files.newDirectoryStream(backupFolder)) {
                if (path.getFileName().toString().startsWith(format)) {
                    files.add(path);
                }
            }
            Collections.sort(files); // make sure they are read in the right order

            for (Path path : files) {
                LOG.info("Restoring backup file " + path);
                try (InputStream fos = Files.newInputStream(path);
                        BufferedInputStream bos = new BufferedInputStream(fos);
                        GZIPInputStream gos = new GZIPInputStream(bos);
                        ObjectInputStream oos = new ObjectInputStream(gos)) {
                    AisPacketSource sb = (AisPacketSource) oos.readObject();
                    TargetInfo ti = (TargetInfo) oos.readObject();
                    while (sb != null && ti != null) {
                        tracker.update(sb, ti);
                        sb = (AisPacketSource) oos.readObject();
                        ti = (TargetInfo) oos.readObject();
                    }
                }
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void runOneIteration() throws Exception {
        // If this is the first run, we check if there are any files to restore
        if (f == null) {
            try {
                restoreBackupFiles();
            } catch (Exception e) {
                LOG.error("Cannot restore from backup", e);
            }
            f = new BackupFile();
        }
        Path p = f.toPath(backupFolder, prefix);
        try {
            // clean if we are making a full backup
            if (f.isFull()) {
                tracker.clean();
            }
            writeBackupFile(p, f.isFull());
            f = f.next();
        } catch (Exception e) {
            LOG.error("Failed to write backup to " + p, e);
            f = f.nextFull();
        }
    }

    /** {@inheritDoc} */
    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(0, 1, TimeUnit.SECONDS);
    }

    private void writeBackupFile(Path p, boolean isFull) throws IOException {
        // We write to a temporary file, to make sure we only have complete valid backup files in the folder
        Path temporaryFile = Files.createTempFile(p.getFileName().toString(), ".tmp");

        // Serialize all sourceBundle->TargetInfo mappings
        try (OutputStream fos = Files.newOutputStream(temporaryFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                GZIPOutputStream gos = new GZIPOutputStream(bos);
                ObjectOutputStream oos = new ObjectOutputStream(gos)) {
            for (MmsiTarget t : tracker.targets.values()) {
                for (Entry<AisPacketSource, TargetInfo> e : t.entrySet()) {
                    // Serialize it, if it is a full backup or if the backup flag has not yet been set
                    if (isFull || !e.getValue().isBackedUp) {
                        oos.writeObject(e.getKey());
                        oos.writeObject(e.getValue());
                        e.getValue().isBackedUp = true; // set the backup flag
                    }
                }
            }
            // Write footer, 2 nulls mean end of stream
            oos.writeObject(null);
            oos.writeObject(null);
        }

        // move the temporary file to the correct spot
        Files.move(temporaryFile, p);

        // If this backup is a full backup, delete old backup files
        if (isFull) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(backupFolder)) {
                for (Path path : stream) {
                    if (!path.equals(p)) { // delete all files but the full backup we just wrote
                        Files.delete(path);
                    }
                }
            }
        }
    }

    static class BackupFile {
        final int major;
        final int minor;

        BackupFile() {
            this(1, 0);
        }

        private BackupFile(int major, int minor) {
            this.major = major;
            this.minor = minor;
        }

        boolean isFull() {
            return minor == 0;
        }

        BackupFile next() {
            return minor == 99 ? nextFull() : new BackupFile(major, minor + 1);
        }

        BackupFile nextFull() {
            return new BackupFile((major + 1) % 100000, 0);
        }

        Path toPath(Path directory, String prefix) {
            return directory.resolve("aisviewer_backup-" + prefix + "-" + new DecimalFormat("00000").format(major)
                    + "-" + new DecimalFormat("00").format(minor));
        }
    }
}
