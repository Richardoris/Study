package com.acmeair.web;

import com.acmeair.problems.ProblemInjectInvocationHandler;
import com.acmeair.problems.ProblemInjector;
import java.lang.reflect.Proxy;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * The Base Rest Service which could be used to inject the failures for APM to find out
 */
public abstract class RestServiceSupport {

    // Using this request object to inject the error per request
    @Context
    protected HttpServletRequest request;

    abstract protected <T> T getService(Class<T> serviceClass);

    protected ProblemInjector getProblemInjector() {
        return new ProblemInjector();
    }

    protected <T> T getWrappedService(Class<T> serviceClass) {
        T service = getService(serviceClass);
        ProblemInjectInvocationHandler handler = new ProblemInjectInvocationHandler<T>(service, request, getProblemInjector());
        T wrappedService = (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[] {serviceClass}, handler);
        return wrappedService;
    }

    // setup the handler with the resource


}
