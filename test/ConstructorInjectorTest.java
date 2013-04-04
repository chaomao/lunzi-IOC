import org.junit.Before;
import org.junit.Test;
import parser.result.Cookbook;
import parser.result.Injector;
import parser.result.ReferenceType;
import product.Bank;

import static helper.TestHelper.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ConstructorInjectorTest {

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
        Injector injector = getConstructorInjector("id", 1001, ReferenceType.Primitivity);
        Cookbook cookbook = createCookbook(createRecipe("bank", "product.Bank", injector));

        ioc.setCookbook(cookbook);

        Bank bank = (Bank) ioc.lookUp("bank");

        assertThat(bank.getId(), is(1001));
    }

    @Test
    public void should_create_object_with_constructor_determined_by_param_type() {
        Injector injector = getConstructorInjector("name", "Bank of Beijing", ReferenceType.Primitivity);
        Cookbook cookbook = createCookbook(createRecipe("bank", "product.Bank", injector));

        ioc.setCookbook(cookbook);

        Bank bank = (Bank) ioc.lookUp("bank");

        assertThat(bank.getName(), is("Bank of Beijing"));
    }
}
