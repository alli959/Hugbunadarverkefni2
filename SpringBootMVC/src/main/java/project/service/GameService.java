package project.service;

import project.persistence.entities.Game;

import java.util.ArrayList;
import java.util.List;

public interface GameService {

    /**
     * save a {@Link Game}
     * @param game {@Link Game} to be saved
     * @return {@Link Game} that was saved
     */



    Game save(Game game);

    /**
     * Delete {@link Game}
     * @param game {@link Game} to be deleted
     */



    void delete(Game game);

    /**
     * Get all {@link Game}s
     * @return A list of {@link Game}s
     */


    List<Game> findAll();

    /**
     * Get all {@link Game}s in a reverse order
     * @return A reversed list of {@link Game}s
     */


    List<Game> findAllReverseOrder();

    /**
     * Find a {@link Game} based on {@link Long id}
     * @param id {@link Long}
     * @return A {@link Game} with {@link Long id}
     */


    Game findOne(Long id);

    /**
     * Find all {@link Game}s with {@link String name}
     * @param name {@link String}
     * @return All {@link Game}s with the {@link String name} passed
     */
    List<Game> findByName(String name);

    /**
     * find largest {@Link Long id}
     * @return the largest id of {@Link Long id}
     */

    List<Game> findLargestId();

    /**
     * find {@Link Game} with {@Link String playerId}
     * @param playerId {@Link Long}
     *
     * @return {@Link Game} with the {@Link Long playerId} passed
     */

    Game findByPlayerId(Long playerId);

    /**
     * find {@Link List<Game>}
     *
     *
     * @return {@Link List<Game>}
     */

    List<Game> getBench();

    /**
     * find {@Link List<Game>}
     *
     *
     * @return {@Link List<Game>}
     */

    List<Game> getPlaying();

    List<Long> getThreeHit(Long playerId);

    List<Long> getTwoHit(Long PlayerId);

    List<Long> getThreeMiss(Long playerId);

    List<Long> getTwoMiss(Long playerId);











}
