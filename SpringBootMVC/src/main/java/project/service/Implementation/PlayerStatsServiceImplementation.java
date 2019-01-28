package project.service.Implementation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.persistence.entities.PlayerStats;
import project.persistence.repositories.PlayerStatsRepository;
import project.service.PlayerStatsService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class PlayerStatsServiceImplementation implements PlayerStatsService {

    PlayerStatsRepository repository;

    @Autowired
    public PlayerStatsServiceImplementation(PlayerStatsRepository repository){ this.repository = repository;}

    @Override
    public PlayerStats save(PlayerStats playerStats) {
        return repository.save(playerStats);
    }

    @Override
    public void delete(PlayerStats player) {

    }

    @Override
    public List<PlayerStats> findAll() {
        return repository.findAll();
    }

    @Override
    public List<PlayerStats> findAllReverseOrder() {
        // Get all the Postit notes
        List<PlayerStats> player = repository.findAll();

        // Reverse the list
        Collections.reverse(player);

        return player;
    }

    @Override
    public PlayerStats findOne(Long id) {
        return null;
    }

    @Override
    public List<PlayerStats> findByName(String name) {
        return null;
    }

    @Override
    public List<PlayerStats> findLargestId() {
        return null;
    }

    @Override
    public List<PlayerStats> countPlayersInTeam(Long teamId) {
        return null;
    }

    @Override
    public List<PlayerStats> findPlayersInTeamReverseOrder(Long teamId) {
        return null;
    }

    @Override
    public List<PlayerStats> getByPlayerId(Long playerId) {
        return repository.getByPlayerId(playerId);
    }

    @Override
    public List<PlayerStats> getByTeamId(Long teamId) {
        List<PlayerStats> players = repository.getByTeamId(teamId);
        Collections.reverse(players);
        return players;
    }

    @Override
    public PlayerStats getEntityByPlayerId(Long playerId) {
        return repository.getEntityByPlayerId(playerId);
    }
}
