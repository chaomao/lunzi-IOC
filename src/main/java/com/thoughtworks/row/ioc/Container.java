package com.thoughtworks.row.ioc;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import com.thoughtworks.row.ioc.exception.ComponentNotFoundException;
import com.thoughtworks.row.ioc.exception.MultipleConstructorsException;
import com.thoughtworks.row.ioc.predicate.ClassOnlyPredicate;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.reflect.ClassPath.ClassInfo;
import static com.google.common.reflect.ClassPath.from;

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

    private void registerClassWithoutInterface(Class registerKey, Class klass) {
        providers.put(registerKey, getComponentProvider(klass));
    }

    private <T> ComponentProvider getComponentProvider(Class<? extends T> serviceClass) {
        Constructor<?>[] constructors = serviceClass.getDeclaredConstructors();
        if (isMultipleConstructor(constructors)) {
            throw new MultipleConstructorsException();
        }
        return hasUserDefinedConstructor(constructors[0]) ?
                new ConstructorComponentProvider(this, serviceClass) :
                new SetterComponentProvider(this, serviceClass);
    }

    private boolean hasUserDefinedConstructor(Constructor<?> constructor) {
        return constructor.getParameterTypes().length != 0;
    }

    private boolean isMultipleConstructor(Constructor<?>[] constructors) {
        return constructors.length > 1;
    }

    public ArrayList<Object> getComponentsByAnnotation(final Class<? extends Annotation> annotation) {
        Iterable<Object> transform = transform(providers.values(), new Function<ComponentProvider, Object>() {
            @Override
            public Object apply(ComponentProvider input) {
                return input.getInstance(new Stack<Class>());
            }
        });

        transform.toString();
        Iterable<Object> filter = filter(transform, new Predicate<Object>() {
            @Override
            public boolean apply(Object input) {
                return input.getClass().isAnnotationPresent(annotation);
            }
        });
        filter.toString();
        return Lists.newArrayList(filter);
    }

    public void registerComponentsInPackage(String packageName) {
        try {
            for (Class klass : getAllClasses(packageName)) {
                registerClassWithoutInterface(getRegisterKey(klass), klass);
            }
        } catch (IOException ignored) {
            throw new RuntimeException();
        }
    }

    public Iterable<Class> getAllClasses(String packageName) throws IOException {
        ClassPath classPath = from(ClassLoader.getSystemClassLoader());
        Iterable<Class> allClasses = transform(classPath.getTopLevelClassesRecursive(packageName),
                new Function<ClassInfo, Class>() {
                    @Override
                    public Class apply(ClassInfo input) {
                        return input.load();
                    }
                });
        return filter(allClasses, new ClassOnlyPredicate());
    }

    private Class getRegisterKey(Class klass) {
        return klass.getInterfaces().length != 0 ? klass.getInterfaces()[0] : klass;
    }
}
