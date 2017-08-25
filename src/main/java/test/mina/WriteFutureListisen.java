package test.mina;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;

public class WriteFutureListisen implements IoFutureListener<IoFuture>{

	public void operationComplete(IoFuture future) {
		System.out.println("****************write is over*************************");
	}

}
