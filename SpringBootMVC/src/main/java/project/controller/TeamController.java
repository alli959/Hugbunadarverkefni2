package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.persistence.entities.Team;
import project.persistence.entities.User;
import project.persistence.repositories.TeamRepository;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TeamController {

  @Autowired
  private TeamRepository teamRepository;

  public List<Team> listOfLongToTeams(List<Long> teamIds) {
    return teamIds
    .stream()
    .map(id -> teamRepository.findById(id).get())
    .collect(Collectors.toList());
  }

  @RequestMapping(value = "/user/team", method = RequestMethod.GET)
  public String createTeamGet(HttpSession session, Model model){
    User loggedInUser = (User)session.getAttribute("login");
    if(loggedInUser != null) {
      model.addAttribute("msg", loggedInUser.getName());
      model.addAttribute("createTeam", new Team());
      model.addAttribute("teams", teamRepository.findAll());
      return "team/Team";
    }
    session.setAttribute("error", "User must be logged in!");
    return "redirect:/login";
  }

  @RequestMapping(value = "/user/team", method = RequestMethod.POST)
  public String createTeamPost(
  @ModelAttribute("createTeam")
  Team team,
  HttpSession session,
  Model model
  ) {
    User loggedInUser = (User)session.getAttribute("login");
    if(loggedInUser != null) {
      team.setUserOwner(loggedInUser.getUserName());
      teamRepository.save(team);
      model.addAttribute("msg", loggedInUser.getName());
      model.addAttribute("teams", teamRepository.findAll());
      model.addAttribute("createTeam", new Team());
      return "team/Team";
    }
    session.setAttribute("error", "User must be logged in!");
    return "redirect:/login";
  }

  @RequestMapping(value = "/user/team/{teamId}",  method = RequestMethod.GET)
  public String teamGetFromName(
  @PathVariable
  Long teamId,
  HttpSession session,
  Model model
  ){
    User loggedInUser = (User)session.getAttribute("login");
    if(loggedInUser != null) {
      model.addAttribute("msg", loggedInUser.getName());
      Team team = teamRepository.findById(teamId).get();
      if(!team.getUserOwner().equals(loggedInUser.getUserName())){
        model.addAttribute("Message","Team not owned by User");
        return "Error";
      }
      model.addAttribute("players", team.getPlayers());
      return "team/teamView";
    }
    session.setAttribute("error", "User must be logged in!");
    return "redirect:/login";
  }
}
