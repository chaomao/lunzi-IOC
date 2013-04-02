package parser.result;

import java.util.List;

public class Recipe {
    private String name;
    private String klass;
    private List<Injector> injectorList;

    public void setName(String name) {
        this.name = name;
    }

    public void setKlass(String klass) {
        this.klass = klass;
    }

    public void setInjectorList(List<Injector> injectorList) {
        this.injectorList = injectorList;
    }

    public String getName() {
        return name;
    }

    public String getKlass() {
        return klass;
    }
}
