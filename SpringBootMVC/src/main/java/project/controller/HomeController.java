package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import project.service.StringManipulationService;
import org.springframework.web.bind.annotation.RestController;

import project.controller.Toolkit;

/**

    I think this one is redundant as of now since it's only
    to route the user to the frontpage.

**/

@RestController
public class HomeController {

  // Instance Variables
  StringManipulationService stringService;

  // Dependency Injection
  @Autowired
  public HomeController(StringManipulationService stringService) {
    this.stringService = stringService;
  }

  // Request mapping is the path that you want to map this method to
  // In this case, the mapping is the root "/", so when the project
  // is running and you enter "localhost:8080" into a browser, this
  // method is called
  @RequestMapping(value = "/user/whatismyusername", method = RequestMethod.GET)
  public String home(
      @RequestHeader("Authorization") String basicAuthString
  ){
    return Toolkit.getUserName(basicAuthString);
    // The string "Index" that is returned here is the name of the view
    // (the Index.jsp file) that is in the path /main/webapp/WEB-INF/jsp/
    // If you change "Index" to something else, be sure you have a .jsp
    // file that has the same name
  }


}
