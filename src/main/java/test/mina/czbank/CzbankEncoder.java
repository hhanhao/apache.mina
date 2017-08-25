package test.mina.czbank;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.rmi.MarshalException;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CzbankEncoder extends ProtocolEncoderAdapter{
	
	private static final Logger log = LoggerFactory.getLogger(CzbankEncoder.class);

	private final Charset charset = Charset.forName("GBK");
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
	    try{
	    	log.info("client eccode begin =========="+ message);
//	    	String text=(String)message ;  
//	    	String text="";
//	    	 log.info("------send message length----"+text.length());
	    	CharsetEncoder ce = charset.newEncoder();
//	         IoBuffer.allocate(100);//这个方法内部通过SimpleBufferAllocator 创建一个实例，参数指定初始化容量
//	         setAutoExpand(true);//这个方法设置IoBuffer 为自动扩展容量
	        String value = message.toString();
//	         String value = "";
	        IoBuffer buffer = IoBuffer.allocate(value.getBytes(charset).length).setAutoExpand(true);
//	        buffer.putInt(value.length());
//	        buffer.put(text);
	        buffer.putString(value, ce);
	        // 反转此缓冲区。首先对当前位置设置限制，然后将该位置设置为零。如果已定义了标记，则丢弃该标记。
	        // 在序列信道读取或 put 操作之后，调用此方法以做好序列信道写入或相对 get 操作
	        buffer.flip();
	        out.write(buffer);
	    }catch(Exception e){
			log.error("异常:"+e.getMessage());
		}
	    
		// TODO Auto-generated method stub
		
	}

}
