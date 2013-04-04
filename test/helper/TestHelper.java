package helper;

import parser.result.*;

import java.util.Arrays;

public class TestHelper {
    public static Injector getConstructorInjector(String name, Object value, ReferenceType referenceType) {
        return getInjector(InjectType.Constructor, name, value, referenceType);
    }

    public static Injector getSetterInjector(String name, Object value, ReferenceType referenceType) {
        return getInjector(InjectType.Setter, name, value, referenceType);
    }

    private static Injector getInjector(InjectType type, String name, Object value, ReferenceType referenceType) {
        Injector injector = new Injector();
        injector.setInjectType(type);
        injector.setName(name);
        injector.setValue(value);
        injector.setIsReference(referenceType);
        return injector;
    }

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
