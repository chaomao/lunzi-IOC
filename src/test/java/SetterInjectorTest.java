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

public class SetterInjectorTest {
    private Kitchen kitchen;

    @Before
    public void setUp() throws Exception {
        kitchen = new Kitchen();
    }

    @Test
    public void should_create_object_with_one_setter() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Injector injector = new InjectorBuilder().
                setterInject().
                name("amount").
                value(123).
                primitiveValue().
                build();
        Cookbook cookbook = createByRecipes(createRecipe("bank", "product.Bank", injector));
        kitchen.setCookbook(cookbook);

        Bank bank = (Bank) kitchen.lookUp("bank");

        assertThat(bank.getAmount(), is(123));
    }

    @Test
    public void should_create_object_with_one_constructor_and_one_setter() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Injector constructorInjector = new InjectorBuilder().
                constructorInject().
                name("name").
                value("Bank of Beijing").
                primitiveValue().
                build();

        Injector setterInjector = new InjectorBuilder().
                setterInject().
                name("amount").
                value(123).
                primitiveValue().
                build();
        Cookbook cookbook = createByRecipes(createRecipe("bank", "product.Bank", setterInjector, constructorInjector));
        kitchen.setCookbook(cookbook);

        Bank bank = (Bank) kitchen.lookUp("bank");

        assertThat(bank.getName(), is("Bank of Beijing"));
        assertThat(bank.getAmount(), is(123));
    }
}
