package parser.result;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import java.util.ArrayList;

import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Iterables.tryFind;

public class Cookbook {
    private ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    private ArrayList<Cookbook> parentCookbooks = new ArrayList<Cookbook>();

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
        recipes.add(recipe);
    }

    public Recipe findRecipe(final String name) {
        Optional<Recipe> recipe = findRecipeFromCookbook(name);
        return recipe.isPresent() ? recipe.get() : getRecipeFromParent(name);
    }

    private Recipe getRecipeFromParent(String name) {
        for (Cookbook parent : parentCookbooks) {
            Recipe recipeFromParent = parent.findRecipe(name);
            if (recipeFromParent.isPresent()) {
                return recipeFromParent;
            }
        }
        return new NullRecipe();
    }

    private Optional<Recipe> findRecipeFromCookbook(final String name) {
        return tryFind(recipes, new Predicate<Recipe>() {
            @Override
            public boolean apply(Recipe recipe) {
                return recipe.getName().equalsIgnoreCase(name);
            }
        });
    }

    public void addParent(Cookbook parentCookbook) {
        parentCookbooks.add(parentCookbook);
    }
}
