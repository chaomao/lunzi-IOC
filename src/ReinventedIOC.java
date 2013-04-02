import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.sun.istack.internal.Nullable;
import parser.result.Injector;
import parser.result.Recipe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Iterables.transform;

public class ReinventedIOC {
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
        Constructor constructor = find(Lists.newArrayList(constructors), new Predicate<Constructor>() {
            @Override
            @Nullable
            public boolean apply(Constructor o) {
                int length = o.getParameterTypes().length;
                return length == recipe.getInjectorNumber();
            }
        });

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

    public void setCookbook(Cookbook cookbook) {
        this.cookbook = cookbook;
    }

    public int getObjectNumbers() {
        return objectMap.size();
    }
}
