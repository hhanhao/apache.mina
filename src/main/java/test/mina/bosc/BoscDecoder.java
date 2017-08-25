package test.mina.bosc;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BoscDecoder extends CumulativeProtocolDecoder {
	private static final Logger logger = LoggerFactory.getLogger(BoscDecoder.class);
	private final Charset charset = Charset.forName("UTF-8");
	private final AttributeKey CONTEXT = new AttributeKey(getClass(), "context");
    private final int HEADLENGTH=41;

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		logger.info("decoder.iosession:" + session);
		Context ctx = getContext(session);
		int matchCount = ctx.getMatchCount();
		IoBuffer buffer = ctx.innerBuffer;
		while (in.hasRemaining()) {
			byte b = in.get();
			matchCount++;
			buffer.put(b);
		}
		ctx.setMatchCount(matchCount);
		buffer.flip();
		byte[] len = new byte[HEADLENGTH];
		if (matchCount < HEADLENGTH) {
			return true;
		} else {
			buffer.get(len, 0, HEADLENGTH);
		}
		int length = 0;
		length = Integer.parseInt((new String(len, 4, 5, charset)).trim());// 取得数据区长度
		
		if (length + HEADLENGTH <= buffer.limit()) {// 判断报文是否完整读取
			byte[] message=buffer.array();
			String s1 = new String(message, charset);
			logger.info("getmsg:" + s1);
			byte[] message1=new byte[length];
			System.arraycopy(message, HEADLENGTH, message1, 0, length);
			String s = new String(message1, charset);
			out.write(s);
			ctx.reset();
			logger.info("getmsg:" + s);
			return false;
		} else {
			return true;
		}
	}
	
	private Context getContext(IoSession session) {
		Context context = (Context) session.getAttribute(CONTEXT);
		if (context == null) {
			context = new Context();
			session.setAttribute(CONTEXT, context);
		}
		return context;
	}

	private class Context {
		private final IoBuffer innerBuffer;

		public Context() {
			innerBuffer = IoBuffer.allocate(100).setAutoExpand(true);
		}

		private int matchCount = 0;

		public int getMatchCount() {
			return matchCount;
		}

		public void setMatchCount(int matchCount) {
			this.matchCount = matchCount;
		}

		public void reset() {
			this.innerBuffer.clear();
			this.matchCount = 0;
		}
	}
}
