package parser.result;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static parser.result.Cookbook.createByRecipes;
import static parser.simple.SimpleParser.createRecipe;

public class CookbookTest {

    @Test
    public void should_find_recipe_in_parent_container() throws Exception {
        Recipe recipe = createRecipe("bank", "product.Bank");
        Cookbook parent = createByRecipes(recipe);
        Cookbook son = createByRecipes(createRecipe("user", "product.User"));

        son.addParent(parent);

        assertThat(son.findRecipe("bank"), is(recipe));
    }

    @Test
    public void should_find_recipe_in_grandfather_container() throws Exception {
        Recipe recipe = createRecipe("bank", "product.Bank");
        Cookbook grandfather = createByRecipes(recipe);
        Cookbook parent = createByRecipes(createRecipe("object", "Object"));
        Cookbook son = createByRecipes(createRecipe("user", "product.User"));

        parent.addParent(grandfather);
        son.addParent(parent);

        assertThat(son.findRecipe("bank"), is(recipe));
    }
}
