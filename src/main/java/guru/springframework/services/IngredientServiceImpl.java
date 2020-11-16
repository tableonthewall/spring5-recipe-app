package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converts.IngredientCommandToIngredient;
import guru.springframework.converts.IngredientToIngredientCommand;
import guru.springframework.model.Ingredient;
import guru.springframework.model.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasuresRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final UnitOfMeasuresRepository unitOfMeasuresRepository;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientToIngredientCommand ingredientToIngredientCommand, UnitOfMeasuresRepository unitOfMeasuresRepository, IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.unitOfMeasuresRepository = unitOfMeasuresRepository;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
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

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
        Optional<Recipe> optionalRecipe=recipeRepository.findById(ingredientCommand.getRecipeId());
        if(!optionalRecipe.isPresent()){
            //todo error if not found
            log.error("Tarif bulunamamıştır: "+ingredientCommand.getRecipeId());
            return new IngredientCommand();
        }else{
            Recipe recipe=optionalRecipe.get();

            Optional<Ingredient> ingredientOptional=recipe
            .getIngredientSet()
            .stream()
            .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
            .findFirst();

            if(ingredientOptional.isPresent()){
                Ingredient ingredientFound=ingredientOptional.get();
                ingredientFound.setDescription(ingredientCommand.getDescription());
                ingredientFound.setAmount(ingredientCommand.getAmount());
                ingredientFound.setUnitOfMeasure(unitOfMeasuresRepository
                        .findById(ingredientCommand.getUnitOfMeasure().getId())
                        .orElseThrow(()->new RuntimeException("Ölçü bulunamamıştır.")));
            }else{
                //ingredient varsa yukarda onu güncelledik eğer yoksa tarife ekliyoruz.
                recipe.addIngredient(ingredientCommandToIngredient.convert(ingredientCommand));
            }

            Recipe savedRecipe=recipeRepository.save(recipe);

            return ingredientToIngredientCommand.convert(savedRecipe.getIngredientSet().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(ingredientCommand.getId()))
                    .findFirst()
                    .get());

        }
    }
}
