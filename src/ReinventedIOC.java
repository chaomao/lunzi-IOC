import parser.result.Recipe;

import java.util.HashMap;
import java.util.NoSuchElementException;

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

    private Object createObject(Recipe description) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class klass = Class.forName(description.getKlass());
        return klass.newInstance();
    }

    public void setCookbook(Cookbook cookbook) {
        this.cookbook = cookbook;
    }

    public int getObjectNumbers() {
        return objectMap.size();
    }
}
