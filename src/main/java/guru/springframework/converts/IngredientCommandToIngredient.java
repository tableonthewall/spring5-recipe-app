package guru.springframework.converts;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.model.Ingredient;
import guru.springframework.model.Recipe;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {
    private final UnitOfMasureCommandToUnitOfMeasure uomConverter;

    public IngredientCommandToIngredient(UnitOfMasureCommandToUnitOfMeasure uomConverter) {
        this.uomConverter = uomConverter;
    }

    @Nullable
    @Override
    public Ingredient convert(IngredientCommand ingredientCommand) {
        if(ingredientCommand==null){
            return null;
        }

        final Ingredient ingredient=new Ingredient();

        ingredient.setId(ingredientCommand.getId());

        //ingredientCommand ile gelen recipe bilgilerini ingredient olarak dönüştürme işlemi
        if(ingredientCommand.getRecipeId()!=null){
            Recipe recipe=new Recipe();
            recipe.setId(ingredientCommand.getRecipeId());
            ingredient.setRecipe(recipe);
            recipe.addIngredient(ingredient);
        }

        ingredient.setAmount(ingredientCommand.getAmount());
        ingredient.setDescription(ingredientCommand.getDescription());
        ingredient.setUnitOfMeasure(uomConverter.convert(ingredientCommand.getUnitOfMeasure()));


        return ingredient;
    }
}
