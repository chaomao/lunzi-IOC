import parser.result.Cookbook;
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
            Object o = chef.cook(recipe);
            objectMap.put(name, o);
            return o;
        }
    }

    public void setCookbook(Cookbook cookbook) {
        this.cookbook = cookbook;
    }
}
