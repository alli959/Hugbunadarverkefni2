
package project.controller;



import org.springframework.web.bind.annotation.RequestParam;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.persistence.entities.User;
import project.persistence.repositories.UserRepository;;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class RegisterController {
  @Autowired
  private UserRepository userRepository;

  // This method is allowed when the authentication header is set to
  //    Username: anonymous
  //    Password: anonymous
  //
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

  // Eg aetla ekki ad nota thetta, functionid ad ofan virkar nogu vel
  @Deprecated
  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public String createUserPost(@ModelAttribute("createUser") User inputUser,
  Model model) {
    User dbUser = userRepository.findById(inputUser.getName()).get();
    if(dbUser != null){
      model.addAttribute("error","User already exists");
      return "/register";
    }
    inputUser.setPassword(BCrypt.hashpw(inputUser.getPassword(), BCrypt.gensalt()));
    userRepository.save(inputUser);
    model.addAttribute("createUser", new User());
    return "redirect:login";
  }
}
