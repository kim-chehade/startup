package com.amigoscode.customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository
        extends JpaRepository<Customer, Integer> {
    boolean existsCustomersByEmail(String email);
    boolean existsCustomersById(Integer id);

}
