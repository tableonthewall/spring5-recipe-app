package guru.springframework.repositories;

<<<<<<< Updated upstream

import guru.springframework.model.UnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UnitOfMeasuresRepository extends CrudRepository<UnitOfMeasure,Integer> {

    Optional<UnitOfMeasure> findByDescription(String description);
=======
import guru.springframework.model.UnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

public interface UnitOfMeasuresRepository extends CrudRepository<UnitOfMeasure,Integer> {
>>>>>>> Stashed changes
}
