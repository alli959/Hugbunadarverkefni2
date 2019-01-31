package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import project.persistence.repositories.UserRepository;

@RestController
public class LoginController {

  @Autowired
  private UserRepository userRepository;

  @RequestMapping(value = "/isLoggedIn", method = RequestMethod.GET)
  public String isLoggedIn() {
    return "true";
  }

}
