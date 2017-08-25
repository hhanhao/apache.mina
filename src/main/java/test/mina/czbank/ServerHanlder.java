package test.mina.czbank;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHanlder extends IoHandlerAdapter{
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
    	logger.info("messageReceived");
    	logger.info("server get msg:" + message);
//    	HimiObject ho = (HimiObject)message;
//    	logger.info("ho.name"+ ho.getName());
//    	ho.setName("serverHIMI");
//    	session.write(ho);
    	String sendMsg = "00" + getMessageError();
		String lenStr;
		String msg = null;
		try {
			lenStr = "0000000000" + String.valueOf(sendMsg.getBytes("gbk").length);
			msg = lenStr.substring(lenStr.length() - 10) + sendMsg;
			logger.info("=====================return msg:" + msg);
		} catch (UnsupportedEncodingException e) {
			logger.error("浙商 子账户转码失败！" + e);
		}
    	session.write(msg);
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
    
    private static String get300001() {
    	String msg = 			
    					"<ap>"				
    					+"<head>"
    					+"<tr_code>交易码</tr_code>"
    					+"<cms_corp_no>企业代码</cms_corp_no>"
    					+"<user_no>操作员号</user_no>"
    					+"<org_code>机构号</org_code>"
    					+"<serial_no>交易序号</serial_no>"
    					+"<req_no>发起方序号</req_no>"
    					+"<tr_acdt>交易日期</tr_acdt>"
    					+"<tr_time>交易时间</tr_time>"
    					+"<succ_flag>1</succ_flag>"
    					+"<ret_info>返回信息</ret_info>"
    					+"<ret_code>9999</ret_code>"
    					+"<ext_info>返回扩展信息</ext_info>"
    					+"<file_flag>文件标识</file_flag>"
    					+"<reserved>保留位</reserved>"
    					+"</head>"
    					+"<body>"
    					+"<host_serial_no>1234324234123"
    					+ "</host_serial_no>"
    					+ "</body>"
    					+"</ap>"
    					;

    	return "";
    }
    private static String get200205() {
    	String msg = 			
    					"<ap>"				
    					+"<head>"
    					+"<tr_code>200205</tr_code>"
    					+"<cms_corp_no>企业代码</cms_corp_no>"
    					+"<user_no>操作员号</user_no>"
    					+"<org_code>机构号</org_code>"
    					+"<serial_no>交易序号</serial_no>"
    					+"<req_no>发起方序号</req_no>"
    					+"<tr_acdt>20170801</tr_acdt>"
    					+"<tr_time>092830</tr_time>"
    					+"<succ_flag>1</succ_flag>"
    					+"<ret_info>返回信息</ret_info>"
    					+"<ret_code>9999</ret_code>"
    					+"<ext_info>返回扩展信息</ext_info>"
    					+"<file_flag>文件标识</file_flag>"
    					+"<reserved>保留位</reserved>"
    					+"</head>"
    					+"<body>"
    					+"<serial_no>20170801090001</serial_no>"
    					+"<req_no>20170801090001</req_no>"
    					+"<cert_no>20170801090001</cert_no>"
    					+"<cms_corp_no>88992</cms_corp_no>"
    					+"<tr_acdt>20170801</tr_acdt>"
    					+"<tr_time>092830</tr_time>"
    					+"<amt>1</amt>"
    					+"<fee_amt>0</fee_amt>"
    					+ "</body>"
    					+"</ap>"
    					;

    	return "";
    }
    private static String getMessageError() {
    	String msg = 			
    					"<ap>"				
    					+"<head>"
    					+"<tr_code>交易码</tr_code>"
    					+"<cms_corp_no>企业代码</cms_corp_no>"
    					+"<user_no>操作员号</user_no>"
    					+"<org_code>机构号</org_code>"
    					+"<serial_no>交易序号</serial_no>"
    					+"<req_no>发起方序号</req_no>"
    					+"<tr_acdt>交易日期</tr_acdt>"
    					+"<tr_time>交易时间</tr_time>"
    					+"<succ_flag>1</succ_flag>"
    					+"<ret_info>返回信息</ret_info>"
    					+"<ret_code>9999</ret_code>"
    					+"<ext_info>返回扩展信息</ext_info>"
    					+"<file_flag>文件标识</file_flag>"
    					+"<reserved>保留位</reserved>"
    					+"</head>"
    					+"</ap>"
    					;

    	return msg;
    }
}
