
import javax.script.ScriptException;

public class Test {

    public static void main(String[] args) throws ScriptException {
        String a = "new java.lang.ProcessBuilder['(java.lang.String[])'](['/bin/sh','-c','open /System/Applications/Calculator.app/Contents/MacOS/Calculator']).start()";
        new javax.script.ScriptEngineManager().getEngineByName("js").eval(a);
    }
}
