package net.easyhouse.controller;

import net.easyhouse.model.User;
import net.easyhouse.service.Admin_user_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/easyhouse/users") // changed from /users to /easyhouse/users
@CrossOrigin("http://localhost:4200")
public class Admin_user_controller {
	
	@Autowired
	private Admin_user_service userService;
	
	@PostMapping
	public User createUser(@RequestBody User user) {
	 return userService.saveUser(user);
	}
	
	@GetMapping
	public List<User> getAllUsers() {
	 return userService.getAllUsers();
	}
	
	@GetMapping("/{id}")
	public Optional<User> getUserById(@PathVariable Integer id) {
	 return userService.getUserById(id);
	}
	
	@PutMapping("/{id}")
	public User updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
	 return userService.updateUser(id, userDetails);
	}
	
	@DeleteMapping("/{id}")
	public void deleteUserById(@PathVariable Integer id) {
	 userService.deleteUserById(id);
	}

}

