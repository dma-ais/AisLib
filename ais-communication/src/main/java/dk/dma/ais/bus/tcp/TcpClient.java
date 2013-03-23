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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import net.jcip.annotations.ThreadSafe;
import dk.dma.ais.bus.AisBusComponent;
import dk.dma.ais.bus.status.AisBusComponentStatus;

/**
 * Base class for all TCP clients
 */
@ThreadSafe
public abstract class TcpClient extends Thread {

    protected final TcpClientConf conf;
    protected final Socket socket;
    private final IClientStoppedListener stopListener;
    
    protected final AisBusComponentStatus status = new AisBusComponentStatus();

    public TcpClient(IClientStoppedListener stopListener, Socket socket, TcpClientConf conf) {
        status.setInitialized();
        this.stopListener = stopListener;
        this.socket = socket;
        this.conf = conf;
    }

    /**
     * Get status for "component"
     * @return
     */
    public AisBusComponentStatus getStatus() {
        return status;
    }
    
    public String getRemoteHost() {
        InetSocketAddress address = (InetSocketAddress)socket.getRemoteSocketAddress();
        if (address == null) {
            return "not connected";
        } else {
            return address.toString();
        }            
    }

    /**
     * Method must be called before the thread stops
     */
    protected void stopping() {
        status.setStopped();
        stopListener.clientStopped(this);
    }

    /**
     * Stop the client
     */
    public void cancel() {
        this.interrupt();
        try {
            socket.close();
        } catch (IOException ignored) {
        }
        try {
            this.join(AisBusComponent.THREAD_STOP_WAIT_MAX);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String rateReport() {
        return String.format("[count/overflow] %4.2f / %4.2f  (packets/sec)", status.getInRate(), status.getOverflowRate());
    }

}
