package project.persistence.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.persistence.entities.PlayerStats;

import java.util.List;

public interface PlayerStatsRepository extends JpaRepository<PlayerStats, Long> {

    PlayerStats save(PlayerStats playerstats);

    List<PlayerStats> findAll();


    @Query(value = "SELECT p FROM PlayerStats p WHERE playerId = ?1 ")
    List<PlayerStats> getByPlayerId(Long playerId);

    @Query(value = "SELECT p FROM PlayerStats p WHERE teamId = ?1")
    List<PlayerStats> getByTeamId(Long teamId);

    @Query(value = "SELECT p FROM PlayerStats p WHERE teamId = ?1")
    PlayerStats getEntityByPlayerId(Long playerId);



    
}
