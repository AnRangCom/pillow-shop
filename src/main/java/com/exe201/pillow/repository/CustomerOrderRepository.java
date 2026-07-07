package com.exe201.pillow.repository;

import com.exe201.pillow.domain.CustomerOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomerOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {}
