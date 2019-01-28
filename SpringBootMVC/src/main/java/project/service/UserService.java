package project.service;

import project.persistence.entities.Users;

import java.util.List;

public interface UserService {

    /**
     * save a {@Link Users}
     * @param users {@Link Users} to be saved
     * @return {@Link Users} that was saved
     */



    Users save(Users users);

    /**
     * Delete {@link Users}
     * @param users {@link Users} to be deleted
     */



    void delete(Users users);

    /**
     * Get all {@link Users}s
     * @return A list of {@link Users}s
     */




    List<Users> findAll();

    /**
     * Get all {@link Users}s in a reverse order
     * @return A reversed list of {@link Users}s
     */

    //void register(Users user);

    //Users validateUser(Login login);

    /**
     * get  {@Link Users} by {@String userName}
     * @param userName
     * @return A @Link(Users}
     */


    Users getByUserName(String userName);
}
