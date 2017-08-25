package test.mina;


import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMina {
	private static final Logger logger = LoggerFactory.getLogger(ServerMina.class);
	public static void main(String[] args) {
		SocketAcceptor acceptor = new NioSocketAcceptor();
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		ProtocolCodecFilter filter = new ProtocolCodecFilter(new ObjectSerializationCodecFactory());
		chain.addLast("codec", filter);
		acceptor.setHandler(new ClientMinaServerHanlder());
		int bindPort = 9988;
		try {
			acceptor.bind(new InetSocketAddress(bindPort));
		} catch (IOException e) {
			logger.error("Server mina start error", e);
		}
		logger.info("Mina Server run done! on port:"+bindPort);
		logger.info("Mina Server run done! on port:"+bindPort);
		logger.info("Mina Server run done! on port:"+bindPort);
		logger.info("Mina Server run done! on port:"+bindPort);
	}
}
