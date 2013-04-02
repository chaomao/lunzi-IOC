import com.google.common.base.Predicate;
import parser.result.Recipe;

import java.util.ArrayList;

import static com.google.common.collect.Iterables.find;

public class Cookbook {
    private ArrayList<Recipe> descriptions = new ArrayList<Recipe>();

    public void add(Recipe recipe) {
        descriptions.add(recipe);
    }

    public Recipe findRecipe(final String name) {
        return find(descriptions, new Predicate<Recipe>() {
            @Override
            public boolean apply(Recipe recipe) {
                return recipe.getName().equalsIgnoreCase(name);
            }
        });
    }
}
