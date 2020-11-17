package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngId(Integer recipeId, Integer ingredientId);

    IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand);

    void deleteById(Integer recipeid, Integer ingredientid);
}
