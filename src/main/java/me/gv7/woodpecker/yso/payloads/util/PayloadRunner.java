package me.gv7.woodpecker.yso.payloads.util;

import java.io.File;
import java.util.concurrent.Callable;

import me.gv7.woodpecker.yso.Deserializer;
import me.gv7.woodpecker.yso.Serializer;
import static me.gv7.woodpecker.yso.Deserializer.deserialize;
import static me.gv7.woodpecker.yso.Serializer.serialize;
import me.gv7.woodpecker.yso.payloads.ObjectPayload;
import me.gv7.woodpecker.yso.payloads.ObjectPayload.Utils;
import me.gv7.woodpecker.yso.secmgr.ExecCheckingSecurityManager;

/*
 * utility class for running exploits locally from command line
 */
@SuppressWarnings("unused")
public class PayloadRunner {

    public static void run(final Class<? extends ObjectPayload<?>> clazz, final String[] args) throws Exception {
		// ensure payload generation doesn't throw an exception
		byte[] serialized = new ExecCheckingSecurityManager().callWrapped(new Callable<byte[]>(){
			public byte[] call() throws Exception {
				final String command = args.length > 0 && args[0] != null ? args[0] : getDefaultTestCmd();

				System.out.println("generating payload object(s) for command: '" + command + "'");

				ObjectPayload<?> payload = clazz.newInstance();
                final Object objBefore = payload.getObject(command);

				System.out.println("serializing payload");
				byte[] ser = Serializer.serialize(objBefore);
				Utils.releasePayload(payload, objBefore);
                return ser;
		}});

		try {
			System.out.println("deserializing payload");
			final Object objAfter = Deserializer.deserialize(serialized);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

    private static String getDefaultTestCmd() {
	    String cmdfile =  getFirstExistingFile(
	        "C:\\Windows\\System32\\calc.exe",
            "/System/Applications/Calculator.app/Contents/MacOS/Calculator",
            "/usr/bin/gnome-calculator",
            "/usr/bin/kcalc"
        );
	    return String.format("raw_cmd:%s",cmdfile);
    }

    private static String getFirstExistingFile(String ... files) {
        for (String path : files) {
            if (new File(path).exists()) {
                return path;
            }
        }
        throw new UnsupportedOperationException("no known test executable");
    }
}
