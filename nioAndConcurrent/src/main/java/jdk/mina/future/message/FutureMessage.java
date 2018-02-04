package jdk.mina.future.message;


import org.apache.mina.core.buffer.IoBuffer;

/**
 * 期货行情消息接口类
 * @Date 2017/08/16 16:27
 */
public interface FutureMessage {
    boolean readData(IoBuffer ioBuffer, int expectLength);

    IoBuffer writeData() throws Exception;
}
