package project.persistence.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.persistence.entities.Users;


public interface UserRepository extends JpaRepository<Users, Long > {

    Users save(Users users);

    void delete(Users users);

    List<Users> findAll();

    @Query(value = "SELECT p FROM Users p WHERE p.userName = ?1")
    Users getByUserName(String userName);

    //void register(Users user);

//    Users validateUser(Login login);

    //@Query(value = "SELECT p FROM Player p where length(p.name) >= 3 ")
    //List<Player> findAllWithNameLongerThan3Chars();


    //public void register(Users user) {
      //  String sql = "insert into users values(?,?,?,?,?,?,?)";
        //jdbcTemplate.update(sql, new Object[] { user.getUsername(), user.getPassword(), user.getFirstname(),
          //      user.getLastname(), user.getEmail(), user.getAddress(), user.getPhone() });
   // }
   // public Users validateUser(Login login) {
     //   String sql = "select * from users where username='" + login.getUsername() + "' and password='" + login.getPassword()
      //          + "'";
    //    List<Users> users = jdbcTemplate.query(sql, new UserMapper());
    //    return users.size() > 0 ? users.get(0) : null;
   // }
}
