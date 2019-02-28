
package project.controller;



import org.springframework.web.bind.annotation.RequestParam;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.persistence.entities.User;
import project.persistence.repositories.UserRepository;;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class RegisterController {
  @Autowired
  private UserRepository userRepository;

  // Method: localhost:8080/register?userName=[string]&password=[string]&name=[string]&email=[string]
  // Attntn: Needs the header 'Authorization' to be set to 
  //    Username: 'anonymous'
  //    Password: 'anonymous'
  // Return: userName of the user created if successful, if already exists then "User already exists bruh"
  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public String createUser(
    @RequestParam String userName,
    @RequestParam String password,
    @RequestParam String name,
    @RequestParam String email
  ) {
    if (userRepository.findById(userName).isPresent())
      return "User already exists bruh";
    User user = new User();
    user.setUserName(userName);
    user.setEmail(email);
    user.setPassword(password);
    user.setName(name);
    user.setEmail(email);
    String uName = userRepository.save(user).getUserName();
    System.out.printf("Created user with username: %s, password: %s, email: %s", userName, password, email);
    return uName;
  }
}
