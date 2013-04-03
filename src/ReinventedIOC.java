import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.sun.istack.internal.Nullable;
import parser.result.Cookbook;
import parser.result.Injector;
import parser.result.Recipe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.google.common.collect.Iterables.*;

public class ReinventedIOC {
    public static final HashMap<Class, Class> CLASS_HASH_MAP = new HashMap<Class, Class>();

    static {
        CLASS_HASH_MAP.put(int.class, Integer.class);
        CLASS_HASH_MAP.put(double.class, Double.class);
        CLASS_HASH_MAP.put(float.class, Float.class);
        CLASS_HASH_MAP.put(boolean.class, Boolean.class);
        CLASS_HASH_MAP.put(char.class, Character.class);
    }

    private Cookbook cookbook = new Cookbook();
    private HashMap<String, Object> objectMap = new HashMap<String, Object>();

    public Object lookUp(String name) {
        Recipe description = cookbook.findRecipe(name);
        try {
            if (objectMap.containsKey(name)) {
                return objectMap.get(name);
            } else {
                Object o = createObject(description);
                objectMap.put(name, o);
                return o;
            }
        } catch (NoSuchElementException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    private Object createObject(final Recipe recipe) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class klass = Class.forName(recipe.getKlass());

        Constructor<?>[] constructors = klass.getConstructors();
        Constructor constructor = getSpecificConstructor(recipe, constructors);

        List<Injector> injectorList = recipe.getInjectorList();
        Iterable<Object> p = transform(injectorList, new Function<Injector, Object>() {
            @Override
            @Nullable
            public Object apply(Injector injector) {
                return injector.getValue();
            }
        });

        try {
            return constructor.newInstance(Iterables.toArray(p, Object.class));
        } catch (InvocationTargetException e) {

        }
        return null;
    }

    private Constructor getSpecificConstructor(final Recipe recipe, Constructor<?>[] constructors) {

        ArrayList<Constructor<?>> constructorList = Lists.newArrayList(constructors);
        Iterable<Constructor<?>> filteredConstructor = filter(constructorList, new Predicate<Constructor<?>>() {
            @Override
            @Nullable
            public boolean apply(Constructor<?> constructor) {
                return recipe.getInjectorNumber() == constructor.getParameterTypes().length;
            }
        });

        return find(filteredConstructor, new Predicate<Constructor>() {
            @Override
            @Nullable
            public boolean apply(Constructor constructor) {
                ArrayList<Class> parameterClasses = Lists.newArrayList(getWrapperClasses(constructor));
                Iterable<Object> injectValues = recipe.getInjectValues();


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

    public void setCookbook(Cookbook cookbook) {
        this.cookbook = cookbook;
    }

    public int getObjectNumbers() {
        return objectMap.size();
    }
}
