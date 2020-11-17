package guru.springframework.controlles;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    //List all ingredients
    @GetMapping
    @RequestMapping("/recipe/{recipeid}/ingredients")
    public String listIngredients(@PathVariable String recipeid, Model model){
        log.debug("malzemeler tarif id'sine göre alınıyor: "+recipeid);

        model.addAttribute("recipe",recipeService.findCommandById(Integer.valueOf(recipeid)));

        return "recipe/ingredient/list";
    }

    //Ingredient show
    @GetMapping
    @RequestMapping("/recipe/{recipeid}/ingredient/{ingredientid}/show")
    public String showIngredient(@PathVariable String recipeid,@PathVariable String ingredientid,Model model){
        //IngredientCommand gönderiyorum
        model.addAttribute("ingredient",ingredientService.findByRecipeIdAndIngId(Integer.valueOf(recipeid),Integer.valueOf(ingredientid)));
        return "recipe/ingredient/show";
    }

    //New Ingredient
    @GetMapping
    @RequestMapping("/recipe/{recipeid}/ingredient/new")
    public String newIngredient(@PathVariable String recipeid,Model model){
        RecipeCommand recipeCommand= recipeService.findCommandById(Integer.valueOf(recipeid));

        if(recipeCommand==null){
            throw new RuntimeException("Tarif bilgisi bulunamıyor.");
        }
        IngredientCommand command=new IngredientCommand();
        command.setRecipeId(recipeCommand.getId());

        model.addAttribute("ingredient",command);

        command.setUnitOfMeasure(new UnitOfMeasureCommand());

        model.addAttribute("unitOfMeasure",unitOfMeasureService.listAllUoms());

        return "/recipe/ingredient/ingredientform";

    }
    //Update Ingredient
    @GetMapping
    @RequestMapping("/recipe/{recipeid}/ingredient/{ingredientid}/update")
    public String updateIngredient(@PathVariable String recipeid,@PathVariable String ingredientid,Model model){
        model.addAttribute("ingredient",ingredientService.findByRecipeIdAndIngId(Integer.valueOf(recipeid),Integer.valueOf(ingredientid)));
        model.addAttribute("unitOfMeasure",unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredientform ";
    }

    //Delete Ingredient
    @GetMapping
    @RequestMapping("/recipe/{recipeid}/ingredient/{ingredientid}/delete")
    public String deleteIngredient(@PathVariable String recipeid, @PathVariable String ingredientid){
        ingredientService.deleteById(Integer.valueOf(recipeid),Integer.valueOf(ingredientid));
        return "redirect:/recipe/"+recipeid+"/ingredients";
    }

    //gelen post'u recipe başına "/" karakteri eklemeden alıyoruz.
    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command){
        IngredientCommand savedCommand=ingredientService.saveIngredientCommand(command);

        log.debug("saved recipe id: "+savedCommand.getRecipeId());
        log.debug("saved ingredient id : "+savedCommand.getId());

        return "redirect:/recipe/"+savedCommand.getRecipeId()+"/ingredient/"+savedCommand.getId()+"/show";
    }




}
