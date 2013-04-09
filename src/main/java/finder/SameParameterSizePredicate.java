package finder;

import com.google.common.base.Predicate;

import java.lang.reflect.Constructor;

import static com.google.common.collect.Iterables.size;

public class SameParameterSizePredicate implements Predicate<Constructor> {

    private Iterable<Object> constructorValues;

    public SameParameterSizePredicate(Iterable<Object> constructorValues) {
        this.constructorValues = constructorValues;
    }

    @Override
    public boolean apply(Constructor constructor) {
        return size(constructorValues) == constructor.getParameterTypes().length;
    }
}
