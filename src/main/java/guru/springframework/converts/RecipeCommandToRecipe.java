package guru.springframework.converts;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.model.Recipe;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {
    private final CategoryCommandToCategory categoryConverter;
    private final IngredientCommandToIngredient ingredientConverter;
    private final NotesCommandToNotes notesConverter;

    public RecipeCommandToRecipe(CategoryCommandToCategory categoryConverter, IngredientCommandToIngredient ingredientConverter, NotesCommandToNotes notesConverter) {
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
    }

    @Override
    public Recipe convert(RecipeCommand recipeCommand) {
        if(recipeCommand==null){
            return null;
        }
        final Recipe recipe=new Recipe();
        recipe.setId(recipeCommand.getId());
        recipe.setCookTime(recipeCommand.getCookTime());
        recipe.setPrepTime(recipeCommand.getPrepTime());
        recipe.setDescription(recipeCommand.getDescription());
        recipe.setDifficulty(recipeCommand.getDifficulty());
        recipe.setDirections(recipeCommand.getDirections());
        recipe.setServings(recipeCommand.getServings());
        recipe.setSource(recipeCommand.getSource());
        recipe.setUrl(recipeCommand.getUrl());
        recipe.setNotes(notesConverter.convert(recipeCommand.getNotes()));

        if(recipeCommand.getCategories()!=null && recipeCommand.getCategories().size()>0){
            recipeCommand.getCategories()
                    .forEach(category-> recipe.getCategories().add(categoryConverter.convert(category)));
        }

        if(recipeCommand.getIngredientSet()!=null && recipeCommand.getIngredientSet().size()>0){
            recipeCommand.getIngredientSet()
                    .forEach(ingedient -> recipe.getIngredientSet().add(ingredientConverter.convert(ingedient)));
        }




        return recipe;
    }
}
