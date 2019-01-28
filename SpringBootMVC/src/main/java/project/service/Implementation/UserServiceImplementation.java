package project.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.persistence.entities.Users;
//import project.persistence.repositories.PlayerRepository;
import project.persistence.repositories.UserRepository;
import project.service.UserService;

import java.util.List;

@Service
public class UserServiceImplementation implements UserService{

    UserRepository userRepository;


    @Autowired
    public UserServiceImplementation(UserRepository repository) { this.userRepository = repository;}


    @Override
    public Users save(Users users) {
        return userRepository.save(users);
    }

    @Override
    public void delete(Users users) {

        userRepository.delete(users);

    }

    @Override
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Users getByUserName(String userName) {
        return userRepository.getByUserName(userName);
    }

    //public void register(Users user) {
        // TODO Auto-generated method stub
        //userRepository.register(user);
    //}


    //public Users validateUser(Login login) {
        // TODO Auto-generated method stub

        //return null;
        //return userRepository.validateUser(login);
    //}

}