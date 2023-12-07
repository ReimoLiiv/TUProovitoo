package TU.proovitoo;

import TU.proovitoo.model.User;
import TU.proovitoo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;



@Component
public class UserInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SpringLiquibase liquibase;

    @PostConstruct
    public void initUsers() {
        if (userRepository.count() == 0) {
            createUser("joonas123", "fiction", "Joonas");
            createUser("maria321", "doping", "Maria");
            createUser("toomas111", "soccer", "Toomas");
        }
        try {
            liquibase.setContexts("dataInsertion");
            liquibase.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createUser(String username, String rawPassword, String name) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setName(name);
        userRepository.save(user);
    }
}