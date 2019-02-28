package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import project.persistence.entities.Team;
import project.persistence.entities.User;
import project.persistence.repositories.*;
import project.controller.Toolkit;

import java.util.List;

@RestController
public class TeamController {

  @Autowired
  private TeamRepository teamRepository;
  @Autowired
  private UserRepository userRepository;

  // Method: localhost:8080/user/team
  // Return: All teams user has as JSON
  @RequestMapping(value = "/user/team", method = RequestMethod.GET)
  public Iterable<Team> getAllTeams(
    @RequestHeader("Authorization") String basicAuthString
  ) {
    String userName = Toolkit.getUserName(basicAuthString);

    User user = userRepository.findById(userName).get();
    return Toolkit.idsToEntities(user.getTeamIds(), teamRepository);
  }

  // Method: localhost:8080/user/createTeam?name=[string]
  // Return: The newly created team as JSON
  @RequestMapping(value = "/user/createTeam", method = RequestMethod.GET)
  public Team createTeamPost(
    @RequestHeader("Authorization") String basicAuthString,
    @RequestParam String name
  ) {
    String userName = Toolkit.getUserName(basicAuthString);
    User user = userRepository.findById(userName).get();
    Team team = new Team();
    team.setName(name);
    team.setUserOwner(userName);
    team = teamRepository.save(team);
    user.addTeamId(team.getId());
    userRepository.save(user);
    return team;
  }

  // Method: localhost:8080/user/team/getOne?teamId=[long]
  // Return: The team with teamId provided as JSON
  @RequestMapping(value = "/user/team/getOne",  method = RequestMethod.GET)
  public Team getOneTeam(
    @RequestParam Long teamId
  ){
    return teamRepository.findById(teamId).get();
  }
}
