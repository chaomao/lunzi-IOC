import finder.ConstructorFinder;
import parser.result.Cookbook;
import parser.result.Injector;
import parser.result.Recipe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class ReinventedIOC {

    private final ConstructorFinder constructorFinder = new ConstructorFinder();
    private final Chef chef = new Chef();
    private Cookbook cookbook = new Cookbook();
    private HashMap<String, Object> objectMap = new HashMap<String, Object>();

    public Object lookUp(String name) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Recipe recipe = cookbook.findRecipe(name);
        if (objectMap.containsKey(name)) {
            return objectMap.get(name);
        } else {
            Recipe newRecipe = consolidateReferenceInjector(recipe);
            Object o = chef.cook(newRecipe);
            objectMap.put(name, o);
            return o;
        }
    }

    private Recipe consolidateReferenceInjector(Recipe recipe) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Iterable<Injector> referenceInjectors = recipe.getReferenceInjectors();
        for (Injector injector : referenceInjectors) {
            Object o = getReferenceInjectorValue(injector);
            injector.setValue(o);
        }
        return recipe;
    }

    private Object getReferenceInjectorValue(Injector injector) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String recipeName = (String) injector.getValue();
        Recipe referenceRecipe = cookbook.findRecipe(recipeName);
        String referenceRecipeName = referenceRecipe.getName();
        if (objectMap.containsKey(referenceRecipeName)) {
            return objectMap.get(referenceRecipeName);
        }
        else {
            return lookUp(referenceRecipeName);
        }
    }

    public void setCookbook(Cookbook cookbook) {
        this.cookbook = cookbook;
    }

    public int getObjectCount(){
        return objectMap.size();
    }
}
