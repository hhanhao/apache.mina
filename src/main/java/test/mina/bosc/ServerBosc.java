package test.mina.bosc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.mina.ServerMina;
import test.mina.czbank.CodecFactory;
import test.mina.czbank.CzbankDecoder;
import test.mina.czbank.CzbankEncoder;
import test.mina.czbank.ServerHanlder;

public class ServerBosc {
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
		acceptor.setHandler(new BoscServerHandler());
		int bindPort = 47601;
		try {
			acceptor.bind(new InetSocketAddress(bindPort));
		} catch (IOException e) {
			logger.error("Server mina start error", e);
		}
		logger.info("Mina Server run done! on port:"+bindPort);
	}
}
