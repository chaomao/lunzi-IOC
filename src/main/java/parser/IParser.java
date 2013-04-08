package parser;

import parser.result.Recipe;

public interface IParser {
    Iterable<Recipe> getRecipes();
}
