import org.junit.Before;
import org.junit.Test;
import parser.result.Recipe;
import product.Bank;

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
}
