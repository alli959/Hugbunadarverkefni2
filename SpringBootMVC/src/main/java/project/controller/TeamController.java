package project.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import project.persistence.entities.Team;
import project.persistence.entities.User;
import project.persistence.repositories.*;
import project.controller.Toolkit;


@RestController
public class TeamController {

  @Autowired
  private TeamRepository teamRepository;
  @Autowired
  private UserRepository userRepository;

  public List<Team> listOfLongToTeams(List<Long> teamIds) {
    return teamIds
    .stream()
    .map(id -> teamRepository.findById(id).get())
    .collect(Collectors.toList());
  }

  // Done
  @RequestMapping(value = "/user/team", method = RequestMethod.GET)
  public Iterable<Team> createTeamGet(
    @RequestHeader("Authorization") String basicAuthString
  ) {
    String userName = Toolkit.getUserName(basicAuthString);
    User user = userRepository.findById(userName).get();
    return listOfLongToTeams(user.getTeamIds());
  }

  // Done
  @RequestMapping(value = "/user/createTeam", method = RequestMethod.GET)
  public String createTeamPost(
    @RequestHeader("Authorization") String basicAuthString,
    @RequestParam String name
  ) {
    String userName = Toolkit.getUserName(basicAuthString);
    Team team = new Team();
    team.setName(name);
    team.setUserOwner(userName);
    return teamRepository.save(team).getName();
  }

  // Done
  @RequestMapping(value = "/user/team/{teamId}",  method = RequestMethod.GET)
  public Team getOneTeam(
    @PathVariable Long teamId
  ){
    return teamRepository.findById(teamId).get();
  }
}
