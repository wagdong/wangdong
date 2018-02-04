package jdk.mina.future.codec;



import jdk.mina.future.constant.EventEnum;
import jdk.mina.future.message.FutureMessage;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 期货行情解码器
 * @Date 2017/08/16 15:14
 */
public class FutureMessageDecoder implements MessageDecoder {
    private final static Logger LOGGER = LoggerFactory.getLogger(FutureMessageDecoder.class);

    private static final int HEAD_LEN = 4;
/*
	private static final  int HEART_LEN=18;

	private static final  int body_LEN=343;*/

    private static final int MAX_LENGTH = 4096;

    @Override
    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        MessageDecoderResult decoderResult = decodeHead(in);
        return decoderResult;
    }

    private MessageDecoderResult decodeHead(IoBuffer in) {
        if(in.remaining() < HEAD_LEN) {
            return NEED_DATA;
        }
        int readLen =in.getInt();
        if(readLen <= 0 || readLen > MAX_LENGTH - HEAD_LEN) {
        /*    if(LOGGER.isWarnEnabled()){
                LOGGER.warn("光大行情解析出错，解析出的长度为 " + readLen);
            }*/
            return OK;
        }
        if(in.remaining() < readLen) {
            return NEED_DATA;
        }
        return OK;
    }

    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        FutureMessage m = decodeBody(session, in);
        if (m == null) {
          /*  if(LOGGER.isWarnEnabled()){
                LOGGER.warn("解包错误，解出来的是null");
            }*/
            in.position(in.position()+in.remaining());
            return MessageDecoderResult.OK;
        }
        out.write(m);

        return MessageDecoderResult.OK;
    }

    protected FutureMessage decodeBody(IoSession session, IoBuffer in) throws Exception {
        try {
            // 解析长度
            int readLen = in.getInt();
            if(readLen <= 0 || readLen > MAX_LENGTH - HEAD_LEN) {
             /*   if(LOGGER.isWarnEnabled())
                LOGGER.warn("行情解析出错，解析出的长度为 " + readLen);*/
                return null;
            }
            int eventId = in.getInt();
            EventEnum eventEnum = EventEnum.getByEventId(eventId);
            if(eventEnum != null) {
                Class<? extends FutureMessage> clazz = eventEnum.getMessageType();
                FutureMessage futureMessage = clazz.newInstance();
                boolean dealFlag = futureMessage.readData(in, readLen - 8);
                if(dealFlag) {
                    return futureMessage;
                } else {
                    LOGGER.error("期望包长和实际解析后的包长不一致 - eventId="+eventId);
                }
            } else {
               // LOGGER.error("解析包找不到处理类！eventId="+eventId);
                return  null;
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {

    }
}
