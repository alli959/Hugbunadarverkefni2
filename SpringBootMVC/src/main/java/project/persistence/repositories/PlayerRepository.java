

package project.persistence.repositories;

import org.springframework.data.repository.CrudRepository;
import project.persistence.entities.Player;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// // CRUD refers Create, Read, Update, Delete
//
public interface PlayerRepository extends CrudRepository<Player, Long> { }
