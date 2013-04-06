package parser.result;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Iterables.*;

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

    public int getConstructorInjectorCount() {
        return size(filter(injectorList, new Predicate<Injector>() {
            @Override
            @Nullable
            public boolean apply(Injector injector) {
                return injector.isConstructorInjector();
            }
        }));
    }

    public Iterable<Object> getConstructorInjectorValues() {
        return transform(filter(injectorList, new Predicate<Injector>() {
            @Override
            @Nullable
            public boolean apply(Injector injector) {
                return injector.isConstructorInjector();
            }
        }), new Function<Injector, Object>() {
            @Override
            @Nullable
            public Object apply(Injector injector) {
                return injector.getValue();
            }
        });
    }

    public Iterable<Injector> getSetterInjectors() {
        return filter(injectorList, new Predicate<Injector>() {
            @Override
            @Nullable
            public boolean apply(Injector injector) {
                return !injector.isConstructorInjector();
            }
        });
    }

    public Iterable<Injector> getReferenceInjectors() {
        return filter(injectorList, new Predicate<Injector>() {
            @Override
            @Nullable
            public boolean apply(Injector injector) {
                return injector.getIsReference();
            }
        });
    }
}
