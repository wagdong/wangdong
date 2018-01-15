package jdk.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @author 汪冬
 * @Date 2018/1/15
 */
public class NettyServer {

	public static void main(String[] args) {
		new NettyServer().server();
	}

	public void server() {
		//eventLoop
		NioEventLoopGroup group = new NioEventLoopGroup();
		NioEventLoopGroup group1= new NioEventLoopGroup();
		try {

			ServerBootstrap server = new ServerBootstrap();
			ServerBootstrap channel = server.group(group,group1).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(8999)).childHandler(new ChannelInitializer() {
				@Override
				protected void initChannel(Channel channel) throws Exception {
					channel.pipeline().addLast(new NioServerSocketHandler());
				}
			});
			ChannelFuture f = channel.bind().sync();//绑定的服务器;sync 等待服务器关闭
			f.channel().closeFuture().sync();//关闭 channel 和 块，直到它被关闭
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				group.shutdownGracefully().sync();//释放所有的资源
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	class NioServerSocketHandler extends ChannelInboundHandlerAdapter {

		@Override
		public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
			System.out.println("channelRegistered....");
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("channelActive");
		}


		@Override
		public void channelRead(ChannelHandlerContext ctx,
								Object msg) {
			ByteBuf in = (ByteBuf) msg;
			System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
			ctx.writeAndFlush(in);
		}


		@Override
		public void exceptionCaught(ChannelHandlerContext ctx,
									Throwable cause) {
			cause.printStackTrace();
			ctx.close();
		}
	}
}
