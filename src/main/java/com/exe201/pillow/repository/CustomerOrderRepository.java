package com.exe201.pillow.repository;

import com.exe201.pillow.domain.CustomerOrder;
import com.exe201.pillow.domain.enumeration.OrderStatus;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    Page<CustomerOrder> findByStatus(OrderStatus status, Pageable pageable);

    Page<CustomerOrder> findByCustomerId(Long customerId, Pageable pageable);

    Page<CustomerOrder> findByOrderDateBetween(Instant startDate, Instant endDate, Pageable pageable);

    Page<CustomerOrder> findByStatusAndCustomerId(OrderStatus status, Long customerId, Pageable pageable);
}
