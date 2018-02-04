package jdk.mina.future.codec;


import jdk.mina.future.message.FutureMessage;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * 期货行情编码器
 * @Date 2017/08/16 15:10
 */
public class FutureMessageEncoder  <T extends FutureMessage>  implements MessageEncoder<T> {

    protected void encodeBody(IoSession session, T message, IoBuffer out) throws Exception {
        out.put(message.writeData());
    }

    public void encode(IoSession session, T message, ProtocolEncoderOutput out) throws Exception {
        IoBuffer buf = IoBuffer.allocate(16);
        buf.setAutoExpand(true); // Enable auto-expand for easier encoding
        encodeBody(session, message, buf);
        buf.flip();
        out.write(buf);
    }

}
