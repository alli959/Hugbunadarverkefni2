package project.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.persistence.entities.Game;
import project.persistence.repositories.GameRepository;
import project.persistence.repositories.PlayerRepository;
import project.service.GameService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class GameServiceImplementation implements  GameService {

    GameRepository repository;

    @Autowired
    public GameServiceImplementation(GameRepository repository){this.repository = repository;}

    @Override
    public Game save(Game game) {
        return repository.save(game);
    }

    @Override
    public void delete(Game game) {
        repository.delete(game);
    }

    @Override
    public List<Game> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Game> findAllReverseOrder() {
        List<Game> game = repository.findAll();

        Collections.reverse(game);

        return game;
    }

    @Override
    public Game findOne(Long id) {
        return null;
    }

    @Override
    public List<Game> findByName(String name) {
        return null;
    }

    @Override
    public List<Game> findLargestId() {
        return null;
    }


    @Override
    public Game findByPlayerId(Long playerId) {
        return repository.findByPlayerId(playerId);
    }

    @Override
    public List<Game> getBench() {
        return repository.getBench();
    }

    @Override
    public List<Game> getPlaying() {
        return repository.getPlaying();
    }

    @Override
    public List<Long> getThreeHit(Long playerId) {
        return repository.getThreeHit(playerId);
    }

    @Override
    public List<Long> getThreeMiss(Long playerId) {
        return repository.getThreeMiss(playerId);
    }

    @Override
    public List<Long> getTwoHit(Long playerId) {
        return repository.getTwoHit(playerId);
    }

    @Override
    public List<Long> getTwoMiss(Long playerId) {
        return repository.getTwoMiss(playerId);
    }



}
