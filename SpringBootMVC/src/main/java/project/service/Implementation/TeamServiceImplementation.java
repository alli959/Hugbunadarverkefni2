package project.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.persistence.entities.Team;
import project.persistence.repositories.TeamRepository;
import project.service.TeamService;

import java.util.*;


@Service
public class TeamServiceImplementation implements TeamService {

    TeamRepository repository;

    @Autowired
    public TeamServiceImplementation(TeamRepository repository) { this.repository = repository;}


    @Override
    public Team save(Team team) {
        return repository.save(team);
    }

    @Override
    public void delete(Team team) {

        repository.delete(team);

    }

    @Override
    public List<Team> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Team> findAllReverseOrder() {
        // Get all the Postit notes
        List<Team> team = repository.findAll();

        // Reverse the list
        Collections.reverse(team);

        return team;
    }

    @Override
    public Team findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<Team> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<Team> findAllReverseOrderOwnedByUser(String userName) {
        List<Team> teams = repository.findAllReverseOrderOwnedByUser(userName);
        Collections.reverse(teams);
        return teams;
    }

}
