package finder;

import com.google.common.collect.Lists;
import parser.result.Recipe;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.find;

public class ConstructorFinder {

    public Constructor getSpecificConstructor(final Recipe recipe, Constructor<?>[] constructors) {
        ArrayList<Constructor<?>> constructorList = Lists.newArrayList(constructors);
        Iterable<Constructor<?>> filteredConstructors = filter(constructorList, new SameParameterSizePredicate(recipe));
        return find(filteredConstructors, new AppropriateParameterPredicate(recipe));
    }
}

