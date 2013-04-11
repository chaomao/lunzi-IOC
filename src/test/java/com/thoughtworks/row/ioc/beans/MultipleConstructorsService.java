package com.thoughtworks.row.ioc.beans;

public class MultipleConstructorsService implements Service {
    private Service service;

    public MultipleConstructorsService() {
    }

    public MultipleConstructorsService(Service service) {
    }

    @Override
    public String service() {
        return null;
    }
}
