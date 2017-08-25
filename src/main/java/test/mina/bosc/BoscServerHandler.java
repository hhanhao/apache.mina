package test.mina.bosc;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.mina.czbank.ServerHanlder;

public class BoscServerHandler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(ServerHanlder.class);
	
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
    	logger.info("bosc server get msg:" + message);
    	String sendMsg = getMsg();
    	session.write(sendMsg);
    }
    
    public static String getMsg() {
		String msg = "";
		msg = getDzReciverMsg();
		String length = "00000";
		try {
			length = "00000" + msg.getBytes("UTF-8").length;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
		}
		length = length.substring(length.length()-5, length.length());
		StringBuffer head = new StringBuffer();
		head.append("X1.0").append(length).append("B291031111111100xxxxxxxx00000000");
		return head.append(msg).toString();
		
	}
    
    public static String getDzReciverMsg() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		String time = sdf.format(new Date());
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><Pub><Version>V1.0</Version><InstructionCode>19203</InstructionCode>"
					+ "<Date>20170821</Date><Time>"+time+"</Time>"
					+ "<TradeSource>B</TradeSource><MarketCode>0000999</MarketCode></Pub>" 
				   + "<Serial><BankSerial>2017821"+time+new Date().getTime()+"</BankSerial>"
				   + "<MarketSerial>2017821"+time+new Date().getTime()+"</MarketSerial></Serial>"
				   + "<ReturnInfo><RtnCode>0000</RtnCode><RtnInfo>交易成功</RtnInfo></ReturnInfo>"
				   + "<MoneyKind><MoneyKind>0</MoneyKind><CashExCode></CashExCode></MoneyKind>"
				   + "<Transfer><TransferAmount>100000</TransferAmount><CurrentBalance></CurrentBalance></Transfer>"
				   + "<SummaryInfo><Summary1>$!{Summary1}</Summary1><Summary2>$!{Summary2}</Summary2></SummaryInfo>"
				   + "</root>";
		msg = msg + "\n";
		return msg;
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
    public static void main(String[] args) {
    	String string = "X1.000593R1920381238888000000000000000000";
		System.out.println(string.length());
	}
    
}
