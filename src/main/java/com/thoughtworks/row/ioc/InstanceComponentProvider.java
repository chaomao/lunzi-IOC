package com.thoughtworks.row.ioc;

import java.util.Stack;

class InstanceComponentProvider implements ComponentProvider {
    private Object instance;

    InstanceComponentProvider(Object instance) {
        this.instance = instance;
    }

    @Override
    public Object getInstance(Stack<Class> buildPath) {
        return instance;
    }
}
