
package project.controller;



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

  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public String createUser(Model model) {
    model.addAttribute("msg", "Please Enter Your Information");
    model.addAttribute("createUser", new User());
    return "register";
  }

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
