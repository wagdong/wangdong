package jdk.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

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
		NioEventLoopGroup group1 = new NioEventLoopGroup();
		try {

			ServerBootstrap server = new ServerBootstrap();
			ServerBootstrap channel = server.group(group, group1).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(8999)).childHandler(new ChannelInitializer() {
				@Override
				protected void initChannel(Channel channel) throws Exception {
					channel.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
					channel.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));
					channel.pipeline().addLast(new NioServerSocketHandler());

					/**
					 * 当时这么写 消息一直发不出去  源码最后一个向上找
					 DefaultChannelHandlerContext ctx = this;
					 do {
					 ctx = ctx.prev;
					 } while (!(ctx.handler() instanceof ChannelOutboundHandler));
					 return ctx;

					 ##################################################
					 channel.pipeline().addLast(new DecodeHandler());
					 channel.pipeline().addLast(new NioServerSocketHandler());
					 channel.pipeline().addLast(new EncodeHandler());
					 ################################################
					 channel.pipeline().addLast(new DecodeHandler());
					 channel.pipeline().addLast(new EncodeHandler());
					 channel.pipeline().addLast(new NioServerSocketHandler());
					 */
				}
			});
			ChannelFuture f = channel.bind().sync();//绑定的服务器;sync 等待服务器关闭
			f.channel().closeFuture().sync();//关闭 channel 和 块，直到它被关闭
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
			String in = (String) msg;
			System.out.println("Server received: " + in);
			ctx.writeAndFlush(in);
		}


		@Override
		public void exceptionCaught(ChannelHandlerContext ctx,
									Throwable cause) {
			cause.printStackTrace();
			ctx.close();
		}
	}


	class DecodeHandler extends ByteToMessageDecoder {

		@Override
		protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
			if (in.isReadable()) {
				int i = in.readableBytes();
				if (i > 0) {
					byte[] arr = new byte[i];
					in.getBytes(0, arr);
					System.out.println(Arrays.toString(arr));
					out.add(new String(arr));
					in.clear();
				}
			}


		}
	}


	class EncodeHandler extends MessageToByteEncoder {
		@Override
		protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
			byte[] msg1 = ((String) msg).getBytes();
			//out.setBytes(0,msg1);
			out.writeBytes(msg1);
		}
	}
}
