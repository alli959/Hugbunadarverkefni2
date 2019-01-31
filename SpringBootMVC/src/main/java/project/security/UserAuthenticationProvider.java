package project.security;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import project.persistence.entities.*;
import project.persistence.repositories.*;

import java.util.ArrayList;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    UserRepository userRepository;
    // Basically looks up the the user by the email provided and
    // Matches the password with the password stored in the database
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        System.out.println("Something");
        Optional<User> check = userRepository.findById(userName);
        if (!check.isPresent()) return null;
        User user = check.get();
        System.out.println("Something 2");
        if(authentication.getCredentials() == null) {
            if(user == null) System.out.println("User did not exist, was null. Lookup value: " + authentication.getName());
            else System.out.println("Credentials were null");
            return null;
        }
        String password = authentication.getCredentials().toString();
        if(user.getPassword().equals(password)) {
            return new UsernamePasswordAuthenticationToken(userName, password, new ArrayList<>());
        }
        System.out.println("Password incorrect, but user did exists");
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
