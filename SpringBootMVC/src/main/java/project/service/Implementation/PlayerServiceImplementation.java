package project.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.persistence.entities.Player;
import project.persistence.repositories.PlayerRepository;
import project.service.PlayerService;

import java.util.Collections;
import java.util.List;


@Service
public class PlayerServiceImplementation implements PlayerService {

    PlayerRepository repository;

    @Autowired
    public PlayerServiceImplementation(PlayerRepository repository) { this.repository = repository;}


    @Override
    public Player save(Player player) {
        return repository.save(player);
    }

    @Override
    public void delete(Player player) {

        repository.delete(player);

    }

    @Override
    public List<Player> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Player> findAllReverseOrder() {
        // Get all the Postit notes
        List<Player> player = repository.findAll();

        // Reverse the list
        Collections.reverse(player);

        return player;
    }

    @Override
    public Player findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<Player> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<Player> findLargestId() {
        return repository.findLargestId();
    }

    @Override
    public List<Player> countPlayersInTeam(Long teamId) {
        return repository.countPlayersInTeam(teamId);
    }

    @Override
    public List<Player> findPlayersInTeamReverseOrder(Long teamId) {

        List<Player> player = repository.findPlayersInTeamReverseOrder(teamId);
        Collections.reverse(player);
        return player;
    }
}
