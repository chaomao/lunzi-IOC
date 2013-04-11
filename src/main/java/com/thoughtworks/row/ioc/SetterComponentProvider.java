package com.thoughtworks.row.ioc;

import com.thoughtworks.row.ioc.exception.CyclicDependencyException;
import com.thoughtworks.row.ioc.exception.MultipleSetterException;
import com.thoughtworks.row.ioc.exception.SetComponentException;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Stack;

class SetterComponentProvider implements ComponentProvider {
    public static final int MAX_SETTER_LIMITATION = 3;
    private Container container;
    private Class componentClass;

    SetterComponentProvider(Container container, Class componentClass) {
        this.container = container;
        this.componentClass = componentClass;
    }

    @Override
    public Object getInstance(final Stack<Class> buildPath) {
        if (buildPath.contains(componentClass)) {
            throw new CyclicDependencyException();
        }
        buildPath.push(componentClass);
        Object target = setTargetObject(componentClass.getConstructors()[0], buildPath);
        buildPath.pop();
        return target;
    }

    private Object setTargetObject(Constructor constructor, Stack<Class> buildPath) {
        try {
            Object target = constructor.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(componentClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            if (propertyDescriptors.length > MAX_SETTER_LIMITATION) {
                throw new MultipleSetterException();
            }
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                Method setter = descriptor.getWriteMethod();
                if (setter != null) {
                    setter.invoke(target, container.createInstance(descriptor.getPropertyType(), buildPath));
                }
            }
            return target;
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | IntrospectionException e) {
            throw new SetComponentException();
        }
    }
}
