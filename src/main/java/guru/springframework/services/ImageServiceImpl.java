package guru.springframework.services;

import guru.springframework.model.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImageFile(Integer recipeid, MultipartFile file) {
        try {
            Recipe recipe=recipeRepository.findById(recipeid).get();

            Byte[] byteObject=new Byte[file.getBytes().length];

            int i=0;
            for(byte b: file.getBytes()){
                byteObject[i++]=b;
            }
            recipe.setImage(byteObject);

            recipeRepository.save(recipe);

        }catch (Exception e){
            log.error("Fotoğraf yükleme sırasında bir hata ile karşılaşıldı.",e);
            e.printStackTrace();
        }
    }
}
