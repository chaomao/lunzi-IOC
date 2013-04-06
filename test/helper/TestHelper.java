package helper;

import parser.result.*;

import java.util.Arrays;

public class TestHelper {

    public static Cookbook createCookbook(Recipe... recipes) {
        Cookbook cookbook = new Cookbook();
        for (Recipe r : recipes) {
            cookbook.add(r);
        }
        return cookbook;
    }

    public static Recipe createRecipe(String name, String klass) {
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setKlass(klass);
        return recipe;
    }

    public static Recipe createRecipe(String name, String klass, Injector... injectors) {
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setKlass(klass);
        recipe.setInjectorList(Arrays.asList(injectors));
        return recipe;
    }
}
