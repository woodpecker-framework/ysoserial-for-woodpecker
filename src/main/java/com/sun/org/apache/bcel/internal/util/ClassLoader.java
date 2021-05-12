package com.sun.org.apache.bcel.internal.util;

/**
 * 解决在OpenJDK下没有com.sun.org.apache.bcel.internal.util.ClassLoader，导致javassist编译bcel相关的class报错问题。
 */
public class ClassLoader extends java.lang.ClassLoader {
    public ClassLoader(){}
}
