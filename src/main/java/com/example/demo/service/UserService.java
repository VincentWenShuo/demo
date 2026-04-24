package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> getUsers() {
		return userRepository.findAll();
	}

	public User createUser(String name, String email) {
		try {
			return userRepository.save(new User(name, email));
		} catch (DataIntegrityViolationException exception) {
			throw new IllegalArgumentException("A user with this email already exists.", exception);
		}
	}
}
