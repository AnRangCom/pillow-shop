package com.exe201.pillow.repository;

import com.exe201.pillow.domain.DefaultSize;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DefaultSize entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DefaultSizeRepository extends JpaRepository<DefaultSize, Long> {}
