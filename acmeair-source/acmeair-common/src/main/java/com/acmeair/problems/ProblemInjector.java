package com.acmeair.problems;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * Just calling the problem injection handler to inject the problem for APM to find out
 */

public class ProblemInjector implements ProblemInterceptor {
    public static final String ISSUE_HEADER = "issue-header";

    private List<ProblemInterceptor> interceptorChain = new ArrayList<ProblemInterceptor>();

    public ProblemInjector() {
        // TODO we need find a way to find out the problem list, now we just use the hard coding
        interceptorChain.add(new CPUProblem());
        interceptorChain.add(new MemoryLeakProblem());
        interceptorChain.add(new SlowTransactionProblem());
    }

    public ProblemInjector(List<ProblemInterceptor> chain) {
        this.interceptorChain = chain;
    }

    public void preHandleRequest(final HttpServletRequest request) {
        for (ProblemInterceptor interceptor : interceptorChain) {
            interceptor.preHandleRequest(request);
        }
    }

    public void postHandleRequest(final HttpServletRequest request) {
        for(ProblemInterceptor interceptor : interceptorChain) {
            interceptor.postHandleRequest(request);
        }
    }

}
