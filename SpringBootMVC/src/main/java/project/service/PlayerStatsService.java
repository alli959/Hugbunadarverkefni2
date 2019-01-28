package project.service;

import project.persistence.entities.PlayerStats;

import java.util.List;

public interface PlayerStatsService {

    /**
     * save a {@Link PlayerStats}
     * @param playerStats {@Link PlayerStats} to be saved
     * @return {@Link PlayerStats} that was saved
     */



    PlayerStats save(PlayerStats playerStats);

    /**
     * Delete {@link PlayerStats}
     * @param player {@link PlayerStats} to be deleted
     */



    void delete(PlayerStats player);

    /**
     * Get all {@link PlayerStats}s
     * @return A list of {@link PlayerStats}s
     */


    List<PlayerStats> findAll();

    /**
     * Get all {@link PlayerStats}s in a reverse order
     * @return A reversed list of {@link PlayerStats}s
     */


    List<PlayerStats> findAllReverseOrder();

    /**
     * Find a {@link PlayerStats} based on {@link Long id}
     * @param id {@link Long}
     * @return A {@link PlayerStats} with {@link Long id}
     */


    PlayerStats findOne(Long id);

    /**
     * Find all {@link PlayerStats}s with {@link String name}
     * @param name {@link String}
     * @return All {@link PlayerStats}s with the {@link String name} passed
     */
    List<PlayerStats> findByName(String name);

    /**
     * find largest {@Link Long id}
     * @return the largest id of {@Link Long id}
     */

    List<PlayerStats> findLargestId();


    /**
     * count {@Link PlayerStats}s  in team with {@Link Long teamId}
     * @param teamId {@Link Long}
     * @return number of {@Link PlayerStats}s with {@Link Long teamId} passed
     */

    List<PlayerStats> countPlayersInTeam(Long teamId);


    /**
     * find all {@Link PlayerStats}s in Team with {@Link Long teamId}
     * @param teamId {@Link long}
     * @return All {@Link PlayerStats}s in Team with {@Link long teamId} passed
     */

    List<PlayerStats> findPlayersInTeamReverseOrder(Long teamId);

    List<PlayerStats> getByPlayerId(Long playerId);

    List<PlayerStats> getByTeamId(Long teamId);

    PlayerStats getEntityByPlayerId(Long playerId);



}


