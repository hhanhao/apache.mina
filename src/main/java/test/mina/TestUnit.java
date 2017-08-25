package test.mina;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class TestUnit {
	@Test
	public void test() throws UnsupportedEncodingException {
		String length = "00000991韩浩得到利随你的是什么";
		byte[] len = length.getBytes("UTF-8");
		System.out.println(len.length);
		String decode = new String(len, 0, 8, "UTF-8");
		System.out.println(decode);
	}
}
