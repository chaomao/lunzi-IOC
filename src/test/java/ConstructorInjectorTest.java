import helper.TestHelper;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
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

public class ConstructorInjectorTest {

    private ReinventedIOC ioc;

    @Before
    public void setUp() throws Exception {
        ioc = new ReinventedIOC();
    }

    @Test
    public void should_create_object_given_name_class_only() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Cookbook cookbook = createCookbook(createRecipe("bank", "product.Bank"));
        ioc.setCookbook(cookbook);

        Bank bank = (Bank) ioc.lookUp("bank");

        assertThat(bank.verify(), is("success"));
    }

    @Test
    public void should_not_create_object_twice_when_object_created() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Cookbook cookbook = createCookbook(createRecipe("bank", "product.Bank"));
        ioc.setCookbook(cookbook);

        Object bank1 = ioc.lookUp("bank");
        Object bank2 = ioc.lookUp("bank");

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
        Cookbook cookbook = createCookbook(createRecipe("bank", "product.Bank", injector));

        ioc.setCookbook(cookbook);

        Bank bank = (Bank) ioc.lookUp("bank");

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
        Cookbook cookbook = createCookbook(createRecipe("bank", "product.Bank", injector));
        ioc.setCookbook(cookbook);

        Bank bank = (Bank) ioc.lookUp("bank");

        assertThat(bank.getName(), is("Bank of Beijing"));
    }
}
