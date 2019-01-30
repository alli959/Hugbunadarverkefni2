package project.controller;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.persistence.entities.User;
import project.persistence.repositories.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  @Autowired
  private UserRepository userRepository;

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String init(Model model) {
    model.addAttribute("msg", "Please Enter Your Login Details");
    return "login";
  }

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public String submit(@ModelAttribute("user") User inputUser, HttpSession session, Model model) {
    model.addAttribute("user", new User());
    model.addAttribute("error", session.getAttribute("error"));
    session.removeAttribute("error");
    String userName = inputUser.getUserName();
    String password = inputUser.getPassword();
    String name     = inputUser.getName(); // Afh er þetta null?
    // System.out.println(name);
    User user = userRepository.findById(userName).get();
    // System.out.println(exists == null);
    if (user != null || userName != null || password != null) {
      if(BCrypt.checkpw(password, user.getPassword())) {
        session.setAttribute("login", user);
        return "redirect:/user";
      }
    }

    model.addAttribute("error", "Invalid Details");
    return "login";





  }


}
