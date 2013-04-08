package parser.simple;

import parser.result.InjectType;
import parser.result.Injector;
import parser.result.ReferenceType;

public class InjectorBuilder {
    Injector injector = new Injector();

    public Injector build() {
        return injector;
    }

    public InjectorBuilder name(String name) {
        injector.setName(name);
        return this;
    }

    public InjectorBuilder constructorInject() {
        injector.setInjectType(InjectType.Constructor);
        return this;
    }

    public InjectorBuilder value(Object value) {
        injector.setValue(value);
        return this;
    }

    public InjectorBuilder primitiveValue() {
        injector.setIsReference(ReferenceType.Primitivity);
        return this;
    }

    public InjectorBuilder setterInject() {
        injector.setInjectType(InjectType.Setter);
        return this;
    }

    public InjectorBuilder referenceValue() {
        injector.setIsReference(ReferenceType.Reference);
        return this;
    }
}
