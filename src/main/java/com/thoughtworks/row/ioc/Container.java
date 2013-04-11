package com.thoughtworks.row.ioc;

import com.thoughtworks.row.ioc.exception.ComponentNotFoundException;
import com.thoughtworks.row.ioc.exception.MultipleConstructorsException;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Stack;

public class Container {
    private HashMap<Class, ComponentProvider> providers = new HashMap<>();
    private Container parentContainer;

    public Container() {
    }

    public Container(Container parent) {
        parentContainer = parent;
    }

    public <T> void register(Class<T> serviceInterface, Class<? extends T> serviceClass) {
        register(serviceInterface, serviceClass, Lifecycle.Singleton);
    }

    public <T> void register(Class<T> serviceInterface, T serviceInstance) {
        providers.put(serviceInterface, new InstanceComponentProvider(serviceInstance));
    }

    public <T> T get(Class<T> serviceInterface) {
        return createInstance(serviceInterface, new Stack<Class>());
    }

    <T> T createInstance(Class<T> serviceInterface, final Stack<Class> buildPath) {
        ComponentProvider provider = getProvider(serviceInterface);
        if (provider == null) {
            throw new ComponentNotFoundException();
        }
        return (T) provider.getInstance(buildPath);
    }

    private <T> ComponentProvider getProvider(Class<T> serviceInterface) {
        ComponentProvider provider = providers.get(serviceInterface);
        if (provider == null && parentContainer != null) {
            provider = parentContainer.getProvider(serviceInterface);
        }
        return provider;
    }

    public <T> void register(Class<T> serviceInterface, Class<? extends T> serviceClass, Lifecycle lifecycle) {
        providers.put(serviceInterface, lifecycle.getProvider(getComponentProvider(serviceClass)));
    }

    private <T> ComponentProvider getComponentProvider(Class<? extends T> serviceClass) {
        Constructor<?>[] constructors = serviceClass.getDeclaredConstructors();
        if (isMultipleConstructor(constructors)) {
            throw new MultipleConstructorsException();
        }
        return constructors[0].getParameterTypes().length != 0 ?
                new ConstructorComponentProvider(this, serviceClass) :
                new SetterComponentProvider(this, serviceClass);
    }

    private boolean isMultipleConstructor(Constructor<?>[] constructors) {
        return constructors.length > 1;
    }
}
