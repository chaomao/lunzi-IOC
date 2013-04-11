package com.thoughtworks.row.ioc;

import com.google.common.base.Function;

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
        Constructor constructor = componentClass.getConstructors()[0];
        Iterable<Object> parameters = transform(Arrays.asList(getParameterTypes(constructor)), new Function<Class, Object>() {
            @Override
            public Object apply(Class type) {
                return container.createInstance(type, buildPath);
            }
        });
        try {
            return constructor.newInstance(toArray(parameters, Object.class));
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new CreateComponentException();
        } finally {
            buildPath.pop();
        }
    }

    private Class[] getParameterTypes(Constructor constructor) {
        Class[] parameterTypes = constructor.getParameterTypes();
        if (parameterTypes.length > 3) {
            throw new MultipleParametersException();
        }
        return parameterTypes;
    }
}
