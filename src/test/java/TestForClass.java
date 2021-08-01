import me.gv7.woodpecker.yso.payloads.CommonsBeanutils1;
import me.gv7.woodpecker.yso.payloads.URLDNS;
import org.apache.commons.collections.functors.ChainedTransformer;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.List;

public class TestForClass {
    public static void main(String[] args) throws Exception {

        ObjectStreamClass a = ObjectStreamClass.lookupAny(A.class);
        System.out.println(a.getSerialVersionUID());
//
//        List<Object> testList = new ArrayList<Object>();
//        URLDNS urldns = new URLDNS();
//        testList.add(A.class);
//        testList.add(urldns.getObject("http://wwww.baidu.com"));
//
//        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ok7.ser"));
//        oos.writeObject(testList);
//        oos.flush();

        //ObjectInputStream ois = new ObjectInputStream(new FileInputStream("ok.ser"));
        //ois.readObject();
    }


    public static Class getClazz(String clazzName){
        try {
            return Class.forName(clazzName);
        }catch (Exception e){
            try {
                return Thread.currentThread().getContextClassLoader().loadClass(clazzName);
            }catch (Exception ee){

            }
        }
        return null;
    }
}
