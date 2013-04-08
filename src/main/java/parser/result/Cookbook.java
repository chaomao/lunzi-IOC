package parser.result;

import com.google.common.base.Predicate;

import java.util.ArrayList;

import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Iterables.toArray;

public class Cookbook {
    private ArrayList<Recipe> descriptions = new ArrayList<Recipe>();

    public static Cookbook createByRecipes(Recipe... recipes) {
        Cookbook cookbook = new Cookbook();
        for (Recipe r : recipes) {
            cookbook.add(r);
        }
        return cookbook;
    }

    public static Cookbook createByRecipes(Iterable<Recipe> recipes) {
        return createByRecipes(toArray(recipes, Recipe.class));
    }

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
