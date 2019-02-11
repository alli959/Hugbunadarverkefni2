package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import project.persistence.repositories.UserRepository;

/**

    Class is redundant since the authentication is handled in
    the 'security' package, there is still a method to check
    whether a user is logged in.

**/
@RestController
public class LoginController {

  @Autowired
  private UserRepository userRepository;

  // Done
  @RequestMapping(value = "/isLoggedIn", method = RequestMethod.GET)
  public String isLoggedIn() {
    return "true";
  }

}
