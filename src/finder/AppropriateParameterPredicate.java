package finder;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.sun.istack.internal.Nullable;
import parser.result.Recipe;

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

    private Recipe recipe;

    public AppropriateParameterPredicate(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    @Nullable
    public boolean apply(Constructor constructor) {
        Class[] parameterClasses = toArray(getWrapperClasses(constructor), Class.class);
        Iterable<Object> injectValues = recipe.getConstructorInjectorValues();
        for (int i = 0; i < parameterClasses.length; i++) {
            if (!parameterClasses[i].isInstance(get(injectValues, i))) {
                return false;
            }
        }
        return true;
    }

    private Iterable<Class> getWrapperClasses(Constructor constructor) {
        Class<?>[] types = constructor.getParameterTypes();
        return transform(Arrays.asList(types), new Function<Class<?>, Class>() {
            @Override
            @Nullable
            public Class apply(Class<?> klass) {
                return CLASS_HASH_MAP.containsKey(klass) ? CLASS_HASH_MAP.get(klass) : klass;
            }
        });
    }
}
