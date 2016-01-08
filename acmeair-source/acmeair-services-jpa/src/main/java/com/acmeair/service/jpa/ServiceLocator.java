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
package com.acmeair.service.jpa;

import java.util.concurrent.atomic.AtomicReference;

import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ServiceLocator {

	public static String REPOSITORY_LOOKUP_KEY = "com.acmeair.repository.type";
	final ApplicationContext ctx;
	private static Logger logger = LoggerFactory.getLogger(ServiceLocator.class);

	private static AtomicReference<ServiceLocator> singletonServiceLocator = new AtomicReference<ServiceLocator>();

	static ServiceLocator instance() {
		if (singletonServiceLocator.get() == null) {
			synchronized (singletonServiceLocator) {
				if (singletonServiceLocator.get() == null) {
					singletonServiceLocator.set(new ServiceLocator());
				}
			}
		}
		return singletonServiceLocator.get();
	}

	private ServiceLocator() {
		
		ctx = new AnnotationConfigApplicationContext(JPAContextConfig.class);
	}

	public static <T> T getService(Class<T> clazz) {
		return instance().ctx.getBean(clazz);
	}
}
