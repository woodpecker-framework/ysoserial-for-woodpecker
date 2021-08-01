import java.io.*;

import sun.net.www.protocol.jar.*;

import javax.swing.*;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {


        JEditorPane jEditorPane = new JEditorPane();
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ok.ser"));
        oos.writeObject(jEditorPane);
        oos.flush();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("ok.ser"));
        ois.readObject();
    }
}
