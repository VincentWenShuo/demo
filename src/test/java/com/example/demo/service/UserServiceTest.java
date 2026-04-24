package com.example.demo.service;

import com.example.demo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceTest {

	@Autowired
	private UserService userService;

	@Test
	void createAndListUsers() {
		User createdUser = userService.createUser("Alice", "alice@example.com");

		assertNotNull(createdUser.getId());
		assertEquals("Alice", createdUser.getName());
		assertEquals("alice@example.com", createdUser.getEmail());

		List<User> users = userService.getUsers();

		assertEquals(1, users.size());
		assertEquals("Alice", users.get(0).getName());
		assertEquals("alice@example.com", users.get(0).getEmail());
	}

	@Test
	void rejectDuplicateEmail() {
		userService.createUser("Bob", "bob@example.com");

		IllegalArgumentException exception = assertThrows(
			IllegalArgumentException.class,
			() -> userService.createUser("Another Bob", "bob@example.com")
		);

		assertEquals("A user with this email already exists.", exception.getMessage());
	}
}
