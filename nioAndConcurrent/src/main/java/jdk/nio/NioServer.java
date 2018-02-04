package jdk.nio;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * nio 理解
 * selector channel buffer
 * 通道 双工   区别传统io  单向
 * @author 汪冬
 * @Date 2018/1/12
 */
public class NioServer {

	public static void main(String[] args) {
		server();
	}
	public static void server() {
		ServerSocketChannel channel = null;
		try {

			Selector selector = Selector.open();

			channel = ServerSocketChannel.open();
			channel.configureBlocking(false);
			channel.socket().setReuseAddress(true);
			channel.bind(new InetSocketAddress(8020));
			System.out.println(channel.toString());
			channel.register(selector, SelectionKey.OP_ACCEPT, new Integer(1));



			ServerSocketChannel channel1 = ServerSocketChannel.open();
			channel1.configureBlocking(false);
			channel1.socket().setReuseAddress(true);
			channel1.bind(new InetSocketAddress(8021));
			channel1.register(selector, SelectionKey.OP_ACCEPT, new Integer(2));

			//selector 管理了channel channel1 两条通道
			System.out.println(selector.keys().size());


			while (true) {
				if (selector.select() > 0) {
					Set<SelectionKey> sets = selector.selectedKeys();
					Iterator<SelectionKey> keys = sets.iterator();
					while (keys.hasNext()) {
						SelectionKey key = keys.next();
						//SelectionKey  包含了通道的信息
						keys.remove();

						if (key.isAcceptable()) {
							key.attach(new Integer(1));
							SocketChannel schannel = ((ServerSocketChannel) key.channel()).accept();
							schannel.configureBlocking(false);
							schannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
						}

						//Tests whether this key's channel is ready for reading.
						//猜测底层tcp 推送信息的时候 会改变底层的状态
						if (key.isReadable()) {
							SocketChannel schannel = (SocketChannel) key.channel();
							//通道到缓存
							ByteArrayOutputStream output = new ByteArrayOutputStream();
							ByteBuffer buffer = ByteBuffer.allocate(1024);
							int len = 0;
							while ((len = schannel.read(buffer)) != 0) {
								buffer.flip();
								byte by[] = new byte[buffer.remaining()];
								buffer.get(by);
								output.write(by);
								buffer.clear();
							}
							String s = new String(output.toByteArray());
							System.out.println(s);
							//耗时会影响链接？？？
							Thread.sleep(100l);

					/*		TimeUnit.SECONDS.sleep(1);*/
							String attach = "chinese";
							schannel.write(ByteBuffer.wrap(attach.getBytes()));
						}

						/*if (key.isWritable()) {
							SocketChannel schannel = (SocketChannel) key.channel();
							String attach = "chinese";
							schannel.write(ByteBuffer.wrap(attach.getBytes()));
						}*/
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
