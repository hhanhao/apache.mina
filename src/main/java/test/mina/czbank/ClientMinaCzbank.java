package test.mina.czbank;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.Scanner;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.mina.ClientMina;
import test.mina.ClientMinaClientHander;
import test.mina.WriteFutureListisen;

public class ClientMinaCzbank {
	private static final Logger logger = LoggerFactory.getLogger(ClientMina.class);
	public static void main(String[] args) {
		
		NioSocketConnector connector = new NioSocketConnector();
		connector.setDefaultRemoteAddress(new InetSocketAddress(8080));
		
		
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		CzbankDecoder decoder = new CzbankDecoder();
		CzbankEncoder encoder = new CzbankEncoder();
		ProtocolCodecFilter filter = new ProtocolCodecFilter(new CodecFactory(encoder, decoder));
		chain.addLast("codec", filter);
		int bindPort = 9988;
		connector.setHandler(new ClientMinaClientHander());
		connector.setConnectTimeoutCheckInterval(3000);
		connector.getSessionConfig().setUseReadOperation(true);
		Scanner scanner = new Scanner(System.in);
		ConnectFuture cf = connector.connect(new InetSocketAddress(bindPort));
		while(scanner.hasNextLine()) {
			cf.awaitUninterruptibly();
			if (cf.isConnected()) {
				logger.info("connect is success");
			}else {
				logger.info("connect is error");
			}
			
			IoSession ioSession = cf.getSession();
			String readLine = scanner.nextLine();
			if ("end".equals(readLine)) {
				break;
			}
			WriteFuture writeFuture = ioSession.write(getMsg(readLine));
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
				String ho2 = (String)readFuture.getMessage();
				System.out.println("returnMsg:" + ho2);
			}
			CloseFuture closeFuture = ioSession.closeOnFlush();
			closeFuture.awaitUninterruptibly()	;
			if (closeFuture.isClosed()) {
				logger.info("Iosession is over");
				logger.info("connect is connect:" + connector.isActive());
			}
		}
		scanner.close();
		
		System.out.println("wo zai deng dai ================");
		connector.dispose();
	
	}
	
//	@Test
//	public void test() {
//		getMsg();
//	}
	public static String getMsg(String msg) {
		String mString = "00000000";
		String length = msg.length() + "";
		mString = mString + length;
		mString = mString.substring(mString.length()-8, mString.length());
		return mString + msg + "\n";
	}
	
	public static String getMsg() {
		String length = "";
		String message = "<ap><head><tr_code>交易码</tr_code><cms_corp_no>企业代码</cms_corp_no><user_no>操作员号</user_no><org_code>机构号</org_code><serial_no>交易序号</serial_no><req_no>发起方序号</req_no><tr_acdt>交易日期</tr_acdt><tr_time>交易时间</tr_time><succ_flag>1</succ_flag><ret_info>返回信息</ret_info><ret_code>9999</ret_code><ext_info>返回扩展信息</ext_info><file_flag>文件标识</file_flag><reserved>保留位</reserved></head></ap>";
		message = message+"\n";
//		String message = "aa";
		try {
			length = message.getBytes("utf-8").length+"";
//			logger.info("msg:" + length);
			length = "00000000" + length;
			length = length.substring(length.length()-8, length.length());
//			logger.info("msg:" + length);
			message = length + message;
//			logger.info("msg:" + message);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return message;
	}

}
