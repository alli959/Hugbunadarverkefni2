package project.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.persistence.entities.Team;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long > {
    Team save(Team team);

    void delete(Team team);

    List<Team> findAll();

    // If we need a custom query that maybe doesn't fit the naming convention used by the JPA repository,
    // then we can write it quite easily with the @Query notation, like you see below.
    // This method returns all Teams where the length of the name is equal or greater than 3 characters.
    @Query(value = "SELECT p FROM Team p where length(p.name) >= 3 ")
    List<Team> findAllWithNameLongerThan3Chars();

    // Instead of the method findAllReverseOrder() in TeamService.java,
    // We could have used this method by adding the words
    // ByOrderByIdDesc, which mean: Order By Id in a Descending order
    //
    List<Team> findAllByOrderByIdDesc();

    @Query(value = "SELECT p FROM Team p WHERE p.id = ?1")
    Team findOne(Long id);

    List<Team> findByName(String name);


    @Query(value = "SELECT p.id FROM Team p WHERE p.id = (select MAX(p.id) FROM Team p)")
    List<Team> findLargestId();

    @Query(value = "SELECT p FROM Team p WHERE p.userOwner = ?1")
    List<Team> findAllReverseOrderOwnedByUser(String userName);





}
