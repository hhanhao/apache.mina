package test.mina.bosc;

import static org.hamcrest.CoreMatchers.containsString;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.mina.core.filterchain.DefaultIoFilterChain;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import test.mina.ClientMinaClientHander;
import test.mina.czbank.CodecFactory;

public class ClientBosc {
	public static void main(String[] args) {
		NioSocketConnector connector = new NioSocketConnector();
		connector.setDefaultRemoteAddress(new InetSocketAddress(8445));
		DefaultIoFilterChainBuilder builder = connector.getFilterChain();
		BoscDecoder boscDecoder = new BoscDecoder();
		BoscEncoder boscEncoder = new BoscEncoder();
		ProtocolCodecFilter filter = new ProtocolCodecFilter(new CodecFactory(boscEncoder, boscDecoder));
		builder.addLast("codec", filter);
		connector.setHandler(new ClientMinaClientHander());
		connector.setConnectTimeoutCheckInterval(3000);
		connector.getSessionConfig().setUseReadOperation(true);
		ConnectFuture future = connector.connect();
		future.awaitUninterruptibly(3000);
		IoSession ioSession = future.getSession();
		WriteFuture write = ioSession.write(getMsg());
		write.awaitUninterruptibly(3000);
		ReadFuture read = ioSession.read();
		read.awaitUninterruptibly(3000);
		Object message = read.getMessage();
		System.out.println("ClientBosc.main() get server response messagen :" + message);
	}
	public static String getMsg() {
		String msg = "";
		msg = getDzMsg();
		String length = "00000";
		try {
			length = "00000" + msg.getBytes("UTF-8").length;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		length = length.substring(length.length()-5, length.length());
		StringBuffer head = new StringBuffer();
		head.append("X1.0").append(length).append("B291031111111100xxxxxxxx00000000");
		return head.append(msg).toString();
		
	}
	public static String getInOutMsg() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		String time = sdf.format(new Date());
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><Pub><Version>V1.0</Version><InstructionCode>29103</InstructionCode>"
					+ "<Date>20170821</Date><Time>"+time+"</Time>"
					+ "<TradeSource>B</TradeSource><MarketCode>0000999</MarketCode></Pub>" 
				   + "<Serial><BankSerial>2017821"+time+new Date().getTime()+"</BankSerial>"
				   + "<MarketSerial>2017821103021001</MarketSerial></Serial>"
				   + "<BusModel><BankModOption>0</BankModOption></BusModel>"
				   + "<BusReturnInfo><BusRtnCode>0000</BusRtnCode><BusRtnInfo>交易成功</BusRtnInfo></BusReturnInfo>"
				   + "<MoneyKind><MoneyKind>0</MoneyKind><CashExCode></CashExCode></MoneyKind>"
				   + "<Transfer><TransferAmount>100000</TransferAmount><CurrentBalance></CurrentBalance></Transfer>"
				   + "<SummaryInfo><Summary1>$!{Summary1}</Summary1><Summary2>$!{Summary2}</Summary2></SummaryInfo>"
				   + "</root>";
		msg = msg + "\n";
		return msg;
	}
	
	public static String getDzMsg() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		String time = sdf.format(new Date());
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><Pub><Version>V1.0</Version><InstructionCode>29203</InstructionCode>"
					+ "<Date>20170821</Date><Time>"+time+"</Time>"
					+ "<TradeSource>B</TradeSource><MarketCode>0000999</MarketCode></Pub>" 
				   + "<Serial><BankSerial>2017821"+time+new Date().getTime()+"</BankSerial>"
				   + "<MarketSerial>2017821103021001</MarketSerial></Serial>"
				   + "<MoneyKind><MoneyKind>0</MoneyKind><CashExCode></CashExCode></MoneyKind>"
				   + "<FileInfo><FileName>99999999_bTransfer_$.20170821</FileName><FileLength></FileLength></FileInfo>"
				   + "<SummaryInfo><Summary1>$!{Summary1}</Summary1><Summary2>$!{Summary2}</Summary2></SummaryInfo>"
				   + "</root>";
		msg = msg + "\n";
		return msg;
	}
}
