package finder;

import com.google.common.collect.Lists;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.find;

public class ConstructorFinder {

    public Constructor getSpecificConstructor(Iterable<Object> constructorValues, Constructor<?>[] constructors) {
        ArrayList<Constructor<?>> constructorList = Lists.newArrayList(constructors);
        Iterable<Constructor<?>> filteredConstructors = filter(constructorList, new SameParameterSizePredicate(constructorValues));
        return find(filteredConstructors, new AppropriateParameterPredicate(constructorValues));
    }
}

