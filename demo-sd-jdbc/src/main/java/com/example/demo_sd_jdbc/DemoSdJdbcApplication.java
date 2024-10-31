package com.example.demo_sd_jdbc;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

@EnableCaching
@SpringBootApplication
public class DemoSdJdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSdJdbcApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(CustomerRepository repository) {
		return args -> {
			var customer = repository.save(new Customer(null, "Brayan",
					new Profile(null, "user", "password"),
					Set.of(new Order(null, "order"))));
			repository.findAll().forEach(System.out::println);
			System.out.println("Getting cached version");
			repository.findById(customer.id());
			System.out.println("Getting cached version version (2)");
			var result = repository.findById(customer.id());
			Assert.state(result.orElseThrow() != customer, "the two references should not be the same");
		};
	}

	@Bean
	ConcurrentMapCacheManager cacheManager() {
		var cacheManager = new ConcurrentMapCacheManager();
		cacheManager.setAllowNullValues(true);
		cacheManager.setStoreByValue(true);
		return cacheManager;
	}
}


@Table("customer_profiles")
record Profile(@Id Integer id, String username, String password) implements Serializable {
}

@Table("customer_orders")
record Order(@Id Integer id, String name) implements Serializable {
}

record Customer(@Id Integer id, String name, Profile profile, Set<Order> orders) implements Serializable {
}

interface CustomerRepository extends ListCrudRepository<Customer, Integer> {

	String CUSTOMER_CACHE_KEY = "customers";

	@Override
	@Cacheable(CUSTOMER_CACHE_KEY)
	Optional<Customer> findById(Integer id);

	@Override
	@CacheEvict(value = CUSTOMER_CACHE_KEY, key = "#result.id")
	<S extends Customer> S save(S entity);

}
