package parser.result;

public class Injector {
    private String name;
    private InjectType injectType;
    private Object value;
    private ReferenceType referenceType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InjectType getInjectType() {
        return injectType;
    }

    public void setInjectType(InjectType injectType) {
        this.injectType = injectType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean getIsReference() {
        return referenceType.equals(ReferenceType.Reference);
    }

    public void setIsReference(ReferenceType type) {
        this.referenceType = type;
    }

    public boolean isConstructorInjector() {
        return injectType.equals(InjectType.Constructor);
    }
}

