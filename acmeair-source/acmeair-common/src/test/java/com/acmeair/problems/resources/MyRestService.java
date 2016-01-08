package com.acmeair.problems.resources;

import com.acmeair.web.RestServiceSupport;
import com.acmeair.problems.MyProblemInterceptor;
import com.acmeair.problems.MyRest;
import com.acmeair.problems.ProblemInjector;
import com.acmeair.problems.ProblemInterceptor;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.mockito.Mockito;

/**
 * This class is used to test the base rest service
 */

@Path("/rest/test")
public class MyRestService extends RestServiceSupport {

    private MyProblemInterceptor myProblemInterceptor = new MyProblemInterceptor();

    @Override
    protected <T> T getService(Class<T> serviceClass) {
        T serviceInstance = Mockito.mock(serviceClass);
        // Just create a mock service for invocation
        if (serviceClass.isAssignableFrom(MyRest.class)) {
            MyRest service = (MyRest) serviceInstance;
            Mockito.when(service.echo(Mockito.anyString())).thenReturn("Hello World");
        }
        return serviceInstance;
    }

    @Override protected ProblemInjector getProblemInjector() {
        // just setup the custom problem interceptor to make sure the interceptor is called
        List<ProblemInterceptor> chain = new ArrayList<ProblemInterceptor>();
        chain.add(myProblemInterceptor);
        return new ProblemInjector(chain);

    }

    @GET @Path("/echo")
    public Response echo() {
        MyRest myRestService =  getWrappedService(MyRest.class);
        //Calling the service
        String response = myRestService.echo("request");
        // now the preHandlerRequest must be called
        if (myProblemInterceptor.isPreHandlerRequestCalled() && myProblemInterceptor.isPostHandleRequestCalled()) {
            return Response.ok().entity(response).build();
        } else {
            return Response.serverError().entity("The Problem Interceptor is not called rightly.").build();
        }
    }

}
