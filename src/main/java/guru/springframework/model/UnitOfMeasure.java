package guru.springframework.model;


import lombok.Data;

import javax.persistence.*;


@Data
@Entity
public class UnitOfMeasure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;




}
