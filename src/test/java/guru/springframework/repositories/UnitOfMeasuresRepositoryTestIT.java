package guru.springframework.repositories;

import guru.springframework.model.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UnitOfMeasuresRepositoryTestIT {

    @Autowired
    UnitOfMeasuresRepository unitOfMeasuresRepository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void findByDescription() throws Exception {
        Optional<UnitOfMeasure> uomOptional=unitOfMeasuresRepository.findByDescription("Kepçe");
        assertEquals("Kepçe",uomOptional.get().getDescription());
    }

    @Test
    public void findByDescriptionGram() throws Exception {
        Optional<UnitOfMeasure> uomOptional=unitOfMeasuresRepository.findByDescription("Gram");
        assertEquals("Gram",uomOptional.get().getDescription());
    }


}