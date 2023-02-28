package com.cogent.service;

import com.cogent.model.BooleanDao;
import com.cogent.model.UserDao;
import com.cogent.model.UserDto;
import com.cogent.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDao user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}

	public UserDao save(UserDto user) {
		
		boolean userExists = userRepository.existsByUsername(user.getUsername());
		
		if(userExists) {
			return new UserDao();
		}
	
		UserDao newUser = new UserDao();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setName(user.getName());
		newUser.setRole(user.getRole());
		newUser.setEmail(user.getEmail());
		return userRepository.save(newUser);
	}

	public List<UserDao> getAllUsers() {
		
		return userRepository.findAllByRole("user");
	}
	
	public List<UserDao> getAllAdmins() {
		
		return userRepository.findAllByRole("admin");
	}
	
	
	public List<UserDao> getAll() {
		return (List<UserDao>) userRepository.findAll();
	}
	public List<UserDao>getAllExceptMe(String username){
		return userRepository.getAllExceptMe(username);
		
		
	}
	
	public String find(String username) {
		return userRepository.findbyUsername(username);
	}
	
	
	public UserDao getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public UserDao getUserById(int id) {
		return userRepository.findById(id);
	}
	
	public List<UserDao> getUserByName(String name) {
		return userRepository.findByName(name);
	}
	
	public List<UserDao> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public BooleanDao usernameExists(String username) {
		UserDao userExists=getUserByUsername(username);
		if(userExists==null) {
			return new BooleanDao(false);
		}
		return new BooleanDao(true);
	}
	
	
	
	
	
	
}