package com.cogent.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cogent.model.UserDao;
import com.cogent.model.UserDto;
import com.cogent.service.JwtUserDetailsService;



@RestController
@CrossOrigin(origins ="http://localhost:4200")
public class UserController {
	
	@Autowired
	private JwtUserDetailsService userDetailsService;

    @GetMapping("/greeting")
    public String getEmployees() {
        return "Welcome!";
    }
    
    @GetMapping("/getAllUsers")
    public List<UserDao> getAllUsers() {
    	return userDetailsService.getAllUsers();
    }
    
    @GetMapping("/getAllAdmins")
    public List<UserDao> getAllAdmins() {
    	return userDetailsService.getAllAdmins();
    }
    
    @GetMapping("/getAll")
    public List<UserDao> getAll() {
    	return userDetailsService.getAll();
    }
    
    @GetMapping("/getUserById/{id}")
    public UserDao getUserById(@PathVariable("id") int id) {
    	return userDetailsService.getUserById(id);
    }
    
    @GetMapping("/getUserByEmail/{email}")
    public List<UserDao> getUserById(@PathVariable("email") String email) {
    	return userDetailsService.getUserByEmail(email);
    }
    
    @GetMapping("/getUserByName/{name}")
    public List<UserDao> getUserByName(@PathVariable("name") String name) {
    	return userDetailsService.getUserByName(name);
    }
    
    @GetMapping("/getUserByUsername/{username}")
    public UserDao getUserByUsername(@PathVariable("username") String username) {
    	System.out.println("Getting user by id: "+username);
    	UserDao user= userDetailsService.getUserByUsername(username);
    	String email= userDetailsService.find(username);
    	System.out.println("email is :"+email);
    	System.out.println("email in object is "+user.getEmail());
    	user.setEmail(email);
    	System.out.println("email in object after manual reset is "+user.getEmail());

    	return user;
    }
    
    
    @GetMapping("/getUserByUsernames/{username}")
    public String getUserByUsernames(@PathVariable("username") String username) {
    	System.out.println("Getting user by id: "+username);
    	return userDetailsService.find(username);
    }
    
    
    
    

    @GetMapping("/getAllBesidesMe/{username}")
    public List<UserDao> getAllBesidesMe(@PathVariable("username") String username) {
    	System.out.println("Getting user by id: "+username);
    	return userDetailsService.getAllExceptMe(username);
    }
    
    
    
    
    
    
    
    
    
}