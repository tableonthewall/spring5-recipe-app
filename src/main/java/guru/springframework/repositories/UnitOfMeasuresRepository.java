package guru.springframework.repositories;

import guru.springframework.model.UnitOfMeasure;
import org.springframework.data.repository.CrudRepository;

public interface UnitOfMeasuresRepository extends CrudRepository<UnitOfMeasure,Integer> {
}
