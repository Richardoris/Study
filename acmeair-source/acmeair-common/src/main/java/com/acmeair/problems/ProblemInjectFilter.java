/*******************************************************************************
* Copyright (c) 2013 IBM Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*******************************************************************************/
package com.acmeair.problems;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ProblemInjectFilter implements Filter {
	private static final Log log = LogFactory.getLog(ProblemInjectFilter.class);
    private ProblemInjector problemInjector = new ProblemInjector();

	@Override public void destroy() {
	}

	@Override public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest request = (HttpServletRequest) req;

			String path = request.getContextPath() + request.getServletPath() + request.getPathInfo();
			// just check the message according to the
            problemInjector.preHandleRequest(request);
			chain.doFilter(req, resp);
            problemInjector.postHandleRequest(request);

		} catch (Exception e) {
			log.error("Got the exception here " , e);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
        // TODO We can set up the filter here
	}
}
