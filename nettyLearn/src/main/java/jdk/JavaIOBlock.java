package jdk;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author 汪冬
 * @Date 2018/1/10
 * tips 每个线程都有一个默认的堆栈内存分配了 128K 和 1M 之间的空间
 */
public class JavaIOBlock {


	public static void main(String[] args) throws Exception {
		new Thread(new Runnable() {
			public void run() {
				try {
					server();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		Thread.sleep(200l);
		new JavaIOBlock().client();
	}
	public void client() throws IOException {
		int i=0;
		for (;;){
			i++;
			if(i>10000){
				break;
			}
			long l = System.currentTimeMillis();
			System.out.println(l);
			Socket socket=new Socket();
			socket.connect(new InetSocketAddress(1112));
			InputStream inputStream = socket.getInputStream();
			byte[] bytes = new byte[1024];
			inputStream.read(bytes);
			System.out.println(System.currentTimeMillis()-l+"\t"+i+"\t"+new String(bytes));
		}


	}

	public static void server() throws Exception{
		//相当于串行 执行 效率太差
		ServerSocket serverSocket = new ServerSocket(1112);
		while (true){
			Socket clientSocket = serverSocket.accept();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out =
					new PrintWriter(clientSocket.getOutputStream(), true);
			String request, response;
			Thread.sleep(2000l);
			response = "chinese";
			out.println(response);
			out.flush();
		}
		}
}
