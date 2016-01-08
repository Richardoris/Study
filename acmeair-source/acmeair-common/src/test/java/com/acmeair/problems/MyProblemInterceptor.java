package com.acmeair.problems;

import javax.servlet.http.HttpServletRequest;

public class MyProblemInterceptor extends ProblemInterceptorSupport implements ProblemInterceptor {
    public static final String MY_PROBLEM_HEADER = "my_problem";
    boolean preHandlerRequestCalled;
    boolean postHandleRequestCalled;

    @Override
    public void preHandleRequest(HttpServletRequest request) {
        if (isInjectProblem(request)) {
            preHandlerRequestCalled = true;
        }
    }

    @Override
    public void postHandleRequest(HttpServletRequest request) {
        if (isInjectProblem(request)) {
            postHandleRequestCalled = true;
        }
    }

    public boolean isPreHandlerRequestCalled() {
        return preHandlerRequestCalled;
    }

    public boolean isPostHandleRequestCalled() {
        return postHandleRequestCalled;
    }

    @Override
    protected String getProblemHeader() {
        return MY_PROBLEM_HEADER;
    }
}
