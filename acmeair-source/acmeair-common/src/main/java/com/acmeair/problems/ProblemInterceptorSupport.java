package com.acmeair.problems;


import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ProblemInterceptorSupport {
    private static final Logger log = LoggerFactory.getLogger(ProblemInterceptorSupport.class);

    protected abstract String getProblemHeader();

    protected boolean isInjectProblem(HttpServletRequest request) {
        log.info("Get the request " + request);

        // just fix the NPE error
        if (request == null) {
            return false;
        }

        String issueHeader =  request.getHeader(ProblemInjector.ISSUE_HEADER);
        if (issueHeader != null) {
            return issueHeader.contains(getProblemHeader());
        } else {
            return false;
        }

    }

}
