package net.easyhouse.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.easyhouse.model.User;
import net.easyhouse.model.UserRole;
import net.easyhouse.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email is already taken");
        }

        if (userRepository.findByUserName(user.getUserName()) != null) {
            throw new RuntimeException("Username is already taken");
        }

        // Hash the password before saving
        user.setUserPassword(passwordService.hashPassword(user.getUserPassword()));
        user.setConfirmPassword(passwordService.hashPassword(user.getConfirmPassword()));

        return userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
    
    
    // when there are multiple admins in the database
    @PostConstruct
    public void createAdminAccount() {
        List<User> admins = userRepository.findByUserRole(UserRole.ADMIN);
        if (admins == null || admins.isEmpty()) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setUserName("admin");
            admin.setPhone("1234567890");
            admin.setEmail("admin@test.com");
            admin.setAddress("Admin Address");
            admin.setUserRole(UserRole.ADMIN);
            admin.setUserPassword(passwordService.hashPassword("admin"));
            admin.setConfirmPassword(admin.getUserPassword());
            userRepository.save(admin);
        }
    }
    
    // didn't work when there are multiple admins in the database
//    @PostConstruct
//    public void createAdminAccount() {
//        if (userRepository.findByUserRole(UserRole.ADMIN) == null) {
//            User admin = new User();
//            admin.setFirstName("Admin");
//            admin.setLastName("User");
//            admin.setUserName("admin");
//            admin.setPhone("1234567890");
//            admin.setEmail("admin@test.com");
//            admin.setAddress("Admin Address");
//            admin.setUserRole(UserRole.ADMIN);
//            admin.setUserPassword(passwordService.hashPassword("admin"));
//            admin.setConfirmPassword(admin.getUserPassword());
//            userRepository.save(admin);
//        }
//    }
    
    
	public Optional<User> getUserById(Integer id) // buyer
	{
		return userRepository.findById(id);
	}
    
}