import com.google.common.base.Predicate;
import exception.LoopDependencyException;
import parser.result.Cookbook;
import parser.result.Injector;
import parser.result.Recipe;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

import static com.google.common.collect.Iterables.find;

public class Kitchen {

    private final Chef chef = new Chef();
    private ArrayList<Cookbook> cookbooks = new ArrayList<Cookbook>();
    private HashMap<String, Object> objectMap = new HashMap<String, Object>();
    private Stack<String> objectInProcess = new Stack<String>();

    private Object lookUpInCookBook(String recipeName, Cookbook cookbook) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Recipe recipe = findRecipe(recipeName, cookbook);
        return returnNewObject(recipeName, recipe, cookbook);
    }

    public Object lookUp(final String recipeName) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (objectMap.containsKey(recipeName)) {
            return objectMap.get(recipeName);
        } else {
            Cookbook cookbook = find(cookbooks, new Predicate<Cookbook>() {
                @Override
                public boolean apply(Cookbook cookbook) {
                    return cookbook.findRecipe(recipeName).isPresent();
                }
            });
            return lookUpInCookBook(recipeName, cookbook);
        }
    }

    private Object returnNewObject(String recipeName, Recipe recipe, Cookbook cookbook) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Recipe consolidatedRecipe = consolidateReferenceInjector(recipe, cookbook);
        Object newObject = chef.cook(consolidatedRecipe);
        return pushNewObjectInContainer(recipeName, newObject);
    }

    private Object pushNewObjectInContainer(String name, Object o) {
        objectMap.put(name, o);
        objectInProcess.pop();
        return o;
    }

    private Recipe findRecipe(String recipeName, Cookbook cookbook) {
        if (objectInProcess.contains(recipeName)) {
            throw new LoopDependencyException("There is loop dependency happens for object " + recipeName);
        } else {
            objectInProcess.push(recipeName);
        }
        return cookbook.findRecipe(recipeName);
    }

    private Recipe consolidateReferenceInjector(Recipe recipe, Cookbook cookbook) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Iterable<Injector> referenceInjectors = recipe.getReferenceInjectors();
        for (Injector injector : referenceInjectors) {
            Object o = getReferenceInjectorValue(injector, cookbook);
            injector.setValue(o);
        }
        return recipe;
    }

    private Object getReferenceInjectorValue(Injector injector, Cookbook cookbook) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        String recipeName = (String) injector.getValue();
        Recipe referenceRecipe = cookbook.findRecipe(recipeName);
        String referenceRecipeName = referenceRecipe.getName();
        if (objectMap.containsKey(referenceRecipeName)) {
            return objectMap.get(referenceRecipeName);
        } else {
            return lookUpInCookBook(referenceRecipeName, cookbook);
        }
    }

    public void addCookbook(Cookbook... cookbooks) {
        this.cookbooks.addAll(Arrays.asList(cookbooks));
    }

    public int getObjectCount() {
        return objectMap.size();
    }
}
