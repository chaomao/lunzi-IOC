import org.junit.Before;
import org.junit.Test;
import parser.result.Cookbook;
import parser.result.Injector;
import parser.simple.InjectorBuilder;
import product.User;

import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static parser.result.Cookbook.createByRecipes;
import static parser.simple.SimpleParser.createRecipe;

public class ParentContainerTest {

    private Kitchen kitchen;

    @Before
    public void setUp() throws Exception {
        kitchen = new Kitchen();
    }

    @Test
    public void should_use_object_in_parent_container_when_create_object() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Injector bankInjector = new InjectorBuilder().
                constructorInject().
                name("city").
                value("city").
                referenceValue().
                build();
        Injector userInjector = new InjectorBuilder().
                constructorInject().
                name("bank").
                value("beijingBank").
                referenceValue().
                build();
        Cookbook grandfather = createByRecipes(createRecipe("city", "product.City"));
        Cookbook father = createByRecipes(createRecipe("beijingBank", "product.Bank", bankInjector));
        Cookbook son = createByRecipes(createRecipe("user", "product.User", userInjector));

        father.addParent(grandfather);
        son.addParent(father);

        kitchen.addCookbook(grandfather, father, son);

        User user = (User) kitchen.lookUp("user");

        assertThat(user, notNullValue());
    }
}
