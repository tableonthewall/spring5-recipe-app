package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;


import java.util.Set;

public interface UnitOfMeasureService {
    //Return Set list all unit of measures
    Set<UnitOfMeasureCommand> listAllUoms();
}
