package com.thoughtworks.row.ioc.annotation;

import com.thoughtworks.row.ioc.beans.good.Service;
import com.thoughtworks.row.ioc.beans.good.ServiceConsumer;

@Controller
public class ComplexController implements ServiceConsumer {
    private Service service;

    public ComplexController(Service service) {
        this.service = service;
    }

    @Override
    public String service() {
        return service.service();
    }
}
