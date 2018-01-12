package jdk.nio;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

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


			System.out.println(selector.keys().size());

			/*System.out.println("interestOps=" + register.interestOps());
			boolean isInterestedInWrite = (register.interestOps() & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE;
			System.out.println(isInterestedInWrite);
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			System.out.println(selectionKeys.size());
*/

			while (true) {
				if (selector.select() > 0) {
					Set<SelectionKey> sets = selector.selectedKeys();
					Iterator<SelectionKey> keys = sets.iterator();
					while (keys.hasNext()) {
						SelectionKey key = keys.next();
						keys.remove();

						if (key.isAcceptable()) {
							key.attach(new Integer(1));
							SocketChannel schannel = ((ServerSocketChannel) key.channel()).accept();
							schannel.configureBlocking(false);
							schannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
						}

						if (key.isReadable()) {
							SocketChannel schannel = (SocketChannel) key.channel();
							ByteBuffer allocate = ByteBuffer.allocate(1024);
							ByteOutputStream output = new ByteOutputStream();
						/*	int len = 0;
							while ((len = schannel.read(buf)) != 0) {
								buf.flip();
								byte by[] = new byte[buf.remaining()];
								buf.get(by);
								output.write(by);
								buf.clear();
							}*/
							String str = new String(output.getBytes());
							System.out.println(str);
							key.attach(str);
							//TimeUnit.SECONDS.sleep(1);

						}

						if (key.isWritable()) {

							/*Object object = key.attachment();
							String attach = "chinese";
							TimeUnit.SECONDS.sleep(1);

							schannel.write(ByteBuffer.wrap(attach.getBytes()));*/
							SocketChannel schannel = (SocketChannel) key.channel();
							String attach = "chinese";
							schannel.write(ByteBuffer.wrap(attach.getBytes()));
						}
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
