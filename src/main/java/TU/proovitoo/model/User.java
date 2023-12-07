package TU.proovitoo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User {

    @Id
    private Long id;
    @Getter
    @Setter
    private String username;
    @Getter
    private String password;
    @Getter
    private String name;

    public Long getId() {
        return id;
    }
}