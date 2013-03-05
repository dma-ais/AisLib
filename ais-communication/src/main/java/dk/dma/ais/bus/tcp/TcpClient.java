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
package dk.dma.ais.bus.tcp;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jcip.annotations.ThreadSafe;

/**
 * Base class for all TCP clients
 */
@ThreadSafe
public abstract class TcpClient extends Thread {
    
    private static final Logger LOG = LoggerFactory.getLogger(TcpClient.class);
    
    protected final TcpClientConf conf;
    protected final Socket socket;        
    private final IClientStoppedListener stopListener;
    private final BlockingQueue<String> buffer;    
    
    public TcpClient(IClientStoppedListener stopListener, Socket socket, TcpClientConf conf) {        
        this.stopListener = stopListener;
        this.socket = socket;
        this.conf = conf;
        this.buffer = new ArrayBlockingQueue<>(conf.getBufferSize());
    }
    
    /**
     * Pull at least one element from buffer. Will clear list before pulling.
     * @param list
     * @return
     * @throws InterruptedException 
     */
    protected int pull(List<String> list) throws InterruptedException {
        list.clear();
        list.add(buffer.take());
        return buffer.drainTo(list);
    }
    
    protected void push(String msg) {
        if (!buffer.offer(msg)) {
            // TOOD
            LOG.error("TcpClient overflow");
        }
    }
    
    /**
     * Method must be called before the thread stops
     */
    protected void stopping() {
        stopListener.clientStopped(this);
    }
    
    
}
