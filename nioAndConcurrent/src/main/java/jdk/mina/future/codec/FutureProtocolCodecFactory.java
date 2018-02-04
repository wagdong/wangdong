package jdk.mina.future.codec;


import jdk.mina.future.message.FutureMessage;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

/**
 * 编解码器工厂类
 * @Date 2017/08/16 15:09
 */
public class FutureProtocolCodecFactory extends DemuxingProtocolCodecFactory {
    public FutureProtocolCodecFactory() {
        super.addMessageDecoder(FutureMessageDecoder.class);
        super.addMessageEncoder(FutureMessage.class, FutureMessageEncoder.class);
    }
}
