import com.google.common.collect.Iterables;
import com.sun.xml.internal.ws.util.StringUtils;
import parser.result.Cookbook;
import parser.result.Injector;
import parser.result.Recipe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ReinventedIOC {

    private final ConstructorFinder constructorFinder = new ConstructorFinder();
    private Cookbook cookbook = new Cookbook();
    private HashMap<String, Object> objectMap = new HashMap<String, Object>();

    public Object lookUp(String name) {
        Recipe recipe = cookbook.findRecipe(name);
        try {
            if (objectMap.containsKey(name)) {
                return objectMap.get(name);
            } else {
                Object o = createObject(recipe);
                o = populateObject(recipe, o);
                objectMap.put(name, o);
                return o;
            }
        } catch (NoSuchElementException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (NoSuchMethodException e) {
        } catch (InvocationTargetException e) {
        }
        return null;
    }

    private Object createObject(final Recipe recipe) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class klass = Class.forName(recipe.getKlass());

        Constructor constructor = constructorFinder.getSpecificConstructor(recipe, klass.getConstructors());

        Iterable<Object> injectedValues = recipe.getConstructorInjectorValues();

        try {
            return constructor.newInstance(Iterables.toArray(injectedValues, Object.class));
        } catch (InvocationTargetException e) {

        }
        return null;
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

    public void setCookbook(Cookbook cookbook) {
        this.cookbook = cookbook;
    }

    public int getObjectNumbers() {
        return objectMap.size();
    }
}
