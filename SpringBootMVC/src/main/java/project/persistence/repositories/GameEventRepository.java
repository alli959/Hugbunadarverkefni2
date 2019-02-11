

package project.persistence.repositories;

import org.springframework.data.repository.CrudRepository;
import project.persistence.entities.GameEvent;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// // CRUD refers Create, Read, Update, Delete
//
public interface GameEventRepository extends CrudRepository<GameEvent, Long> { }
