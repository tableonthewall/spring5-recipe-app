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
import java.util.zip.Inflater;

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
        //IngredientCommand'dan Recipe bulunuyor. - Optional Recipe olarak
        Optional<Recipe> optionalRecipe=recipeRepository.findById(ingredientCommand.getRecipeId());
        //recipe oluşan nesnenin recipe olup olmadığı test ediliyor.
        if(!optionalRecipe.isPresent()){
            //todo error if not found
            log.error("Tarif bulunamamıştır: "+ingredientCommand.getRecipeId());
            return new IngredientCommand();
        }else{
            //Optional Recipe olarak dönüştürülüyor.
            Recipe recipe=optionalRecipe.get();
            //IngredientCommand nesnesinin id'si recipe içinde bulunan IngredientSet listesinin elemanlarının id'sinden eşleşen eleman
            //optional olarak Ingredinet-optional nesnesine eşitleniyor.
            Optional<Ingredient> ingredientOptional=recipe
            .getIngredientSet()
            .stream()
            .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
            .findFirst();

            //eğer ingredient optional nesnesi null değilse
            if(ingredientOptional.isPresent()){
                //Optional-Ingredient Ingredient'e dönüştürülüyor.
                Ingredient ingredientFound=ingredientOptional.get();
                ///ingredientFound'un description ve amout değerleri formdan gelen IngredientCommand nesnesinin elemanları ile güncelleniyor.
                ingredientFound.setDescription(ingredientCommand.getDescription());
                ingredientFound.setAmount(ingredientCommand.getAmount());
                //Ölçü değeri de yine IngredientCommand'dan gelen unitOfMeasure'un id'si ile güncelleniyor.
                ingredientFound.setUnitOfMeasure(unitOfMeasuresRepository
                        .findById(ingredientCommand.getUnitOfMeasure().getId())
                        .orElseThrow(()->new RuntimeException("Ölçü bulunamamıştır.")));
            }else{
                //ingredient varsa yukarda onu güncelledik eğer yoksa tarife ekliyoruz.
                //Tarife-> IngredientCommand'ı Ingredient olarak convert edip Tarife ekliyoruz.
                recipe.addIngredient(ingredientCommandToIngredient.convert(ingredientCommand));
            }
            //Tarifi recipeRepository yardımıyla kaydediyoruz.
            Recipe savedRecipe=recipeRepository.save(recipe);
            //kaydettiğimiz savedRecipe içerisinden OptionalSavedIngredient olarak Ingredient nesnemizi buluyoruz.
            Optional<Ingredient> optionalSavedIngredient=savedRecipe
                    .getIngredientSet()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                    .findFirst();

            //eğer optionalSavedIngredient elemanı Recipe içinde bulunamamışsa değerleri kendimiz ingredientCommandla eşleştiriyoruz.
            if(!optionalSavedIngredient.isPresent()){
                optionalSavedIngredient=savedRecipe.getIngredientSet().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(ingredientCommand.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(ingredientCommand.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId().equals(ingredientCommand.getUnitOfMeasure().getId()))
                        .findFirst();
            }

            return ingredientToIngredientCommand.convert(optionalSavedIngredient.get());
        }
    }

    @Override
    public void deleteById(Integer recipeid, Integer ingredientid) {
        log.debug("Deleting Ingredient: "+recipeid+" : "+ingredientid);
        Optional<Recipe> optionalRecipe=recipeRepository.findById(recipeid);

        if(optionalRecipe.isPresent()){
            Recipe recipe=optionalRecipe.get();
            log.debug("found recipe");

            Optional<Ingredient> ingredientOptional=recipe
                    .getIngredientSet()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientid))
                    .findFirst();


            if(ingredientOptional.isPresent()){
                log.debug("ingredient found");

                Ingredient ingredient=ingredientOptional.get();
                ingredient.setRecipe(null);
                //tariften malzemeyi siliyoruz.
                recipe.getIngredientSet().remove(ingredientOptional.get());
                recipeRepository.save(recipe);

            }

        }else{
            log.debug("Recipe Id Not found: "+recipeid);
        }

    }
}
