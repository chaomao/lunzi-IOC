package finder;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;

import static com.google.common.collect.Iterables.*;

public class AppropriateParameterPredicate implements Predicate<Constructor> {
    public static final HashMap<Class, Class> CLASS_HASH_MAP = new HashMap<Class, Class>();

    static {
        CLASS_HASH_MAP.put(int.class, Integer.class);
        CLASS_HASH_MAP.put(double.class, Double.class);
        CLASS_HASH_MAP.put(float.class, Float.class);
        CLASS_HASH_MAP.put(boolean.class, Boolean.class);
        CLASS_HASH_MAP.put(char.class, Character.class);
    }

    private Iterable<Object> constructorValues;

    public AppropriateParameterPredicate(Iterable<Object> constructorValues) {
        this.constructorValues = constructorValues;
    }

    @Override
    public boolean apply(final Constructor constructor) {
        Iterable<Class> parameterClasses = getWrapperParameterClasses(constructor.getParameterTypes());
        for (int i = 0; i < size(parameterClasses); i++) {
            if (!get(parameterClasses, i).isInstance(get(constructorValues, i))) {
                return false;
            }
        }
        return true;
    }

    private Iterable<Class> getWrapperParameterClasses(final Class[] parameterTypes) {
        return transform(Arrays.asList(parameterTypes), new Function<Class, Class>() {
            @Override
            public Class apply(Class klass) {
                return CLASS_HASH_MAP.containsKey(klass) ? CLASS_HASH_MAP.get(klass) : klass;
            }
        });
    }
}
