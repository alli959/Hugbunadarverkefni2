package project.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.persistence.entities.Player;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long > {
    Player save(Player player);

    void delete(Player player);


    List<Player> findAll();

    // If we need a custom query that maybe doesn't fit the naming convention used by the JPA repository,
    // then we can write it quite easily with the @Query notation, like you see below.
    // This method returns all Players where the length of the name is equal or greater than 3 characters.
    @Query(value = "SELECT p FROM Player p where length(p.name) >= 3 ")
    List<Player> findAllWithNameLongerThan3Chars();

    // Instead of the method findAllReverseOrder() in PlayerService.java,
    // We could have used this method by adding the words
    // ByOrderByIdDesc, which mean: Order By Id in a Descending order
    //
    List<Player> findAllByOrderByIdDesc();

    @Query(value = "SELECT p FROM Player p WHERE p.id = ?1")
    Player findOne(Long id);

    List<Player> findByName(String name);


    @Query(value = "SELECT p.id FROM Player p WHERE p.id = (select MAX(p.id) FROM Player p)")
    List<Player> findLargestId();

    @Query(value = "SELECT COUNT(p.teamId) FROM Player p WHERE p.teamId = ?1")
    List<Player> countPlayersInTeam(Long teamId);


    //It's not in reverse order so It's changed in the PlayerServiceImplementation
    @Query(value = "SELECT p FROM Player p WHERE p.teamId = ?1")
    List<Player> findPlayersInTeamReverseOrder(Long teamId);





}
