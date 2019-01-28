package project.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import project.persistence.entities.Game;

import java.util.ArrayList;
import java.util.List;


public interface GameRepository extends JpaRepository<Game, Long> {

    Game save(Game game);

    void delete(Game game);

    List<Game> findAll();

    @Query(value = "Select g FROM Game g WHERE g.playerId = ?1")
    Game findByPlayerId(Long playerId);


    @Query(value = "Select g From Game g WHERE g.isBench = true")
    List<Game> getBench();

    @Query(value = "Select g From Game g WHERE g.isBench = false")
    List<Game> getPlaying();

    @Query(value = "SELECT p.LeftWingThreeHit,p.RightWingThreeHit,p.TopThreeHit,p.LeftCornerThreeHit,p.RightCornerThreeHit FROM Game p WHERE p.playerId = ?1")
    List<Long> getThreeHit(Long playerId);

    @Query(value = "SELECT p.LeftWingThreeMiss,p.RightWingThreeMiss,p.TopThreeMiss,p.LeftCornerThreeMiss,p.RightCornerThreeMiss FROM Game p WHERE p.playerId = ?1")
    List<Long> getThreeMiss(Long playerId);

    @Query(value = "SELECT p.LeftShortCornerHit,p.RightShortCornerHit,p.LeftTopKeyHit,p.RightTopKeyHit,p.TopKeyHit,p.LayUpHit FROM Game p WHERE p.playerId = ?1")
    List<Long> getTwoHit(Long playerId);

    @Query(value = "SELECT p.LeftShortCornerMiss,p.RightShortCornerMiss,p.LeftTopKeyMiss,p.RightTopKeyMiss,p.TopKeyMiss,p.LayUpMiss FROM Game p WHERE p.playerId = ?1")
    List<Long> getTwoMiss(Long playerId);



}
