package com.thoughtworks.row.ioc;

import java.util.Stack;

class CacheComponentProvider implements ComponentProvider {
    private ComponentProvider provider;
    private Object instance;

    CacheComponentProvider(ComponentProvider provider) {
        this.provider = provider;
    }

    @Override
    public Object getInstance(Stack<Class> buildPath) {
        if (instance == null)
            instance = provider.getInstance(buildPath);
        return instance;
    }
}
