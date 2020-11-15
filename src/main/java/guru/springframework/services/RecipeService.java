package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.model.Recipe;
import org.springframework.stereotype.Service;

import java.util.Set;


public interface RecipeService {
    Set<Recipe> getRecipes();
    Recipe findById(Integer id);

    //expanding recipeService
    RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand);
}
