package net.easyhouse.service;

import net.easyhouse.model.User;
import net.easyhouse.repository.Admin_user_repository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.AotInitializerNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class Admin_user_service {
	
 @Autowired
 private Admin_user_repository userRepository;

 @Autowired
 private EmailSenderService emailSenderService;
 
 @Transactional
 public User updateUser(Integer id, User userDetails) {
 User user = userRepository.findById(id)
 .orElseThrow(() -> new AotInitializerNotFoundException(null, "User not found for this id :: "+ id));
 user.setFirstName(userDetails.getFirstName());
 user.setLastName(userDetails.getLastName());
 user.setUserName(userDetails.getUserName());
 user.setPhone(userDetails.getPhone());
 user.setEmail(userDetails.getEmail());
 user.setUserPassword(userDetails.getUserPassword());
 user.setConfirmPassword(userDetails.getConfirmPassword());
 user.setAddress(userDetails.getAddress());
 user.setUserRole(userDetails.getUserRole());
 User updatedUser = userRepository.save(user);
 
 // Send email notification
 String subject = "Profile Updated";
 String text = "Dear " + user.getFirstName() + ",\n\nYour profile has been updated successfully.\n\nRegards,\nAdmin";
 emailSenderService.sendEmail(user.getEmail(), subject, text);
 return updatedUser;
 }
 
 // Create or update a user
 public User saveUser(User user) {
 return userRepository.save(user);
 }
 
 // Get all users
 public List<User> getAllUsers() {
 return userRepository.findAll();
 }
 
 // Get user by ID
 public Optional<User> getUserById(Integer id) {
 return userRepository.findById(id);
 }
 
 // Delete user by ID
 @Transactional
 public void deleteUserById(Integer id) {
 // Retrieve user details before deletion
 User user = userRepository.findById(id)
 .orElseThrow(() -> new AotInitializerNotFoundException(null, "User not found for this id :: "+ id));
 // Delete user
 userRepository.deleteById(id);
 // Send email notification
 String subject = "Profile Deleted";
 String text = "Dear " + user.getFirstName() + ",\n\nYour profile has been deleted.\n\nRegards,\nAdmin";
 emailSenderService.sendEmail(user.getEmail(), subject, text);
 }
}