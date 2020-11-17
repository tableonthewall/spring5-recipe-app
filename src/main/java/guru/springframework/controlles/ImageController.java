package guru.springframework.controlles;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {
    private final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    //image upload forma RecipeCommand nesnesi ile birlikte yönelendiriyoruz
    @GetMapping("recipe/{recipeid}/image")
    public String showUpLoadForm(@PathVariable String recipeid, Model model){
        RecipeCommand recipeCommand=recipeService.findCommandById(Integer.valueOf(recipeid));
        model.addAttribute("recipe",recipeCommand);

        return "recipe/imageuploadform";
    }

    //kayıt gerçekleştiriyoruz
    @PostMapping("recipe/{recipeid}/image")
    public String handleImagePost(@PathVariable String recipeid, @RequestParam("imagefile") MultipartFile file){
        imageService.saveImageFile(Integer.valueOf(recipeid),file);

        return "redirect:/recipe/"+recipeid+"/show";

    }

    @GetMapping("recipe/{recipeid}/recipeimage")
    public void renderImageFromDB(@PathVariable String recipeid, HttpServletResponse response) throws IOException{
        RecipeCommand recipeCommand=recipeService.findCommandById(Integer.valueOf(recipeid));
        byte[] byteArray=new byte[recipeCommand.getImage().length];
        int i=0;
        for(Byte wrappedByte : recipeCommand.getImage()){
            byteArray[i++]=wrappedByte;
        }

        response.setContentType("image/jpeg");
        InputStream is=new ByteArrayInputStream(byteArray);
        IOUtils.copy(is,response.getOutputStream());
    }
}
