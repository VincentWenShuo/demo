package com.example.demo.service;

import com.example.demo.model.Store;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class StoreServiceTest {

	@Autowired
	private StoreService storeService;

	@Test
	void createAndListStores() {
		Store createdStore = storeService.createStore("Main Store", "100 Market Street");

		assertNotNull(createdStore.getId());
		assertEquals("Main Store", createdStore.getName());
		assertEquals("100 Market Street", createdStore.getAddress());

		List<Store> stores = storeService.getStores();

		assertEquals(1, stores.size());
		assertEquals("Main Store", stores.get(0).getName());
		assertEquals("100 Market Street", stores.get(0).getAddress());
	}

	@Test
	void rejectDuplicateName() {
		storeService.createStore("Corner Store", "1 First Street");

		IllegalArgumentException exception = assertThrows(
			IllegalArgumentException.class,
			() -> storeService.createStore("Corner Store", "2 Second Street")
		);

		assertEquals("A store with this name already exists.", exception.getMessage());
	}
}
