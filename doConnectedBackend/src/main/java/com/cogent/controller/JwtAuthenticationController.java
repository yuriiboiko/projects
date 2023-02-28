package com.cogent.controller;

import com.cogent.config.JwtTokenUtil;
import com.cogent.model.BooleanDao;
import com.cogent.model.JwtRequest;
import com.cogent.model.JwtResponse;
import com.cogent.model.UserDao;
import com.cogent.model.UserDto;
import com.cogent.service.JwtUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins ="http://localhost:4200")
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());


		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	@PostMapping("/register")
	public ResponseEntity<?> saveUser(@RequestBody UserDto user) throws Exception {
		UserDao userReturn=userDetailsService.save(user);
		if(userReturn==null) {
			System.out.println("User already exists");
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(userReturn);
	}

	@PostMapping("/usernameExists/{name}")
	public ResponseEntity<BooleanDao> usernameExists(@PathVariable("name") String name) throws Exception {
		return ResponseEntity.ok(userDetailsService.usernameExists(name));
	}
	
	
	
	private void authenticate(String username, String password) throws Exception {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
