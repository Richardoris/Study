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
package com.acmeair.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.acmeair.entities.CustomerSession;
import com.acmeair.web.hystrixcommands.ValidateTokenCommand;
//import com.acmeair.wxs.utils.*;

public class RESTCookieSessionFilter implements Filter {
	private static final Log log = LogFactory.getLog(RESTCookieSessionFilter.class);
	
	static final String LOGIN_USER = "acmeair.login_user";
	private static final String LOGIN_PATH = "/rest/api/login";
	private static final String LOGOUT_PATH = "/rest/api/login/logout";
	
//	private TransactionService transactionService = null; 
//	private boolean initializedTXService = false;
	
	@Override
	public void destroy() {
	}

//	private TransactionService getTxService()
//	{
//		if (!this.initializedTXService)
//		{
//			this.initializedTXService = true;
//			transactionService = ServiceLocator.getService(TransactionService.class);
//		}
//		
//		return transactionService;
//	}
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,	FilterChain chain) throws IOException, ServletException {
		try {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		
		String path = request.getContextPath() + request.getServletPath() + request.getPathInfo();
		// The following code is to ensure that OG is always set on the thread
//		try{
//			TransactionService txService = getTxService();
//			if (txService!=null)
//				txService.prepareForTransaction();
//		}catch( Exception e)
//		{
//			e.printStackTrace();
//		}
		// could do .startsWith for now, but plan to move LOGOUT to its own REST interface eventually
		if (path.endsWith(LOGIN_PATH) || path.endsWith(LOGOUT_PATH)) {
			// if logging in, let the request flow
			chain.doFilter(req, resp);
			return;
		}

		Cookie cookies[] = request.getCookies();
		Cookie sessionCookie = null;
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(LoginREST.SESSIONID_COOKIE_NAME)) {
					sessionCookie = c;
				}
				if (sessionCookie!=null)
					break; 
			}
			String sessionId = "";
			if (sessionCookie!=null) // We need both cookie to work
				sessionId= sessionCookie.getValue().trim();
			else {
				log.info("falling through with a sessionCookie break, but it was null");
			}
			// did this check as the logout currently sets the cookie value to "" instead of aging it out
			// see comment in LogingREST.java
			if (sessionId.equals("")) {
				log.info("sending SC_FORBIDDEN due to empty session cookie");
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
			// Need the URLDecoder so that I can get @ not %40
				log.info("Get the sessionId is " + sessionId);
				ValidateTokenCommand validateCommand = new ValidateTokenCommand(sessionId);
				CustomerSession cs = validateCommand.execute();
			if (cs != null) {
				request.setAttribute(LOGIN_USER, cs.getCustomerid());
				chain.doFilter(req, resp);
				return;
			}
			else {
				log.info("sending SC_FORBIDDEN due  to validateCommand returning null");
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
		}
		
		// if we got here, we didn't detect the session cookie, so we need to return 403
		log.info("sending SC_FORBIDDEN due finding no sessionCookie");
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
		catch (Exception e) {
			log.error("Got the exception here " , e);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}
}
