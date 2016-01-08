package com.acmeair.problems;

import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.spi.container.TestContainerException;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import com.sun.jersey.test.framework.spi.container.grizzly2.web.GrizzlyWebTestContainerFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class BaseRestServiceTest extends JerseyTest {

    public BaseRestServiceTest()throws Exception {
        super("com.acmeair.problems.resources");
    }


    protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
        return new GrizzlyWebTestContainerFactory();
    }

    @Test
    // if the interceptor is called the response should be OK
    public void testHelloWorld() {
        WebResource webResource = resource();
        String responseMsg = null;

        try {
            responseMsg = webResource.path("/rest/test/echo").get(String.class);
            fail("expect an exception here!");
        } catch (UniformInterfaceException ex) {
            assertTrue("Expect the internal server error here!", ex.getMessage().indexOf("500 Internal Server Error") > 0);
        }

        // now we send the message with the header to enable the problem injection
        responseMsg = webResource.path("/rest/test/echo").header(ProblemInjector.ISSUE_HEADER, MyProblemInterceptor.MY_PROBLEM_HEADER).get(String.class);
        assertEquals("Hello World", responseMsg);

    }

}
