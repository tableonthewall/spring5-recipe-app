package guru.springframework.repositories;

import guru.springframework.model.Category;
import org.springframework.data.repository.CrudRepository;

<<<<<<< Updated upstream
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category,Integer> {

    Optional<Category> findByDescription(String description);
=======
public interface CategoryRepository extends CrudRepository<Category, Integer> {
>>>>>>> Stashed changes
}
