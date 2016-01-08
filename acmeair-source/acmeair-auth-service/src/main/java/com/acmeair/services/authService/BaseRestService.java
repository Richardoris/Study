package com.acmeair.services.authService;

import com.acmeair.web.RestServiceSupport;

public class BaseRestService extends RestServiceSupport {
    // Here we just use the ServiceLocator to look up the service object
    protected <T> T getService(Class<T> serviceClass) {
        return ServiceLocator.getService(serviceClass);
    }
}
