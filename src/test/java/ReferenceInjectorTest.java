import exception.LoopDependencyException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import parser.InjectorBuilder;
import parser.result.Cookbook;
import parser.result.Injector;
import product.Bank;
import product.User;

import java.lang.reflect.InvocationTargetException;

import static helper.TestHelper.createCookbook;
import static helper.TestHelper.createRecipe;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReferenceInjectorTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    private ReinventedIOC ioc;

    @Before
    public void setUp() throws Exception {
        ioc = new ReinventedIOC();
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
        Cookbook cookbook = createCookbook(
                createRecipe("beijingBank", "product.Bank", injector),
                createRecipe("user", "product.User", referenceInjector));
        ioc.setCookbook(cookbook);

        Bank bank = (Bank) ioc.lookUp("beijingBank");
        assertThat(ioc.getObjectCount(), is(1));

        User user = (User) ioc.lookUp("user");
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
        Cookbook cookbook = createCookbook(
                createRecipe("beijingBank", "product.Bank", injector),
                createRecipe("user", "product.User", referenceInjector));
        ioc.setCookbook(cookbook);

        User user = (User) ioc.lookUp("user");
        assertThat(ioc.getObjectCount(), is(2));

        Bank bank = (Bank) ioc.lookUp("beijingBank");
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
        Cookbook cookbook = createCookbook(
                createRecipe("beijingBank", "product.Bank", injector),
                createRecipe("user", "product.User", referenceInjector));
        ioc.setCookbook(cookbook);

        User user = (User) ioc.lookUp("user");
        assertThat(ioc.getObjectCount(), is(2));

        Bank bank = (Bank) ioc.lookUp("beijingBank");
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
        Cookbook cookbook = createCookbook(
                createRecipe("one", "helper.ObjectOne", injectorTwo),
                createRecipe("two", "helper.ObjectTwo", injectorOne));
        ioc.setCookbook(cookbook);

        ioc.lookUp("one");
    }
}
