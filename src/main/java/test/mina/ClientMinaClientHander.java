package test.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import sun.util.logging.resources.logging;

public class ClientMinaClientHander extends IoHandlerAdapter {
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// session.write("我来啦........");
//		HimiObject ho = new HimiObject(1, "Himi");
//		session.write(ho);
	}

	// 当一个客户端关闭时
	@Override
	public void sessionClosed(IoSession session) {
		System.out.println("I'm Client &&  I closed!");
	}

	// 当服务器端发送的消息到达时:
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// // 我们己设定了服务器解析消息的规则是一行一行读取,这里就可转为 String:
		// String s = (String) message;
		// // Write the received data back to remote peer
		// System.out.println("服务器发来的收到消息: " + s);
		// // 测试将消息回送给客户端 session.write(s);

//		HimiObject ho = (HimiObject) message;
//		System.out.println(ho.getName());
//		System.out.println("clientminahandle msg:" + message);
	}

}
