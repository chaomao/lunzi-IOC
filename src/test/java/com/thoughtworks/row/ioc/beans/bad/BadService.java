package com.thoughtworks.row.ioc.beans.bad;

import com.thoughtworks.row.ioc.beans.good.Service;
import com.thoughtworks.row.ioc.beans.good.ServiceConsumer;

public class BadService implements Service {
    private ServiceConsumer consumer;

    public BadService(ServiceConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public String service() {
        return "you will never see this";
    }
}
