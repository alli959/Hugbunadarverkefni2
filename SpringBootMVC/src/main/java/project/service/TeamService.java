package project.service;

import project.persistence.entities.Team;

import java.util.List;

public interface TeamService {

    /**
     * save a {@Link Team}
     *
     * @param team {@Link Team} to be saved
     * @return {@Link Team} that was saved
     */


    Team save(Team team);

    /**
     * Delete {@link Team}
     *
     * @param team {@link Team} to be deleted
     */


    void delete(Team team);

    /**
     * Get all {@link Team}s
     *
     * @return A list of {@link Team}s
     */


    List<Team> findAll();

    /**
     * Get all {@link Team}s in a reverse order
     *
     * @return A reversed list of {@link Team}s
     */


    List<Team> findAllReverseOrder();

    /**
     * Find a {@link Team} based on {@link Long id}
     *
     * @param id {@link Long}
     * @return A {@link Team} with {@link Long id}
     */



    Team findOne(Long id);

    /**
     * Find all {@link Team}s with {@link String name}
     *
     * @param name {@link String}
     * @return All {@link Team}s with the {@link String name} passed
     */
    List<Team> findByName(String name);



    /**
     * get all {@Link Team}s owned by {@Link String userName}
     *
     * @param userName {@Link String}
     * @return A list of {@Link Team by userId}
     */
    List<Team> findAllReverseOrderOwnedByUser(String userName);


}


