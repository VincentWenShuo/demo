package com.example.demo.controller;

import com.example.demo.model.Store;
import com.example.demo.service.StoreService;
import jakarta.validation.Valid;
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
@RequestMapping("/stores")
public class StoreController {

	private final StoreService storeService;

	public StoreController(StoreService storeService) {
		this.storeService = storeService;
	}

	@GetMapping
	public List<Store> getStores() {
		return storeService.getStores();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Store createStore(@Valid @RequestBody CreateStoreRequest request) {
		return storeService.createStore(request.name(), request.address());
	}

	public record CreateStoreRequest(
		@NotBlank(message = "Name is required.")
		String name,

		@NotBlank(message = "Address is required.")
		String address
	) {
	}
}
