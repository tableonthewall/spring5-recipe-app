package guru.springframework.controlles;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Slf4j
@Controller
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping("/recipe/{id}/show")
    public String showById(@PathVariable String id, Model model){
        model.addAttribute(recipeService.findById(new Integer(id)));
        return "/recipe/show";
    }

    @RequestMapping("/recipe/new")
    private String newRecipe(Model model){
        //recipe isminde RecipeCommand tipinde bir nesne gönderiyoruz.
        model.addAttribute("recipe",new RecipeCommand());

        return "recipe/recipeform";
    }


    //Update'den ya da new'den gelen Post metodunu karşılıyor. Nesneyi kaydedip show sayfasına yönlendiriyor.
    @PostMapping
    @RequestMapping("recipe")
    public String saveOrUpdate(@ModelAttribute RecipeCommand command){
        RecipeCommand savedCommand=recipeService.saveRecipeCommand(command);

        return "redirect:/recipe/"+savedCommand.getId()+"/show";
    }
    //Update GET metodu update sayfasına yönlendiriyor.
    @RequestMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable String id,Model model){
        model.addAttribute("recipe",recipeService.findCommandById(Integer.valueOf(id)));
        return "recipe/recipeform";

    }
    
    //Delete method
    @GetMapping
    @RequestMapping("/recipe/{id}/delete")
    public String deleteById(@PathVariable String id){
        recipeService.deleteById(Integer.valueOf(id));
        return "redirect:/";
    }


}
