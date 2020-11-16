package guru.springframework.controlles;

import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeid}/ingredients")
    public String listIngredients(@PathVariable String recipeid, Model model){
        log.debug("malzemeler tarif id'sine göre alınıyor: "+recipeid);

        model.addAttribute("recipe",recipeService.findCommandById(Integer.valueOf(recipeid)));

        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeid}/ingredient/{ingredientid}/show")
    public String showIngredient(@PathVariable String recipeid,@PathVariable String ingredientid,Model model){
        //IngredientCommand gönderiyorum
        model.addAttribute("ingredient",ingredientService.findByRecipeIdAndIngId(Integer.valueOf(recipeid),Integer.valueOf(ingredientid)));
        return "recipe/ingredient/show";
    }

}
