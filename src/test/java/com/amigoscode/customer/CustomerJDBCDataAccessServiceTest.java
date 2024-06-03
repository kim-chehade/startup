package com.amigoscode.customer;

import com.amigoscode.AbstractTestcontainers;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {


    private CustomerJDBCDataAccessService underTest;
    private CustomerRowMapper customerRowMapper = new CustomerRowMapper();
    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        //Given
        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);
        //When
        List<Customer> actual = underTest.selectAllCustomers();
        //Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        //Given
        int id = -1;
        //When
        var actual = underTest.selectCustomerById(id);
        //Then
        assertThat(actual).isEmpty();
    }

    @Test
    void selectCustomerById() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void insertCustomer() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When
        var actual = underTest.existsPersonWithId(id);
        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWithId() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20
        );
        underTest.insertCustomer(customer);
        //When
        boolean actual = underTest.existsPersonWithEmail(email);
        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void willReturnFalseWhenIdNotPresent() {
        //Given
        int id = -1;
        //When
        var actual = underTest.existsPersonWithId(id);
        //Then
        assertThat(actual).isFalse();
    }


    @Test
    void existsCustomerWithEmail() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20
        );
        underTest.insertCustomer(customer);
        //When
        boolean actual = underTest.existsPersonWithEmail(email);
        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void whenEmailNotPresent() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        //When
        boolean actual = underTest.existsPersonWithEmail(email);


        //Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20
        );
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //When
        underTest.deleteCustomerById(id);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }


    @Test
    void updateCustomer() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20
        );
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newName = "foo";
        //When
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(
                c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(newName);
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                }
        );
    }

    @Test
    void updateCustomerEmail() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20
        );
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newEmail = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        //When
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(
                c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getEmail()).isEqualTo(newEmail);
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                }
        );
    }

    @Test
    void updateCustomerAge() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20
        );
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newAge = 100;
        //When
        Customer update = new Customer();
        update.setId(id);
        update.setAge( newAge);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(
                c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(c.getAge()).isEqualTo(newAge);
                }
        );
    }
    @Test
    void updateAllPropertiesForCustomer() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20
        );
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        Customer update = new Customer();
        update.setId(id);
        update.setName("foo");
        update.setEmail(UUID.randomUUID().toString());
        update.setAge(22);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValue(update);
    }
        @Test
    void willNotUpdateAllPropertiesForCustomer() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                20
        );
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When update with no changes
        Customer update = new Customer();
        update.setId(id);

        underTest.updateCustomer(update);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(
                    c -> {
                        assertThat(c.getId()).isEqualTo(id);
                        assertThat(c.getName()).isEqualTo(customer.getName());
                        assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                        assertThat(c.getAge()).isEqualTo(customer.getAge());
                    });
    }

}