package guru.springframework.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void saveImageFile(Integer recipeid, MultipartFile file);

}
