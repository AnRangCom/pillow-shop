package com.exe201.pillow.service;

import com.exe201.pillow.domain.OrderItem;
import com.exe201.pillow.repository.OrderItemRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.exe201.pillow.domain.OrderItem}.
 */
@Service
@Transactional
public class OrderItemService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderItemService.class);

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * Save a orderItem.
     *
     * @param orderItem the entity to save.
     * @return the persisted entity.
     */
    public OrderItem save(OrderItem orderItem) {
        LOG.debug("Request to save OrderItem : {}", orderItem);
        return orderItemRepository.save(orderItem);
    }

    /**
     * Update a orderItem.
     *
     * @param orderItem the entity to save.
     * @return the persisted entity.
     */
    public OrderItem update(OrderItem orderItem) {
        LOG.debug("Request to update OrderItem : {}", orderItem);
        return orderItemRepository.save(orderItem);
    }

    /**
     * Partially update a orderItem.
     *
     * @param orderItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderItem> partialUpdate(OrderItem orderItem) {
        LOG.debug("Request to partially update OrderItem : {}", orderItem);

        return orderItemRepository
            .findById(orderItem.getId())
            .map(existingOrderItem -> {
                updateIfPresent(existingOrderItem::setQuantity, orderItem.getQuantity());
                updateIfPresent(existingOrderItem::setSizeType, orderItem.getSizeType());
                updateIfPresent(existingOrderItem::setCustomLength, orderItem.getCustomLength());
                updateIfPresent(existingOrderItem::setCustomWidth, orderItem.getCustomWidth());
                updateIfPresent(existingOrderItem::setPrice, orderItem.getPrice());

                return existingOrderItem;
            })
            .map(orderItemRepository::save);
    }

    /**
     * Get all the orderItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderItem> findAll(Pageable pageable) {
        LOG.debug("Request to get all OrderItems");
        return orderItemRepository.findAll(pageable);
    }

    /**
     * Get all the orderItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OrderItem> findAllWithEagerRelationships(Pageable pageable) {
        return orderItemRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one orderItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderItem> findOne(Long id) {
        LOG.debug("Request to get OrderItem : {}", id);
        return orderItemRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the orderItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderItem : {}", id);
        orderItemRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
