
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

@RestController
public class RegisterController {
  @Autowired
  private UserRepository userRepository;

  // Done
  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public String createUser(
    @RequestParam String userName,
    @RequestParam String password,
    @RequestParam String name,
    @RequestParam String email
  ) {
    if (!userRepository.findById(userName).isPresent())
      return "User already exists bruh";
    User user = new User();
    user.setEmail(email);
    user.setPassword(password);
    user.setName(name);
    user.setEmail(email);
    String uName = userRepository.save(user).getUserName();
    System.out.printf("Created user with username: %s, password: %s, email: %s", userName, password, email);
    return uName;
  }

  // Kinda done
  @Deprecated // Eg aetla ekki ad nota thetta
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
