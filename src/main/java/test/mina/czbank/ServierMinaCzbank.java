package test.mina.czbank;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.mina.ClientMinaServerHanlder;
import test.mina.ServerMina;
import test.mina.bosc.BoscDecoder;
import test.mina.bosc.BoscEncoder;

public class ServierMinaCzbank {
	private static final Logger logger = LoggerFactory.getLogger(ServerMina.class);
	public static void main(String[] args) {
		SocketAcceptor acceptor = new NioSocketAcceptor();
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		TextLineCodecFactory protocolCodecFactory = new TextLineCodecFactory(Charset.forName("gbk"));
		protocolCodecFactory.setDecoderMaxLineLength(Integer.MAX_VALUE);
		protocolCodecFactory.setEncoderMaxLineLength(Integer.MAX_VALUE);
		BoscDecoder decoder = new BoscDecoder();
		BoscEncoder encoder = new BoscEncoder();
		CodecFactory codecFactory = new CodecFactory(encoder, decoder);
		ProtocolCodecFilter filter = new ProtocolCodecFilter(codecFactory);
		chain.addLast("codec", filter);
		acceptor.setHandler(new ServerHanlder());
		int bindPort = 9988;
		try {
			acceptor.bind(new InetSocketAddress(bindPort));
		} catch (IOException e) {
			logger.error("Server mina start error", e);
		}
		logger.info("Mina Server run done! on port:"+bindPort);
	}

	

}
