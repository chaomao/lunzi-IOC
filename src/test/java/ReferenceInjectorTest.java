import exception.LoopDependencyException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import parser.result.Cookbook;
import parser.result.Injector;
import parser.simple.InjectorBuilder;
import product.Bank;
import product.User;

import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static parser.result.Cookbook.createByRecipes;
import static parser.simple.SimpleParser.createRecipe;

public class ReferenceInjectorTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    private Kitchen kitchen;

    @Before
    public void setUp() throws Exception {
        kitchen = new Kitchen();
    }

    @Test
    public void should_lookup_reference_object_first_then_lookup_injected_object() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Injector injector = new InjectorBuilder().
                setterInject().
                name("amount").
                value(123).
                primitiveValue().
                build();
        Injector referenceInjector = new InjectorBuilder().
                constructorInject().
                name("bank").
                value("beijingBank").
                referenceValue().
                build();
        Cookbook cookbook = createByRecipes(
                createRecipe("beijingBank", "product.Bank", injector),
                createRecipe("user", "product.User", referenceInjector));
        kitchen.setCookbook(cookbook);

        Bank bank = (Bank) kitchen.lookUp("beijingBank");
        assertThat(kitchen.getObjectCount(), is(1));

        User user = (User) kitchen.lookUp("user");
        assertThat(user.getBank(), is(bank));
    }

    @Test
    public void should_lookup_injected_object_first_then_lookup_reference_object() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Injector injector = new InjectorBuilder().
                setterInject().
                name("amount").
                value(123).
                primitiveValue().
                build();
        Injector referenceInjector = new InjectorBuilder().
                constructorInject().
                name("bank").
                value("beijingBank").
                referenceValue().
                build();
        Cookbook cookbook = createByRecipes(
                createRecipe("beijingBank", "product.Bank", injector),
                createRecipe("user", "product.User", referenceInjector));
        kitchen.setCookbook(cookbook);

        User user = (User) kitchen.lookUp("user");
        assertThat(kitchen.getObjectCount(), is(2));

        Bank bank = (Bank) kitchen.lookUp("beijingBank");
        assertThat(user.getBank(), is(bank));
    }

    @Test
    public void should_lookup_injected_object_first_then_lookup_reference_object_given_setter() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Injector injector = new InjectorBuilder().
                setterInject().
                name("amount").
                value(123).
                primitiveValue().
                build();
        Injector referenceInjector = new InjectorBuilder().
                setterInject().
                name("bank").
                value("beijingBank").
                referenceValue().
                build();
        Cookbook cookbook = createByRecipes(
                createRecipe("beijingBank", "product.Bank", injector),
                createRecipe("user", "product.User", referenceInjector));
        kitchen.setCookbook(cookbook);

        User user = (User) kitchen.lookUp("user");
        assertThat(kitchen.getObjectCount(), is(2));

        Bank bank = (Bank) kitchen.lookUp("beijingBank");
        assertThat(user.getBank(), is(bank));
    }

    @Test
    public void should_throw_exception_when_loop_dependency_happen() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        expectedEx.expect(LoopDependencyException.class);

        Injector injectorOne = new InjectorBuilder().
                constructorInject().
                name("objectOne").
                value("one").
                referenceValue().
                build();
        Injector injectorTwo = new InjectorBuilder().
                constructorInject().
                name("objectTwo").
                value("two").
                referenceValue().
                build();
        Cookbook cookbook = createByRecipes(
                createRecipe("one", "helper.ObjectOne", injectorTwo),
                createRecipe("two", "helper.ObjectTwo", injectorOne));
        kitchen.setCookbook(cookbook);

        kitchen.lookUp("one");
    }
}
