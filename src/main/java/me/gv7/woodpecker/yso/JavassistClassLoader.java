package me.gv7.woodpecker.yso;

public class JavassistClassLoader extends ClassLoader {
    public JavassistClassLoader(){
        super(Thread.currentThread().getContextClassLoader());
    }
}
