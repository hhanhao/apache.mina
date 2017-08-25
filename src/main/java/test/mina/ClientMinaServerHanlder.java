package test.mina;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMinaServerHanlder extends IoHandlerAdapter{
	private static final Logger logger = LoggerFactory.getLogger(ClientMinaServerHanlder.class);
	
	private AtomicInteger count = new AtomicInteger(0);
    /**
     * {@inheritDoc}
     */
    public void sessionCreated(IoSession session) throws Exception {
    	logger.info("new client created");
    }

    /**
     * {@inheritDoc}
     */
    public void sessionOpened(IoSession session) throws Exception {
    	count.incrementAndGet(); 
        System.out.println("第 " + count + " 个 client 登陆！address： : " 
                + session.getRemoteAddress()); 
    }

    /**
     * {@inheritDoc}
     */
    public void sessionClosed(IoSession session) throws Exception {
    	logger.info("sessionClosed");
    }

    /**
     * {@inheritDoc}
     */
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
    	logger.info("sessionIdle");
    }

    /**
     * {@inheritDoc}
     */
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
    	logger.error("counted a exception" + cause);
    }

    /**
     * {@inheritDoc}
     */
    public void messageReceived(IoSession session, Object message) throws Exception {
    	logger.info("messageReceived");
    	logger.info("server get msg:" + message);
//    	HimiObject ho = (HimiObject)message;
//    	logger.info("ho.name"+ ho.getName());
//    	ho.setName("serverHIMI");
//    	session.write(ho);
    	session.write(message);
    }

    /**
     * {@inheritDoc}
     */
    public void messageSent(IoSession session, Object message) throws Exception {
    	logger.info("messageSent" + message.toString());
    }

    /**
     * {@inheritDoc}
     */
    public void inputClosed(IoSession session) throws Exception {
        session.closeNow();
        logger.info("inputClosed");
    }
}
