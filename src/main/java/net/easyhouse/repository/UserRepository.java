package net.easyhouse.repository;

import java.util.List;
//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.easyhouse.model.User;
import net.easyhouse.model.UserRole;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findByUserName(String userName);
//    User findByUserRole(UserRole userRole);
    // added because didn't work for multiple admin roles
    List<User> findByUserRole(UserRole userRole);
    User getBuyerById(Integer id);
}