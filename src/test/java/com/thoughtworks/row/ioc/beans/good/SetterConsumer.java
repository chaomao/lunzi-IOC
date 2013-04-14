package com.thoughtworks.row.ioc.beans.good;

public class SetterConsumer implements ServiceConsumer {
    private Service service;

    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public String service() {
        return service.service();
    }

    public class MyInnerClass {

    }
}
