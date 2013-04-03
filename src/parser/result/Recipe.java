package parser.result;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String name;
    private String klass;
    private List<Injector> injectorList = new ArrayList<Injector>();

    public List<Injector> getInjectorList() {
        return injectorList;
    }

    public void setInjectorList(List<Injector> injectorList) {
        this.injectorList = injectorList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKlass() {
        return klass;
    }

    public void setKlass(String klass) {
        this.klass = klass;
    }

    public int getInjectorNumber() {
        return injectorList.size();
    }

    public Iterable<Object> getInjectValues() {
        return Iterables.transform(injectorList, new Function<Injector, Object>() {
            @Override
            @Nullable
            public Object apply(Injector injector) {
                return injector.getValue();
            }
        });
    }
}
