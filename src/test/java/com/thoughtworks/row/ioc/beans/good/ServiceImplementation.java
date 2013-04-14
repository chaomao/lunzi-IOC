package com.thoughtworks.row.ioc.beans.good;

public class ServiceImplementation implements Service {

    @Override
    public String service() {
        return this.getClass().getCanonicalName();
    }
}
