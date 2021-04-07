package me.gv7.woodpecker.yso.test.payloads;

import me.gv7.woodpecker.yso.test.util.Files;
import org.junit.Assert;
import me.gv7.woodpecker.yso.test.CustomTest;
import me.gv7.woodpecker.yso.test.util.OS;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.Callable;

public class CommandExecTest implements CustomTest {
    private final File testFile =
        new File(OS.getTmpDir(), "ysoserial-test-" + UUID.randomUUID().toString().replaceAll("-", ""));

    @Override
    public void run(Callable<Object> payload) throws Exception {
        Assert.assertFalse("test file should not exist", testFile.exists());
        try {
            payload.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Files.waitForFile(testFile, 5000);
        Assert.assertTrue("test file should exist", testFile.exists());
        testFile.deleteOnExit();
    }

    @Override
    public String getPayloadArgs() {
        switch (OS.get()) {
            case OSX:
            case LINUX: return "touch " + testFile;
            case WINDOWS: return "powershell -command new-item -type file " + testFile;
            default: throw new UnsupportedOperationException("unsupported os");
        }
    }

}
