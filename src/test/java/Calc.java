import java.io.IOException;

public class Calc {
    public Calc() throws IOException {
        Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","open /System/Applications/Calculator.app/Contents/MacOS/Calculator"});
    }

    public Calc(String args) throws IOException {
        Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",args});
    }
}
