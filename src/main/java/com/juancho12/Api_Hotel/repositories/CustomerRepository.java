package com.juancho12.Api_Hotel.repositories;

import com.juancho12.Api_Hotel.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
