package test.mina.czbank;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;

public class CodecFactory implements ProtocolCodecFactory {
    
    private final ProtocolEncoder encoder;
    private final ProtocolDecoder decoder;

    public CodecFactory(ProtocolEncoder encoder, ProtocolDecoder decoder) {
        this.decoder = decoder;
        this.encoder = encoder;
    }
    
    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
    	return decoder;
    }
    
    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
    	return encoder;
    }
public static void main(String[] args) {
	String pattern = "(\\d{1,3})(\\.\\d{1,2})?";
	System.out.println(java.util.regex.Pattern.matches(pattern, "123231"));
}
}
