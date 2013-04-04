import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.sun.istack.internal.Nullable;
import parser.result.Recipe;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.find;

public class ConstructorFinder {
    public static final HashMap<Class, Class> CLASS_HASH_MAP = new HashMap<Class, Class>();

    static {
        ConstructorFinder.CLASS_HASH_MAP.put(int.class, Integer.class);
        ConstructorFinder.CLASS_HASH_MAP.put(double.class, Double.class);
        ConstructorFinder.CLASS_HASH_MAP.put(float.class, Float.class);
        ConstructorFinder.CLASS_HASH_MAP.put(boolean.class, Boolean.class);
        ConstructorFinder.CLASS_HASH_MAP.put(char.class, Character.class);
    }

    public Constructor getSpecificConstructor(final Recipe recipe, Constructor<?>[] constructors) {
        ArrayList<Constructor<?>> constructorList = Lists.newArrayList(constructors);
        Iterable<Constructor<?>> filteredConstructor = filter(constructorList, new Predicate<Constructor<?>>() {
            @Override
            @Nullable
            public boolean apply(Constructor<?> constructor) {
                return recipe.getConstructorInjectorCount() == constructor.getParameterTypes().length;
            }
        });

        return find(filteredConstructor, new Predicate<Constructor>() {
            @Override
            @Nullable
            public boolean apply(Constructor constructor) {
                ArrayList<Class> parameterClasses = Lists.newArrayList(getWrapperClasses(constructor));
                Iterable<Object> injectValues = recipe.getConstructorInjectorValues();

                int index = 0;
                boolean result = true;
                for (Object value : injectValues) {
                    if (!parameterClasses.get(index++).isInstance(value)) {
                        result = false;
                        break;
                    }
                }
                return result;
            }
        });
    }

    private Iterable<Class> getWrapperClasses(Constructor constructor) {
        Class<?>[] types = constructor.getParameterTypes();

        return Iterables.transform(Arrays.asList(types), new Function<Class<?>, Class>() {
            @Override
            @Nullable
            public Class apply(Class<?> klass) {
                return CLASS_HASH_MAP.containsKey(klass) ? CLASS_HASH_MAP.get(klass) : klass;
            }
        });
    }
}
