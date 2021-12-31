import java.io.*;

import sun.net.www.protocol.jar.*;

import javax.swing.*;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String x = new String("ok");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("/tmp/ok.ser"));
        oos.writeObject(x);
        oos.flush();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/tmp/ok.ser"));
        ois.readObject();
    }
}
