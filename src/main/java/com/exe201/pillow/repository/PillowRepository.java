package com.exe201.pillow.repository;

import com.exe201.pillow.domain.Pillow;
import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface PillowRepository extends JpaRepository<Pillow, Long> {
    Page<Pillow> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Pillow> findByBasePriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}
