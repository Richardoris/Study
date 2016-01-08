package com.acmeair.problems;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;


public class ProblemInjectInvocationHandler<T> implements InvocationHandler {

    private final T delegateInstance;
    private final HttpServletRequest httpServletRequest;
    private final ProblemInjector problemInjector;


    public ProblemInjectInvocationHandler(T instance, HttpServletRequest request, ProblemInjector injector) {
        delegateInstance = instance;
        httpServletRequest = request;
        problemInjector = injector;
    }

    @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Inject the errors here
        problemInjector.preHandleRequest(httpServletRequest);
        try {
            return method.invoke(delegateInstance, args);
        } finally {
            problemInjector.postHandleRequest(httpServletRequest);
        }
    }
}
