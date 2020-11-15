package guru.springframework.services;

import guru.springframework.converts.RecipeCommandToRecipe;
import guru.springframework.converts.RecipeToRecipeCommand;
import guru.springframework.model.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);

        recipeService=new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    public void getRecipes() {
        Recipe recipe=new Recipe();
        HashSet recipeData=new HashSet();
        recipeData.add(recipe);

        when(recipeRepository.findAll()).thenReturn(recipeData);
        Set<Recipe> recipes= recipeService.getRecipes();
        assertEquals(recipes.size(),1);
        verify(recipeRepository,times(1)).findAll();
    }

    @Test
    public void getRecipeByIdTest() throws Exception{
        Recipe recipe=new Recipe();
        recipe.setId(1);
        Optional<Recipe> recipeOptional=Optional.of(recipe);

        when(recipeRepository.findById(anyInt())).thenReturn(recipeOptional);
        Recipe recipeReturned=recipeService.findById(1);
        assertNotNull("Null recipe returned",recipeReturned);

        verify(recipeRepository,times(1)).findById(anyInt());
        verify(recipeRepository,never()).findAll();

    }
}