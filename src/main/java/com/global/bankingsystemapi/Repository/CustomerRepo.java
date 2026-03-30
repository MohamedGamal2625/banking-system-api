package com.global.bankingsystemapi.Repository;

import com.global.bankingsystemapi.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer,Long> {
}
