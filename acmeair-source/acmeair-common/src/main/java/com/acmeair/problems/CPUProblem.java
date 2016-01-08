package com.acmeair.problems;

import javax.servlet.http.HttpServletRequest;

/**
 * Here we just use the for loop the eat up the CPU
 */
public class CPUProblem extends ProblemInterceptorSupport implements ProblemInterceptor {
    public static final String CPU_ISSUE_HEADER = "cpu-issue";

    protected String getProblemHeader() {
        return CPU_ISSUE_HEADER;
    }

    private void eatupCPU() {
        for(int i = 1 ; i < 1000000; i++) {
            Math.sqrt(i);
        }
    }

    @Override
    public void preHandleRequest(HttpServletRequest request) {
         if (isInjectProblem(request)) {
             eatupCPU();
         }
    }

    @Override
    public void postHandleRequest(HttpServletRequest request) {
        if (isInjectProblem(request)) {
            eatupCPU();
        }
    }
}
