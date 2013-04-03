import org.junit.Before;
import org.junit.Test;
import parser.result.Cookbook;
import parser.result.InjectType;
import parser.result.Injector;
import parser.result.Recipe;
import product.Bank;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReinventedIOCTest {

    private ReinventedIOC ioc;

    @Before
    public void setUp() throws Exception {
        ioc = new ReinventedIOC();
    }

    @Test
    public void should_create_object_given_name_class_only() {
        Cookbook cookbook = createCookbook(createRecipe("bank", "product.Bank"));
        ioc.setCookbook(cookbook);

        Bank bank = (Bank) ioc.lookUp("bank");

        assertThat(bank.verify(), is("success"));
    }

    @Test
    public void should_not_create_object_twice_when_object_created() {
        Cookbook cookbook = createCookbook(createRecipe("bank", "product.Bank"));
        ioc.setCookbook(cookbook);

        ioc.lookUp("bank");
        ioc.lookUp("bank");

        assertThat(ioc.getObjectNumbers(), is(1));
    }

    @Test
    public void should_create_object_with_constructor_injector() {
        Injector injector = getInjector(InjectType.Constructor, "id", 1001, false);
        Cookbook cookbook = createCookbook(createRecipe("bank", "product.Bank", injector));

        ioc.setCookbook(cookbook);

        Bank bank = (Bank) ioc.lookUp("bank");

        assertThat(bank.getId(), is(1001));
    }

    @Test
    public void should_create_object_with_constructor_determined_by_param_type() {
        Injector injector = getInjector(InjectType.Constructor, "name", "Bank of Beijing", false);
        Cookbook cookbook = createCookbook(createRecipe("bank", "product.Bank", injector));

        ioc.setCookbook(cookbook);

        Bank bank = (Bank) ioc.lookUp("bank");

        assertThat(bank.getName(), is("Bank of Beijing"));
    }

    private Injector getInjector(InjectType type, String name, Object value, boolean isReference) {
        Injector injector = new Injector();
        injector.setInjectType(type);
        injector.setName(name);
        injector.setValue(value);
        injector.setIsReference(isReference);
        return injector;
    }

    private Cookbook createCookbook(Recipe... recipes) {
        Cookbook cookbook = new Cookbook();
        for (Recipe r : recipes) {
            cookbook.add(r);
        }
        return cookbook;
    }

    private Recipe createRecipe(String name, String klass) {
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setKlass(klass);
        return recipe;
    }

    private Recipe createRecipe(String name, String klass, Injector... recipeList) {
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setKlass(klass);
        recipe.setInjectorList(Arrays.asList(recipeList));
        return recipe;
    }
}
