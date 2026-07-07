package com.exe201.pillow.repository;

import com.exe201.pillow.domain.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    default Optional<Customer> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Customer> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Customer> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select customer from Customer customer left join fetch customer.user",
        countQuery = "select count(customer) from Customer customer"
    )
    Page<Customer> findAllWithToOneRelationships(Pageable pageable);

    @Query("select customer from Customer customer left join fetch customer.user")
    List<Customer> findAllWithToOneRelationships();

    @Query("select customer from Customer customer left join fetch customer.user where customer.id =:id")
    Optional<Customer> findOneWithToOneRelationships(@Param("id") Long id);

    Optional<Customer> findByEmail(String email);

    Page<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);
}
