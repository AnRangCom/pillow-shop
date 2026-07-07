package com.exe201.pillow.repository;

import com.exe201.pillow.domain.Pillow;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pillow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PillowRepository extends JpaRepository<Pillow, Long> {}
