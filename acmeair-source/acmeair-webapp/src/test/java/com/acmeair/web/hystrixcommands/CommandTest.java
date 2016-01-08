package com.acmeair.web.hystrixcommands;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class CommandTest {
    private class MyCommand extends AbstractCommand<String> {
        MyCommand() {
            super (Setter.
                    withGroupKey(HystrixCommandGroupKey.Factory.asKey(CommandConstants.COMMAND_GROUP_KEY)).
                    andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(CommandConstants.THREAD_POOL_KEY)).
                    andThreadPoolPropertiesDefaults(
                            HystrixThreadPoolProperties.Setter().
                                    withCoreSize(CommandConstants.THREAD_POOL_CORE_SIZE)));
        }

        @Override
        protected String run() throws Exception {
             return "test";
        }
    }

    @Test
    public void testAsynInvocation() {
        MyCommand myCommand = new MyCommand();
        assertFalse("We should pick the value from the properties file.", myCommand.asyncInvocation);
    }

}
