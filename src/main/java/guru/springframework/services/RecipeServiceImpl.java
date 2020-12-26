package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converts.RecipeCommandToRecipe;
import guru.springframework.converts.RecipeToRecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.model.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;


    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("Recipe Service Implement");

        Set<Recipe> recipeSet=new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
        return recipeSet;
    }

    @Override
    public Recipe findById(Integer id) {

        Optional<Recipe> recipeOptional=recipeRepository.findById(id);

        if(!recipeOptional.isPresent()){
            throw new NotFoundException("Tarif bulunamadı.. "+id.toString());
        }
        return recipeOptional.get();
    }

    @Override
    public void deleteById(Integer id) {
        recipeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public RecipeCommand findCommandById(Integer id) {
        return recipeToRecipeCommand.convert(findById(id));
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
        Recipe detachedRecipe=recipeCommandToRecipe.convert(recipeCommand);
        Recipe savedRecipe=recipeRepository.save(detachedRecipe);
        log.debug("Saved recipeId: "+savedRecipe.getId());

        return recipeToRecipeCommand.convert(savedRecipe);
    }

}
