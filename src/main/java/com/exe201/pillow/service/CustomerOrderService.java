package com.exe201.pillow.service;

import com.exe201.pillow.domain.CustomerOrder;
import com.exe201.pillow.repository.CustomerOrderRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.exe201.pillow.domain.CustomerOrder}.
 */
@Service
@Transactional
public class CustomerOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerOrderService.class);

    private final CustomerOrderRepository customerOrderRepository;

    public CustomerOrderService(CustomerOrderRepository customerOrderRepository) {
        this.customerOrderRepository = customerOrderRepository;
    }

    /**
     * Save a customerOrder.
     *
     * @param customerOrder the entity to save.
     * @return the persisted entity.
     */
    public CustomerOrder save(CustomerOrder customerOrder) {
        LOG.debug("Request to save CustomerOrder : {}", customerOrder);
        return customerOrderRepository.save(customerOrder);
    }

    /**
     * Update a customerOrder.
     *
     * @param customerOrder the entity to save.
     * @return the persisted entity.
     */
    public CustomerOrder update(CustomerOrder customerOrder) {
        LOG.debug("Request to update CustomerOrder : {}", customerOrder);
        return customerOrderRepository.save(customerOrder);
    }

    /**
     * Partially update a customerOrder.
     *
     * @param customerOrder the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerOrder> partialUpdate(CustomerOrder customerOrder) {
        LOG.debug("Request to partially update CustomerOrder : {}", customerOrder);

        return customerOrderRepository
            .findById(customerOrder.getId())
            .map(existingCustomerOrder -> {
                updateIfPresent(existingCustomerOrder::setOrderCode, customerOrder.getOrderCode());
                updateIfPresent(existingCustomerOrder::setOrderDate, customerOrder.getOrderDate());
                updateIfPresent(existingCustomerOrder::setStatus, customerOrder.getStatus());
                updateIfPresent(existingCustomerOrder::setTotalAmount, customerOrder.getTotalAmount());

                return existingCustomerOrder;
            })
            .map(customerOrderRepository::save);
    }

    /**
     * Get all the customerOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerOrder> findAll(Pageable pageable) {
        LOG.debug("Request to get all CustomerOrders");
        return customerOrderRepository.findAll(pageable);
    }

    /**
     * Get one customerOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerOrder> findOne(Long id) {
        LOG.debug("Request to get CustomerOrder : {}", id);
        return customerOrderRepository.findById(id);
    }

    /**
     * Delete the customerOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerOrder : {}", id);
        customerOrderRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
