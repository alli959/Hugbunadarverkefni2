package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import project.service.StringManipulationService;
import org.springframework.web.bind.annotation.*;

import project.controller.Toolkit;

/**

    I think this one is redundant as of now since it's only
    to route the user to the frontpage.

**/

@CrossOrigin
@RestController
public class HomeController {


  // Method: localhost:8080/user/whatismyusername
  // Return: Username of the user
  @RequestMapping(value = "/user/whatismyusername", method = RequestMethod.GET)
  public String home(
      @RequestHeader("Authorization") String basicAuthString
  ){
    return Toolkit.getUserName(basicAuthString);
  }


}
