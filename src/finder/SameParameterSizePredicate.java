package finder;

import com.google.common.base.Predicate;
import com.sun.istack.internal.Nullable;
import parser.result.Recipe;

import java.lang.reflect.Constructor;

public class SameParameterSizePredicate implements Predicate<Constructor> {

    private Recipe recipe;

    public SameParameterSizePredicate(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    @Nullable
    public boolean apply(Constructor constructor) {
        return recipe.getConstructorInjectorCount() == constructor.getParameterTypes().length;
    }
}
