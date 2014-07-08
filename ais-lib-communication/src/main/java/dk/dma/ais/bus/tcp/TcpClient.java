/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.dma.ais.bus.tcp;

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
            this.join(AisBusComponent.THREAD_STOP_WAIT_MAX);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String rateReport() {
        return String.format("[count/overflow] %4.2f / %4.2f  (packets/sec)", status.getInRate(), status.getOverflowRate());
    }

}
