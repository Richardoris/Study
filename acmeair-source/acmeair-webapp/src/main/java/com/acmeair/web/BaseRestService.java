package com.acmeair.web;

public class BaseRestService extends RestServiceSupport {
    // Here we just use the ServiceLocator to look up the service object
    protected <T> T getService(Class<T> serviceClass) {
        return ServiceLocator.getService(serviceClass);
    }
}
