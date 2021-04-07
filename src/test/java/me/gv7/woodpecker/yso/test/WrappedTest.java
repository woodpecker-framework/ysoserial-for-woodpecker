package me.gv7.woodpecker.yso.test;

import java.util.concurrent.Callable;

/**
 * @author mbechler
 *
 */
public interface WrappedTest extends CustomPayloadArgs {

    Callable<Object> createCallable ( Callable<Object> innerCallable );

}
