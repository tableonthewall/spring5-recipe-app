package guru.springframework.controlles;

import guru.springframework.repositories.RecipeRepository;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class ImageControllerTest {

    @Mock
    ImageService imageService;

    @Mock
    RecipeService recipeService;

    ImageController imageController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        imageController=new ImageController(imageService,recipeService);
        mockMvc= MockMvcBuilders.standaloneSetup(imageController).setControllerAdvice(new ControllerExcepitonHandler()).build();
    }

    @Test
    public void testGetImageNumberFormatException() throws Exception{
        mockMvc.perform(get("/recipe/asdasd/image"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    void showUpLoadForm() {
    }

    @Test
    void handleImagePost() {
    }

    @Test
    void renderImageFromDB() {
    }
}