package project.service;

import project.persistence.entities.Player;

import java.util.List;

public interface PlayerService {

    /**
     * save a {@Link Player}
     * @param player {@Link Player} to be saved
     * @return {@Link Player} that was saved
     */



    Player save(Player player);

    /**
     * Delete {@link Player}
     * @param player {@link Player} to be deleted
     */



    void delete(Player player);

    /**
     * Get all {@link Player}s
     * @return A list of {@link Player}s
     */


    List<Player> findAll();

    /**
     * Get all {@link Player}s in a reverse order
     * @return A reversed list of {@link Player}s
     */


    List<Player> findAllReverseOrder();

    /**
     * Find a {@link Player} based on {@link Long id}
     * @param id {@link Long}
     * @return A {@link Player} with {@link Long id}
     */


    Player findOne(Long id);

    /**
     * Find all {@link Player}s with {@link String name}
     * @param name {@link String}
     * @return All {@link Player}s with the {@link String name} passed
     */
    List<Player> findByName(String name);

    /**
     * find largest {@Link Long id}
     * @return the largest id of {@Link Long id}
     */

    List<Player> findLargestId();


    /**
     * count {@Link Player}s  in team with {@Link Long teamId}
     * @param teamId {@Link Long}
     * @return number of {@Link Player}s with {@Link Long teamId} passed
     */

    List<Player> countPlayersInTeam(Long teamId);


    /**
     * find all {@Link Player}s in Team with {@Link Long teamId}
     * @param teamId {@Link long}
     * @return All {@Link Player}s in Team with {@Link long teamId} passed
     */

    List<Player> findPlayersInTeamReverseOrder(Long teamId);

}


