package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converts.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.model.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasuresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;

class UnitOfMeasureServiceImplTest {

    UnitOfMeasureService service;
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand=new UnitOfMeasureToUnitOfMeasureCommand();


    @Mock
    UnitOfMeasuresRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service=new UnitOfMeasureServiceImpl(repository,unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    void listAllUoms() {
        //given
        Set<UnitOfMeasure> unit=new HashSet<>();
        UnitOfMeasure uom1=new UnitOfMeasure();
        uom1.setId(1);
        unit.add(uom1);

        UnitOfMeasure uom2=new UnitOfMeasure();
        uom2.setId(2);
        unit.add(uom2);

        when(repository.findAll()).thenReturn(unit);

        //when
        Set<UnitOfMeasureCommand> commands=service.listAllUoms();

        assertEquals(2,commands.size());
        verify(repository,times(1)).findAll();
    }
}