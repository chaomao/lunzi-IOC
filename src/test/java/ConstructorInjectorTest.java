import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import parser.result.Cookbook;
import parser.result.Injector;
import parser.simple.InjectorBuilder;
import product.Bank;

import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static parser.result.Cookbook.createByRecipes;
import static parser.simple.SimpleParser.createRecipe;

public class ConstructorInjectorTest {

    private Kitchen kitchen;

    @Before
    public void setUp() throws Exception {
        kitchen = new Kitchen();
    }

    @Test
    public void should_create_object_given_name_class_only() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Cookbook cookbook = createByRecipes(createRecipe("bank", "product.Bank"));
        kitchen.addCookbook(cookbook);

        Bank bank = (Bank) kitchen.lookUp("bank");

        assertThat(bank.verify(), is("success"));
    }

    @Test
    public void should_not_create_object_twice_when_object_created() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Cookbook cookbook = createByRecipes(createRecipe("bank", "product.Bank"));
        kitchen.addCookbook(cookbook);

        Object bank1 = kitchen.lookUp("bank");
        Object bank2 = kitchen.lookUp("bank");

        Assert.assertEquals(bank1, bank2);
    }

    @Test
    public void should_create_object_with_constructor_injector() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Injector injector = new InjectorBuilder().
                constructorInject().
                name("id").
                value(1001).
                primitiveValue().
                build();
        Cookbook cookbook = createByRecipes(createRecipe("bank", "product.Bank", injector));

        kitchen.addCookbook(cookbook);

        Bank bank = (Bank) kitchen.lookUp("bank");

        assertThat(bank.getId(), is(1001));
    }

    @Test
    public void should_create_object_with_constructor_determined_by_param_type() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Injector injector = new InjectorBuilder().
                constructorInject().
                name("name").
                value("Bank of Beijing").
                primitiveValue().
                build();
        Cookbook cookbook = createByRecipes(createRecipe("bank", "product.Bank", injector));
        kitchen.addCookbook(cookbook);

        Bank bank = (Bank) kitchen.lookUp("bank");

        assertThat(bank.getName(), is("Bank of Beijing"));
    }
}
