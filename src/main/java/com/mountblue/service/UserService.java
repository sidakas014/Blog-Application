package com.mountblue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.mountblue.repository.UserRepository;
import com.mountblue.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;


	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public boolean saveUser(User user) {
		Optional<User> newUser = userRepository.findByEmail(user.getEmail());
		if (newUser.isEmpty()) {
			if (user.getConfirmPassword().equals(user.getPassword())) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				userRepository.save(user);
				return true;
			}
		}
		return false;
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUserById(int userId) {
		return userRepository.findById(userId).get();
	}
}


