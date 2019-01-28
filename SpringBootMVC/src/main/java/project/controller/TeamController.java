package project.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.persistence.entities.Player;
import project.persistence.entities.Team;
import project.persistence.entities.Users;
import project.service.TeamService;
import project.service.PlayerService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TeamController {

    private TeamService teamService;
    private PlayerService playerService;

    @Autowired
    public TeamController(TeamService teamService, PlayerService playerService){
        this.teamService = teamService;
        this.playerService = playerService;
    }




    @RequestMapping(value = "/user/team", method = RequestMethod.GET)

    public String createTeamGet(HttpSession session, Model model){
        Users loggedInUser = (Users)session.getAttribute("login");

        if(loggedInUser != null) {

            model.addAttribute("msg", loggedInUser.getName());

            model.addAttribute("createTeam", new Team());


            model.addAttribute("teams", teamService.findAllReverseOrderOwnedByUser(loggedInUser.getUserName()));

            return "team/Team";
        }
        session.setAttribute("error", "User must be logged in!");

        return "redirect:/login";
    }




    @RequestMapping(value = "/user/team", method = RequestMethod.POST)
    public String createTeamPost(@ModelAttribute("createTeam") Team team,
                                 HttpSession session,
                                 Model model) {

        Users loggedInUser = (Users)session.getAttribute("login");
        if(loggedInUser != null) {
            team.setUserOwner(loggedInUser.getUserName());
            teamService.save(team);

            model.addAttribute("msg", loggedInUser.getName());



            model.addAttribute("teams", teamService.findAllReverseOrderOwnedByUser(loggedInUser.getUserName()));

            model.addAttribute("createTeam", new Team());


            return "team/Team";
        }
        session.setAttribute("error", "User must be logged in!");

        return "redirect:/login";


    }




    @RequestMapping(value = "/user/team/{teamId}",  method = RequestMethod.GET)
    public String teamGetFromName(@PathVariable Long teamId,
                                    HttpSession session,
                                    Model model){

        Users loggedInUser = (Users)session.getAttribute("login");
        if(loggedInUser != null) {

            model.addAttribute("msg", loggedInUser.getName());

            Team team = teamService.findOne(teamId);

            if(!team.getUserOwner().equals(loggedInUser.getUserName())){
                model.addAttribute("Message","Team not owned by User");
                return "Error";
            }


            model.addAttribute("players", playerService.findPlayersInTeamReverseOrder(teamId));


            return "team/teamView";
        }
        session.setAttribute("error", "User must be logged in!");


        return "redirect:/login";

    }

}
