package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public List<User> getUsers() {
		return userService.getUsers();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public User createUser(@Valid @RequestBody CreateUserRequest request) {
		return userService.createUser(request.name(), request.email());
	}

	public record CreateUserRequest(
		@NotBlank(message = "Name is required.")
		String name,

		@NotBlank(message = "Email is required.")
		@Email(message = "Email must be valid.")
		String email
	) {
	}
}
