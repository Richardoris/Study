package com.acmeair.problems;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class MemoryLeakProblem extends ProblemInterceptorSupport implements ProblemInterceptor {
    public static final String MEMORY_ISSUE_HEADER = "memory-issue";

    private static List list = new ArrayList<HttpServletRequest>();

    @Override public void preHandleRequest(HttpServletRequest request) {
        if (isInjectProblem(request)) {
            list.add(request);
        }
    }

    @Override public void postHandleRequest(HttpServletRequest request) {

    }

    @Override protected String getProblemHeader() {
        return MEMORY_ISSUE_HEADER;
    }
}
