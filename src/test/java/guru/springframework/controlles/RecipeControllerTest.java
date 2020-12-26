package guru.springframework.controlles;

import guru.springframework.exceptions.NotFoundException;
import guru.springframework.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class RecipeControllerTest {

    @Mock
    RecipeService recipeService;

    RecipeController recipeController;

    MockMvc mockMvc;



    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        recipeController=new RecipeController(recipeService);
        mockMvc= MockMvcBuilders.standaloneSetup(recipeController).build();
    }

    @Test
    public void testGetRecipeNotFound() throws Exception {
        when(recipeService.findById(anyInt())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/4/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    public void testGetRecipeNumberFormat() throws Exception {
        //when(recipeService.findById(anyInt())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/hgjg/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    void showById() {
    }

    @Test
    void saveOrUpdate() {
    }

    @Test
    void updateRecipe() {
    }

    @Test
    void deleteById() {
    }
}