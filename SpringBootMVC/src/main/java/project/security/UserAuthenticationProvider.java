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
        String password = authentication.getCredentials().toString();
        System.out.println("Checking Authentication for " + userName + " and password " + password);
        Optional<User> check = userRepository.findById(userName);
        if (!check.isPresent()) return null;
        User user = check.get();
        if(authentication.getCredentials() == null) {
            if(user == null) System.out.println("User did not exist, was null. Lookup value: " + authentication.getName());
            else System.out.println("Credentials were null");
            return null;
        }
        if(user.getPassword().equals(password)) {
            System.out.println("User authenticated sending token for " + userName + ":" + password);

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
