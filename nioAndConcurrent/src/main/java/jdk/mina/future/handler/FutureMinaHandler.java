package jdk.mina.future.handler;


import jdk.mina.future.FutureQuoteClient;
import jdk.mina.future.message.HeartBeatFutureMessage;
import jdk.mina.future.message.HqFutureContractMess;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mina的消息处理器
 * @Date 2017/08/17 10:14
 */
public class FutureMinaHandler extends IoHandlerAdapter {
	private final static Logger LOGGER = LoggerFactory.getLogger(FutureMinaHandler.class);


	private FutureQuoteClient futureQuoteClient;



	public FutureMinaHandler(FutureQuoteClient futureQuoteClient) {
		this.futureQuoteClient = futureQuoteClient;

	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("光大期货客户端打开");
		}
		session.write(new HeartBeatFutureMessage());

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {

	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if (message instanceof HqFutureContractMess) {

		} else if (message instanceof HeartBeatFutureMessage) {
			// 更新心跳最后收到时间
			futureQuoteClient.updateHeartTime();
		} else {
			System.err.println("擦,发的什么鬼玩意儿");
		}
	}



}
