package jdk.mina.future.message;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * 期货行情消息基类
 * @Date 2017/08/16 16:27
 */
public class BaseFutureMessage implements FutureMessage {

    @Override
    public boolean readData(IoBuffer ioBuffer, int expectLength) {
        return true;
    }

    @Override
    public IoBuffer writeData() throws Exception {
        return null;
    }
}
