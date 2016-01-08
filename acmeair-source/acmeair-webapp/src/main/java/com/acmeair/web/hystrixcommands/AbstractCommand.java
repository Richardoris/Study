package com.acmeair.web.hystrixcommands;

import com.netflix.hystrix.HystrixCommand;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Support to config the Command executor with or without thread pool
 */
public abstract class AbstractCommand<R> extends HystrixCommand<R> {
    private static Logger log = LoggerFactory.getLogger(AbstractCommand.class);
    boolean asyncInvocation;

    protected AbstractCommand(Setter setter) {
        super(setter);
        checkProperties();
    }

    protected void checkProperties() {
        Properties properties = new Properties();
        try {
            InputStream source = this.getClass().getResourceAsStream("/config.properties");
            if (source != null) {
                properties.load(source);
            }
        } catch (IOException  ex) {
            // Do nothing here, we just use the fallback default value
        }
        String value = properties.getProperty(CommandConstants.ACME_AIR_ASYNC_COMMAND, "true");
        asyncInvocation = Boolean.parseBoolean(value);
        log.info("Set the asyncInvocation to be {}", asyncInvocation );
    }

    @Override
    public R execute() {
        if (asyncInvocation) {
            return super.execute();
        } else {
            try {
                return run();
            } catch (Exception e) {
                throw decomposeException(e);
            }
        }
    }
}
