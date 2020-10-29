package guru.springframework.repositories;



import guru.springframework.model.UnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UnitOfMeasuresRepository extends CrudRepository<UnitOfMeasure,Integer> {

    Optional<UnitOfMeasure> findByDescription(String description);

}
