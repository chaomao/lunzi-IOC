package com.thoughtworks.row.ioc;

import java.util.Stack;

public interface ComponentProvider {
    Object getInstance(Stack<Class> buildPath);
}
