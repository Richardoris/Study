package com.acmeair.problems;

import javax.servlet.http.HttpServletRequest;


public interface ProblemInterceptor {

    void preHandleRequest(HttpServletRequest request);

    void postHandleRequest(HttpServletRequest request);
}
