package guru.springframework.controlles;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import javax.validation.Valid;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Slf4j
@Controller
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @GetMapping("/recipe/{id}/show")
    public String showById(@PathVariable String id, Model model){
        try {
            Integer newID=Integer.parseInt(id);
        }catch(NumberFormatException e){
            throw new NumberFormatException("Girilen ifade sayı değildir :"+id);
        }
        model.addAttribute(recipeService.findById(new Integer(id)));
        return "/recipe/show";
    }

    @GetMapping("/recipe/new")
    private String newRecipe(Model model){
        //recipe isminde RecipeCommand tipinde bir nesne gönderiyoruz.
        model.addAttribute("recipe",new RecipeCommand());

        return "recipe/recipeform";
    }


    //Update'den ya da new'den gelen Post metodunu karşılıyor. Nesneyi kaydedip show sayfasına yönlendiriyor.
    @PostMapping("recipe")
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });
            return "recipe/recipeform";
        }
        RecipeCommand savedCommand=recipeService.saveRecipeCommand(command);

        return "redirect:/recipe/"+savedCommand.getId()+"/show";
    }


    //Update GET metodu update sayfasına yönlendiriyor.
    @GetMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable String id,Model model){
        model.addAttribute("recipe",recipeService.findCommandById(Integer.valueOf(id)));
        return "recipe/recipeform";

    }
    
    //Delete method
    @GetMapping("/recipe/{id}/delete")
    public String deleteById(@PathVariable String id){
        recipeService.deleteById(Integer.valueOf(id));
        return "redirect:/";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(Model model,Exception exception){
        log.error("Handling not found exception");
        log.error(exception.getMessage());
        model.addAttribute("message",exception.getMessage());
        return "404error";
    }




}
