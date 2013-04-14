package com.thoughtworks.row.ioc.beans.bad;

import com.thoughtworks.row.ioc.beans.good.Service;
import com.thoughtworks.row.ioc.beans.good.ServiceConsumer;

public class MultipleParametersServiceConsumer implements ServiceConsumer {

    public MultipleParametersServiceConsumer(Service service1, Service service2,
                                             Service service3, Service service4) {
    }

    @Override
    public String service() {
        return null;
    }
}
