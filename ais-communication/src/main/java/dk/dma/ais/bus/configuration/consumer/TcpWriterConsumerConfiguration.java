package dk.dma.ais.bus.configuration.consumer;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TcpWriterConsumerConfiguration extends AisBusConsumerConfiguration {

    private int port;
    private String host;
    private boolean gzipCompress = false;
    private int gzipBufferSize = 2048;
    private int reconnectInterval = 20000;

    public TcpWriterConsumerConfiguration() {

    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isGzipCompress() {
        return gzipCompress;
    }

    public void setGzipCompress(boolean gzipCompress) {
        this.gzipCompress = gzipCompress;
    }

    public int getGzipBufferSize() {
        return gzipBufferSize;
    }

    public void setGzipBufferSize(int gzipBufferSize) {
        this.gzipBufferSize = gzipBufferSize;
    }

    public int getReconnectInterval() {
        return reconnectInterval;
    }

    public void setReconnectInterval(int reconnectInterval) {
        this.reconnectInterval = reconnectInterval;
    }

}
