package project.controller;



import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.persistence.entities.Users;
import project.service.UserService;

import java.util.List;

@Controller
public class RegisterController {

    private UserService userService;

    @Autowired
    public RegisterController(UserService userService){
        this.userService = userService;
    }



    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String createUser(Model model) {
        model.addAttribute("msg", "Please Enter Your Information");

        model.addAttribute("createUser", new Users());

        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String createUserPost(@ModelAttribute("createUser") Users users,
                                 Model model) {

        Users exists = this.userService.getByUserName(users.getName());


        if(exists != null){
            model.addAttribute("error","Users already exists");
            return "/register";
        }
        users.setPassword(BCrypt.hashpw(users.getPassword(), BCrypt.gensalt()));

        userService.save(users);

        model.addAttribute("createUser", new Users());

        return "redirect:login";
    }



}

