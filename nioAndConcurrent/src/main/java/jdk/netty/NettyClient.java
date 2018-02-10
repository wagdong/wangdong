package jdk.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * @author 汪冬
 * @Date 2018/1/15
 */
public class NettyClient {

	public static void main(String[] args) {
		new NettyClient().client();
	}

	public void client() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
					.channel(NioSocketChannel.class)
					.remoteAddress(new InetSocketAddress(8999))
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch)
								throws Exception {
							ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
							ch.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));
							ch.pipeline().addLast(new EchoClientHandler());

						}
					});

			ChannelFuture f = b.connect().sync();
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				group.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		ArrayList<String> strings = new ArrayList<String>() {
			{
				add("1");
			}
		};
	}

	//channelRegistered -->channelActive-->channelInactive-->channelUnregistered
	class EchoClientHandler extends SimpleChannelInboundHandler<String> {
		@Override
		public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
			System.out.println("EchoClientHandler channelRegistered...");

		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) {
			System.out.println(1);
			ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", //2
					CharsetUtil.UTF_8));
		}

		@Override
		public void channelRead0(ChannelHandlerContext ctx,
								 String in) {
			System.out.println(2);
			System.out.println("Client received: " + in);    //3
			ctx.writeAndFlush("Netty rocks2!");
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx,
									Throwable cause) {                    //4
			cause.printStackTrace();
			ctx.close();
		}
	}
}
