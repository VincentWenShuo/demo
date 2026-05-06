package com.example.demo.service;

import com.example.demo.model.Store;
import com.example.demo.repository.StoreRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

	private final StoreRepository storeRepository;

	public StoreService(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}

	public List<Store> getStores() {
		return storeRepository.findAll();
	}

	public Store createStore(String name, String address) {
		try {
			return storeRepository.save(new Store(name, address));
		} catch (DataIntegrityViolationException exception) {
			throw new IllegalArgumentException("A store with this name already exists.", exception);
		}
	}
}
