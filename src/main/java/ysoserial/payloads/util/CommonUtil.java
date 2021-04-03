package ysoserial.payloads.util;

import com.sun.org.apache.bcel.internal.classfile.Utility;

import java.io.*;

public class CommonUtil {
    public static byte[] getFileBytes(String file) {
        try {
            File f = new File(file);
            int length = (int) f.length();
            byte[] data = new byte[length];
            new FileInputStream(f).read(data);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String classToBCEL(byte[] clazzBytes) throws IOException {
        String strBCEL = "$$BCEL$$" + Utility.encode(clazzBytes, true);
        return strBCEL;
    }

    public static String classToBCEL(String classPath) throws IOException {
        byte[] clazzBytes = CommonUtil.getFileBytes(classPath);
        return classToBCEL(clazzBytes);
    }

    public static byte[] readFileByte(String filename) throws IOException {

        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

    public static String fileContextToByteArray(String filePath) throws IOException {
        byte[] fileContent = CommonUtil.readFileByte(filePath);
        StringBuffer sb = new StringBuffer();
        if(fileContent.length >0) {
            for (byte bContent : fileContent) {
                sb.append(bContent);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }


    public static void main(String[] args) throws IOException {
        System.out.println(fileContextToByteArray("/tmp/jndi.ser"));
    }
}
