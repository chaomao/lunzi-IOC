package parser;

import org.junit.Test;
import parser.result.InjectType;
import parser.result.Injector;
import parser.simple.InjectorBuilder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InjectorBuilderTest {

    @Test
    public void should_build_constructor_injector() {
        InjectorBuilder builder = new InjectorBuilder();

        Injector injector = builder.
                name("id").
                constructorInject().
                value(123).
                primitiveValue().
                build();

        assertThat(injector.getName(), is("id"));
        assertThat(injector.getInjectType(), is(InjectType.Constructor));
        assertThat((Integer) injector.getValue(), is(123));
        assertThat(injector.getIsReference(), is(false));
    }

    @Test
    public void should_build_setter_injector_with() {
        InjectorBuilder builder = new InjectorBuilder();

        Injector injector = builder.
                name("amount").
                setterInject().
                value("bank").
                referenceValue().
                build();

        assertThat(injector.getName(), is("amount"));
        assertThat(injector.getInjectType(), is(InjectType.Setter));
        assertThat((String) injector.getValue(), is("bank"));
        assertThat(injector.getIsReference(), is(true));
    }
}
