package com.thoughtworks.row.ioc.predicate;

import com.google.common.base.Predicate;

import java.lang.reflect.Modifier;

public class ClassOnlyPredicate implements Predicate<Class> {
    @Override
    public boolean apply(Class input) {
        return isPublicClass(input) && isNonStaticInnerClass(input)
                && isClass(input);
    }

    private boolean isClass(Class klass) {
        return !klass.isInterface() && !klass.isAnnotation();
    }

    private boolean isPublicClass(Class klass) {
        return Modifier.isPublic(klass.getModifiers());
    }

    private boolean isNonStaticInnerClass(Class klass) {
        return klass.getEnclosingClass() == null && !Modifier.isStatic(klass.getModifiers());
    }
}
