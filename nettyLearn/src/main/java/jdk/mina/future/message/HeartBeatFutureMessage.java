package jdk.mina.future.message;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * @Date 2017/08/17 13:48
 */
public class HeartBeatFutureMessage extends BaseFutureMessage {
    @Override
    public IoBuffer writeData() throws Exception {
        IoBuffer buff = IoBuffer.allocate(26);
        buff.putInt(new String("ConnectTestSucceed").getBytes("GBK").length + 4);
        buff.putInt(90001);
        buff.put(new String("ConnectTestSucceed").getBytes("GBK"));
        buff.flip();
        return buff;
    }

    @Override
    public boolean readData(IoBuffer ioBuffer, int expectLength) {
        byte[] byte2 = new byte[18];
        ioBuffer.get(byte2);
        return true;
    }
}
