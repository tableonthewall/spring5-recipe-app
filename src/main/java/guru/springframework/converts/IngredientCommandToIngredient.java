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
        System.out.println(ingredient.getId());

        //ingredientCommand ile gelen recipe bilgilerini ingredient olarak dönüştürme işlemi
        if(ingredientCommand.getRecipeId()!=null){
            Recipe recipe=new Recipe();
            recipe.setId(ingredientCommand.getRecipeId());
            System.out.println("dönüştürme sonrası id1: "+ingredient.getId());
            ingredient.setRecipe(recipe);
            System.out.println("dönüştürme sonrası id2: "+ingredient.getId());
            recipe.addIngredient(ingredient);
            System.out.println("dönüştürme sonrası id3: "+ingredient.getId());
        }

        ingredient.setAmount(ingredientCommand.getAmount());
        ingredient.setDescription(ingredientCommand.getDescription());
        ingredient.setUnitOfMeasure(uomConverter.convert(ingredientCommand.getUnitOfMeasure()));

        System.out.println("dönüştürme sonrası id4: "+ingredient.getId());
        return ingredient;
    }
}
