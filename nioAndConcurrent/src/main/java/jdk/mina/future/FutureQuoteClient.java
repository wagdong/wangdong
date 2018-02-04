package jdk.mina.future;



import jdk.mina.future.codec.FutureProtocolCodecFactory;
import jdk.mina.future.handler.FutureMinaHandler;
import jdk.mina.future.message.HeartBeatFutureMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 期货行情接入
 * @Date 2017/08/17 14:44
 */
public class FutureQuoteClient {
	private final static Logger LOGGER = LoggerFactory.getLogger(FutureQuoteClient.class);

	private static final long CONNECT_TIMEOUT = 10 * 1000L; // 30 seconds
	private static final int IDLE_TIME = 300; // 10min
	private NioSocketConnector connector;
	private volatile IoSession session;

	private boolean isAutoConn = true; // 是否继续重连

	private AtomicLong lastHeartTime = new AtomicLong(System.currentTimeMillis());
	private static final long HEART_TEST_MAX_TIME = 30 * 1000L;

	private String ip;
	private int port;



	public void start() {
		ip = "111";
		port =11;
		if (StringUtils.isBlank(ip)) {
			LOGGER.error("服务器IP未配置,请检查");
		}
		if (port == 0) {
			LOGGER.error("服务器端口未配置,请检查");
		}
		connector = new NioSocketConnector();
		connector.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, IDLE_TIME);

		connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new FutureProtocolCodecFactory()));
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.setHandler(new FutureMinaHandler(this));
		connector.getSessionConfig().setReuseAddress(true);
		connect();
	}

	public void connect() {
		reconnectCount();
		if (session == null || !session.isConnected())
			LOGGER.info("Connected failed to " + getHost());

		if (isAutoConn)
			new AutoReconnector("GdqhAutoReconnectorWorker").start();
	}

	/**
	 * 尝试连接几次
	 */
	private void reconnectCount() {
		int count = 0;
		while (count <= 3) {
			_connect();
			if (session == null || !session.isConnected()) {
				count++;
			} else {
				break;
			}
		}
	}

	private void _connect() {
		ConnectFuture cf = connector.connect(new InetSocketAddress(ip, port));
		cf.awaitUninterruptibly();
		session = cf.isConnected() ? cf.getSession() : null;
	}

	private synchronized void _reconnect() {
		disconnect();
		reconnectCount();
		if (session == null || !session.isConnected()) {
			LOGGER.info("Connected failed to " + getHost());
		} else {
			LOGGER.info("Reconnected successfully to " + getHost());
			LOGGER.info(toString());
		}
	}

	public void disconnect() {
		LOGGER.info("Disconnect to " + getHost());
		if (session == null || session.isClosing())
			return;

		if (haveConnected()) {
			LOGGER.info("disconnect session" + session.getReaderIdleCount());
			session.setAttribute("MinaCommunicator.disconnect", true);
			session.close(true).awaitUninterruptibly();
//			session.close();
		}
	}

	public void updateHeartTime() {
		long updateTime = System.currentTimeMillis();
		for (; ; ) {
			long lastTime = lastHeartTime.get();
			if (lastHeartTime.compareAndSet(lastTime, updateTime)) {
				return;
			}
		}
	}

	private String getHost() {
		return ip + ":" + port;
	}

	public boolean haveConnected() {
		return session == null ? false : session.isConnected();
	}

	private boolean reconnecting = false;

	class AutoReconnector extends Thread {

		public AutoReconnector(String threadName) {
			super(threadName);
		}

		public void run() {
			int count = 0;
			while (isAutoConn) {
				if ((!reconnecting && !haveConnected()) || (System.currentTimeMillis() - lastHeartTime.get() > HEART_TEST_MAX_TIME)) {
					count++;
					if (System.currentTimeMillis() - lastHeartTime.get() > HEART_TEST_MAX_TIME) {
						LOGGER.error("服务端没有回心跳包导致客户端重连");
					}
					LOGGER.error("Preparing for reconnection《" + (count) + "》 ..........");
					reconnecting = true;
					reconn();
					updateHeartTime();
					LOGGER.error("reconnected 《" + (count) + " successfully!...........");
					reconnecting = false;
				}
				// 正常连接状态，发心跳包
				session.write(new HeartBeatFutureMessage());
				try {
					Thread.sleep(5000);
				} catch (Exception ex) {
				}
			}
		}

		private void reconn() {
			int count = 0;
			do {
				try {
					Thread.sleep(Math.min(++count, 10) * 1000);
				} catch (Exception ex) {
				}

				LOGGER.info("Reconnecting【" + count + "】 ..................");

				_reconnect();
			} while (!haveConnected());
		}
	}
}
