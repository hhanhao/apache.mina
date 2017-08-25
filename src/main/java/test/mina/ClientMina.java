package test.mina;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.omg.PortableInterceptor.HOLDING;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMina {
	private static final Logger logger = LoggerFactory.getLogger(ClientMina.class);
	public static void main(String[] args) {
		NioSocketConnector connector = new NioSocketConnector();
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		ProtocolCodecFilter filter = new ProtocolCodecFilter(new ObjectSerializationCodecFactory());
		chain.addLast("codec", filter);
		int bindPort = 9988;
		connector.setHandler(new ClientMinaClientHander());
		connector.setConnectTimeoutCheckInterval(3000);
		connector.getSessionConfig().setUseReadOperation(true);
		ConnectFuture cf = connector.connect(new InetSocketAddress(bindPort));
		cf.awaitUninterruptibly();
		if (cf.isConnected()) {
			logger.info("connect is success");
		}else {
			logger.info("connect is error");
		}
		
		//get IoSession
		IoSession ioSession = cf.getSession();
		HimiObject ho = new HimiObject(100, "hanhao");
		WriteFuture writeFuture = ioSession.write(ho);
		writeFuture.awaitUninterruptibly();
		writeFuture.addListener(new WriteFutureListisen());
		if (writeFuture.isWritten()) {
			logger.info("write info sucess");
		}else {
			logger.error("write error");
		}
		ReadFuture readFuture = ioSession.read();
		readFuture.awaitUninterruptibly();
		if (readFuture.isRead()) {
			HimiObject ho2 = (HimiObject) readFuture.getMessage();
			System.out.println("==================id:"+ho2.getId()+" name:"+ho2.getName());
		}
		CloseFuture closeFuture = ioSession.closeOnFlush();
		closeFuture.awaitUninterruptibly();
		if (closeFuture.isClosed()) {
			logger.info("Iosession is over");
		}
//		ioSession.getCloseFuture().awaitUninterruptibly(3000);
		
		System.out.println("wo zai deng dai ================");
		connector.dispose();
	
	}

}
