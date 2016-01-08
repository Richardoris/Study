package com.acmeair.problems;

import java.util.Random;
import javax.servlet.http.HttpServletRequest;

public class SlowTransactionProblem extends ProblemInterceptorSupport implements ProblemInterceptor {
    public static final String SLOW_TRANSACTION_ISSUE_HEADER = "slow-transaction-issue";
    private Random random = new Random();

    @Override
    protected String getProblemHeader() {
        return SLOW_TRANSACTION_ISSUE_HEADER;
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void preHandleRequest(HttpServletRequest request) {
        if (isInjectProblem(request)) {
            int sleepTime = random.nextInt(4);
            sleep(sleepTime * 1000);
        }
    }

    @Override
    public void postHandleRequest(HttpServletRequest request) {
        if (isInjectProblem(request)) {
            int sleepTime = random.nextInt(5);
            sleep(sleepTime * 1000);
        }
    }
}
