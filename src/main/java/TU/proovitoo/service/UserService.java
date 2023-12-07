package TU.proovitoo.service;

import TU.proovitoo.model.User;
import TU.proovitoo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User authenticate(String username, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return userRepository.findByUsername(username)
                .filter(user -> encoder.matches(password, user.getPassword()))
                .orElse(null);
    }

}