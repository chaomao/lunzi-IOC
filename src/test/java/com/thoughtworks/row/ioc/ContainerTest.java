package com.thoughtworks.row.ioc;

import com.thoughtworks.row.ioc.annotation.ComplexController;
import com.thoughtworks.row.ioc.annotation.Controller;
import com.thoughtworks.row.ioc.annotation.SimpleController;
import com.thoughtworks.row.ioc.beans.bad.*;
import com.thoughtworks.row.ioc.beans.good.*;
import com.thoughtworks.row.ioc.exception.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ContainerTest {

    private Container container;

    @Before
    public void setUp() throws Exception {
        container = new Container();
    }

    @Test
    public void should_be_able_to_create_instance_with_zero_constructor() {
        container.register(Service.class, ServiceImplementation.class);

        Service service = (Service) container.get(Service.class);
        assertThat(service.service(), is(ServiceImplementation.class.getCanonicalName()));
    }

    @Test
    public void should_be_able_to_inject_service_to_constructor() {

        container.register(ServiceConsumer.class, ServiceConsumerImplementation.class);
        container.register(Service.class, ServiceImplementation.class);

        ServiceConsumer consumer = (ServiceConsumer) container.get(ServiceConsumer.class);
        assertThat(consumer.service(), is(ServiceImplementation.class.getCanonicalName()));
    }

    @Test(expected = CyclicDependencyException.class)
    public void should_throw_exception_if_cyclic_dependency() {

        container.register(ServiceConsumer.class, ServiceConsumerImplementation.class);
        container.register(Service.class, BadService.class);

        container.get(ServiceConsumer.class);
    }

    @Test
    public void should_inject_instance_to_constructor() {

        container.register(ServiceConsumer.class, ServiceConsumerImplementation.class);
        container.register(Service.class, PrivateService.getInstance());

        ServiceConsumer consumer = (ServiceConsumer) container.get(ServiceConsumer.class);
        assertThat(consumer.service(), is(PrivateService.class.getCanonicalName()));
    }

    @Test
    public void should_be_able_to_declare_service_lifecycle() {
        container.register(Service.class, ServiceImplementation.class, Lifecycle.Singleton);
        container.register(ServiceConsumer.class, TransientServiceConsumer.class, Lifecycle.Transient);

        TransientServiceConsumer first = (TransientServiceConsumer) container.get(ServiceConsumer.class);
        TransientServiceConsumer second = (TransientServiceConsumer) container.get(ServiceConsumer.class);

        assertThat(first, not(sameInstance(second)));
        assertThat(first.getService(), sameInstance(second.getService()));
    }

    @Test
    public void should_be_able_to_inject_service_via_setter() {
        container.register(Service.class, ServiceImplementation.class);
        container.register(ServiceConsumer.class, SetterConsumer.class);

        ServiceConsumer consumer = (ServiceConsumer) container.get(ServiceConsumer.class);
        assertThat(consumer.service(), is(ServiceImplementation.class.getCanonicalName()));
    }

    @Test
    public void should_find_service_from_parent_container() {
        Container grandfather = new Container();
        Container father = new Container(grandfather);
        Container son = new Container(father);

        grandfather.register(Service.class, ServiceImplementation.class);
        son.register(ServiceConsumer.class, SetterConsumer.class);

        ServiceConsumer consumer = (ServiceConsumer) son.get(ServiceConsumer.class);
        assertThat(consumer.service(), is(ServiceImplementation.class.getCanonicalName()));
    }

    @Test(expected = ComponentNotFoundException.class)
    public void should_throw_component_not_found_exception() {
        container.register(ServiceConsumer.class, SetterConsumer.class);
        container.get(Service.class);
    }

    @Test(expected = MultipleConstructorsException.class)
    public void should_throw_multiple_constructors_exception_if_class_has_more_than_one_constructor() {
        container.register(Service.class, MultipleConstructorsService.class);
        container.get(Service.class);
    }

    @Test(expected = MultipleSetterException.class)
    public void should_throw_multiple_setters_exception_if_class_has_more_than_three_setters() {
        container.register(Service.class, ServiceImplementation.class);
        container.register(ServiceConsumer.class, MultipleSetterServiceComsumer.class);
        container.get(ServiceConsumer.class);
    }

    @Test(expected = MultipleParametersException.class)
    public void should_throw_multiple_parameters_exception_if_constructor_has_more_than_three_parameters() {
        container.register(Service.class, ServiceImplementation.class);
        container.register(ServiceConsumer.class, MultipleParametersServiceConsumer.class);
        container.get(ServiceConsumer.class);
    }

    @Test
    public void should_get_component_when_register_by_package() {
        container.registerComponentsInPackage("com.thoughtworks.row.ioc.annotation");
        SimpleController simpleController = container.get(SimpleController.class);

        assertThat(simpleController, notNullValue());
    }

    @Test
    public void should_register_by_package() {
        container.registerComponentsInPackage("com.thoughtworks.row.ioc.annotation");
        container.register(Service.class, ServiceImplementation.class);

        ArrayList<Object> controllers = container.getComponentsByAnnotation(Controller.class);

        assertThat(controllers.size(), is(2));
        assertThat(controllers, hasItem(isA(SimpleController.class)));
        assertThat(controllers, hasItem(isA(ComplexController.class)));

        ComplexController complexController = (ComplexController) (controllers.get(0) instanceof SimpleController ?
                controllers.get(1) : controllers.get(0));

        assertThat(complexController.service(), is(ServiceImplementation.class.getCanonicalName()));
    }
}
