package com.thoughtworks.row.ioc;

import com.google.common.base.Function;
import com.thoughtworks.row.ioc.exception.CreateComponentException;
import com.thoughtworks.row.ioc.exception.CyclicDependencyException;
import com.thoughtworks.row.ioc.exception.MultipleParametersException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Stack;

import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Iterables.transform;

class ConstructorComponentProvider implements ComponentProvider {
    private Container container;
    private Class componentClass;

    ConstructorComponentProvider(Container container, Class componentClass) {
        this.container = container;
        this.componentClass = componentClass;
    }

    @Override
    public Object getInstance(final Stack<Class> buildPath) {
        if (buildPath.contains(componentClass)) {
            throw new CyclicDependencyException();
        }
        buildPath.push(componentClass);
        Object target = getObject(buildPath);
        buildPath.pop();
        return target;
    }

    private Object getObject(final Stack<Class> buildPath) {
        try {
            Constructor constructor = componentClass.getConstructors()[0];
            return constructor.newInstance(toArray(getParameters(buildPath, constructor), Object.class));
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new CreateComponentException();
        }
    }

    private Iterable<Object> getParameters(final Stack<Class> buildPath, Constructor constructor) {
        return transform(Arrays.asList(getParameterTypes(constructor)), new Function<Class, Object>() {
            @Override
            public Object apply(Class type) {
                return container.createInstance(type, buildPath);
            }
        });
    }

    private Class[] getParameterTypes(Constructor constructor) {
        Class[] parameterTypes = constructor.getParameterTypes();
        if (parameterTypes.length > 3) {
            throw new MultipleParametersException();
        }
        return parameterTypes;
    }
}
