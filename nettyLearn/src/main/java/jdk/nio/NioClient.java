package jdk.nio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author 汪冬
 * @Date 2018/1/12
 */
public class NioClient {

	public static void main(String[] args) {
		client();
	}

	public static void client() {
		for (int i=0;i<100;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					socketChannel();
				}
			}).start();
		}


	}

	private static void socketChannel(){
		SocketChannel channel = null;
		try {

			Selector selector = Selector.open();
			channel = SocketChannel.open();
			channel.configureBlocking(false);
			channel.connect(new InetSocketAddress(8021));
			channel.register(selector, SelectionKey.OP_CONNECT);
			boolean key1=false;
			while (true) {
				if (selector.select() > 0) {

					Iterator<SelectionKey> set = selector.selectedKeys().iterator();
					while (set.hasNext()) {
						SelectionKey key = set.next();
						set.remove();
						SocketChannel ch = (SocketChannel) key.channel();
						if (key.isConnectable()) {
							ch.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, new Integer(1));
							// 如果正在连接，则完成连接
							if(channel.isConnectionPending()){
								channel.finishConnect();
							}
							// 设置成非阻塞
							channel.configureBlocking(false);
							ch.write(ByteBuffer.wrap((("client say:hi")).getBytes()));
						}

						if (key.isReadable()) {
							key.attach(new Integer(1));
							ByteArrayOutputStream output = new ByteArrayOutputStream();
							ByteBuffer buffer = ByteBuffer.allocate(1024);
							int len = 0;
							while ((len = ch.read(buffer)) != 0) {
								buffer.flip();
								byte by[] = new byte[buffer.remaining()];
								buffer.get(by);
								output.write(by);
								buffer.clear();
							}
							String s = new String(output.toByteArray());
							if(s.equalsIgnoreCase("chinese")){
								System.out.println(s);
								output.close();
							}

						}

						if (key.isWritable()) {
							key.attach(new Integer(1));
							//ch.write(ByteBuffer.wrap((("client say:hi")).getBytes()));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
