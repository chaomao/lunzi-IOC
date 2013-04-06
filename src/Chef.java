import com.google.common.collect.Iterables;
import com.sun.xml.internal.ws.util.StringUtils;
import finder.ConstructorFinder;
import parser.result.Injector;
import parser.result.Recipe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Chef {

    private final ConstructorFinder constructorFinder = new ConstructorFinder();

    public Object cook(Recipe recipe) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        Object object = createObject(recipe);
        return populateObject(recipe, object);
    }

    private Object createObject(final Recipe recipe) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class klass = Class.forName(recipe.getKlass());
        Constructor constructor = constructorFinder.getSpecificConstructor(recipe.getConstructorInjectorValues(), klass.getConstructors());
        Iterable<Object> injectedValues = recipe.getConstructorInjectorValues();
        return constructor.newInstance(Iterables.toArray(injectedValues, Object.class));
    }

    private Object populateObject(Recipe recipe, Object o) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Iterable<Injector> injectors = recipe.getSetterInjectors();
        for (Injector injector : injectors) {
            String setterName = "set" + StringUtils.capitalize(injector.getName());
            Method setter = o.getClass().getDeclaredMethod(setterName, injector.getValue().getClass());
            setter.invoke(o, injector.getValue());
        }

        return o;
    }
}
