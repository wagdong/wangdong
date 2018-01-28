package jdk.mina.future.utils;

import org.apache.mina.core.buffer.IoBuffer;

import java.nio.charset.Charset;

/**
 * 期货行情处理工具类
 * @Date 2017/08/16 17:07
 */
public class FutureUtil {
    public static String readString(byte[] bs) {
        return new String(bs, Charset.forName("GBK")).trim();
    }

    public static String readString(IoBuffer in, int length) {
        byte[] bs = new byte[length];
        in.get(bs);
        return new String(bs, Charset.forName("GBK")).trim();
    }
}
