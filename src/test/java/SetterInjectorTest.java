import org.junit.Before;
import org.junit.Test;
import parser.InjectorBuilder;
import parser.result.Cookbook;
import parser.result.Injector;
import product.Bank;

import java.lang.reflect.InvocationTargetException;

import static helper.TestHelper.createCookbook;
import static helper.TestHelper.createRecipe;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class SetterInjectorTest {
    private ReinventedIOC ioc;

    @Before
    public void setUp() throws Exception {
        ioc = new ReinventedIOC();
    }

    @Test
    public void should_create_object_with_one_setter() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Injector injector = new InjectorBuilder().
                setterInject().
                name("amount").
                value(123).
                primitiveValue().
                build();
        Cookbook cookbook = createCookbook(createRecipe("bank", "product.Bank", injector));
        ioc.setCookbook(cookbook);

        Bank bank = (Bank) ioc.lookUp("bank");

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
        Cookbook cookbook = createCookbook(createRecipe("bank", "product.Bank", setterInjector, constructorInjector));
        ioc.setCookbook(cookbook);

        Bank bank = (Bank) ioc.lookUp("bank");

        assertThat(bank.getName(), is("Bank of Beijing"));
        assertThat(bank.getAmount(), is(123));
    }
}
