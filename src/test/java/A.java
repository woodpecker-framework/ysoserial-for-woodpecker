import me.gv7.woodpecker.yso.payloads.FindClassByBomb;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class A implements Serializable {
    static final long serialVersionUID = 3514945074733160196L;
    private String name;

    public A(String name){
        this.name = name;
    }

    private void readObject(java.io.ObjectInputStream s){
        System.out.println("xxxwewjriwe");
    }

    public void doTest(){

    }

    public void doA() throws FileNotFoundException {


    }


    public static void main(String[] args) throws Exception{
        FileOutputStream fileOutputStream = new FileOutputStream("/tmp/X.ser");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(A[].class);

//        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/tmp/X.ser"));
//        ois.readObject();
    }
}
