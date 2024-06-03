package com.amigoscode.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);

    void insertCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);

//    Optional<Customer> deleteCustomerById(Integer id);
    void deleteCustomerById(Integer id);
    boolean existsPersonWithId(Integer id);

    void updateCustomer(Customer update);


}
