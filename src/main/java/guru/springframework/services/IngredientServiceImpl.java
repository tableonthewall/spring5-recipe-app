package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converts.IngredientToIngredientCommand;
import guru.springframework.model.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientToIngredientCommand ingredientToIngredientCommand) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngId(Integer recipeId, Integer ingredientId) {
        Optional<Recipe> recipeOptional=recipeRepository.findById(recipeId);

        if(!recipeOptional.isPresent()){
            log.error("tarif id'si bulunamamıştır. :"+recipeId);
        }
        Recipe recipe=recipeOptional.get();

        //ingredientcommand nesnesi oluşturuyoruz
        //recipe nesnesinde bulunan ingredientSet setinde stream yapıp dolaşıyoruz.
        //herbir ingredient nesnesinin .getId()'si ile bizim aradığımız ingredientId'yi filtreliyoruz.
        //bulduğumuz nesneyi map ile ingredient'ten ingredientcommand nesnesine dönüştürüyoruz ve böylece başta oluşturduğumuz referansa karşılık nesneyi
        //atıyoruz......
        Optional<IngredientCommand> ingredientCommand=recipe.getIngredientSet().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

        if(!ingredientCommand.isPresent()){
            log.error("malzeme id'si bulunamamıştır :"+ingredientId);
        }

        return ingredientCommand.get();
    }
}
