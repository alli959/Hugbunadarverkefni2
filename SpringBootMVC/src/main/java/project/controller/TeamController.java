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

  // Done
  @RequestMapping(value = "/user/team", method = RequestMethod.GET)
  public Iterable<Team> createTeamGet(
    @RequestHeader("Authorization") String basicAuthString
  ) {
    String userName = Toolkit.getUserName(basicAuthString);

    User user = userRepository.findById(userName).get();
    return Toolkit.idsToEntities(user.getTeamIds(), teamRepository);
  }

  // Done
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

  // Done
  @RequestMapping(value = "/user/team/{teamId}",  method = RequestMethod.GET)
  public Team getOneTeam(
    @PathVariable Long teamId
  ){
    return teamRepository.findById(teamId).get();
  }
}
