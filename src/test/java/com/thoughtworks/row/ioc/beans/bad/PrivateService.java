package com.thoughtworks.row.ioc.beans.bad;

import com.thoughtworks.row.ioc.beans.good.Service;

public class PrivateService implements Service {
    public static Service getInstance() {
        return new PrivateService();
    }

    private PrivateService() {

    }
    @Override
    public String service() {
        return getClass().getCanonicalName();
    }
}
