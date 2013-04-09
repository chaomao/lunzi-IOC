import exception.LoopDependencyException;
import parser.result.Cookbook;
import parser.result.Injector;
import parser.result.Recipe;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Stack;

public class Kitchen {

    private final Chef chef = new Chef();
    private Cookbook cookbook = new Cookbook();
    private HashMap<String, Object> objectMap = new HashMap<String, Object>();
    private Stack<String> objectInProcess = new Stack<String>();

    public Object lookUp(String recipeName) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Recipe recipe = findRecipe(recipeName);
        if (objectMap.containsKey(recipeName)) {
            return returnObjectFromContainer(recipeName);
        } else {
            Recipe consolidatedRecipe = consolidateReferenceInjector(recipe);
            Object newObject = chef.cook(consolidatedRecipe);
            return pushNewObjectInContainer(recipeName, newObject);
        }
    }

    private Object pushNewObjectInContainer(String name, Object o) {
        objectMap.put(name, o);
        objectInProcess.pop();
        return o;
    }

    private Object returnObjectFromContainer(String name) {
        objectInProcess.pop();
        return objectMap.get(name);
    }

    private Recipe findRecipe(String recipeName) {
        if (objectInProcess.contains(recipeName)) {
            throw new LoopDependencyException();
        }
        objectInProcess.push(recipeName);
        return cookbook.findRecipe(recipeName);
    }

    private Recipe consolidateReferenceInjector(Recipe recipe) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Iterable<Injector> referenceInjectors = recipe.getReferenceInjectors();
        for (Injector injector : referenceInjectors) {
            Object o = getReferenceInjectorValue(injector);
            injector.setValue(o);
        }
        return recipe;
    }

    private Object getReferenceInjectorValue(Injector injector) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        String recipeName = (String) injector.getValue();
        Recipe referenceRecipe = cookbook.findRecipe(recipeName);
        String referenceRecipeName = referenceRecipe.getName();
        if (objectMap.containsKey(referenceRecipeName)) {
            return objectMap.get(referenceRecipeName);
        } else {
            return lookUp(referenceRecipeName);
        }
    }

    public void setCookbook(Cookbook cookbook) {
        this.cookbook = cookbook;
    }

    public int getObjectCount() {
        return objectMap.size();
    }
}
