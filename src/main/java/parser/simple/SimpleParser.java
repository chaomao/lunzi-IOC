package parser.simple;

import parser.IParser;
import parser.result.Injector;
import parser.result.Recipe;

import java.util.Arrays;

public class SimpleParser implements IParser {

    public static Recipe createRecipe(String name, String klass, Injector... injectors) {
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setKlass(klass);
        recipe.setInjectorList(Arrays.asList(injectors));
        return recipe;
    }

    @Override
    public Iterable<Recipe> getRecipes() {
        Injector bankInjector = new InjectorBuilder().
                setterInject().
                name("amount").
                value(123).
                primitiveValue().
                build();
        Injector userInjector = new InjectorBuilder().
                constructorInject().
                name("bank").
                value("beijingBank").
                referenceValue().
                build();

        Recipe beijingBank = createRecipe("beijingBank", "product.Bank", bankInjector);
        Recipe user = createRecipe("user", "product.User", userInjector);

        return Arrays.asList(user, beijingBank);
    }
}
