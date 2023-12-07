package TU.proovitoo.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    private String name;

}