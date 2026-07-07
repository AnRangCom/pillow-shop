package com.exe201.pillow.service;

import com.exe201.pillow.domain.CustomerOrder;
import com.exe201.pillow.domain.OrderItem;
import com.exe201.pillow.domain.enumeration.OrderStatus;
import com.exe201.pillow.repository.CustomerOrderRepository;
import com.exe201.pillow.service.exceptions.InvalidOrderStatusException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerOrderService.class);

    private final CustomerOrderRepository customerOrderRepository;

    public CustomerOrderService(CustomerOrderRepository customerOrderRepository) {
        this.customerOrderRepository = customerOrderRepository;
    }

    public CustomerOrder save(CustomerOrder customerOrder) {
        LOG.debug("Request to save CustomerOrder : {}", customerOrder);
        if (customerOrder.getOrderCode() == null || customerOrder.getOrderCode().isBlank()) {
            customerOrder.setOrderCode(generateOrderCode());
        }
        if (customerOrder.getOrderDate() == null) {
            customerOrder.setOrderDate(Instant.now());
        }
        if (customerOrder.getStatus() == null) {
            customerOrder.setStatus(OrderStatus.PENDING);
        }
        return customerOrderRepository.save(customerOrder);
    }

    public CustomerOrder update(CustomerOrder customerOrder) {
        LOG.debug("Request to update CustomerOrder : {}", customerOrder);
        return customerOrderRepository.save(customerOrder);
    }

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

    @Transactional(readOnly = true)
    public Page<CustomerOrder> findAll(Pageable pageable) {
        LOG.debug("Request to get all CustomerOrders");
        return customerOrderRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<CustomerOrder> findOne(Long id) {
        LOG.debug("Request to get CustomerOrder : {}", id);
        return customerOrderRepository.findById(id);
    }

    public void delete(Long id) {
        LOG.debug("Request to delete CustomerOrder : {}", id);
        CustomerOrder order = customerOrderRepository.findById(id).orElseThrow(() -> new InvalidOrderStatusException("Order not found"));
        if (order.getStatus() == OrderStatus.CONFIRMED || order.getStatus() == OrderStatus.SHIPPED) {
            throw new InvalidOrderStatusException("Cannot delete order with status: " + order.getStatus());
        }
        customerOrderRepository.deleteById(id);
    }

    public CustomerOrder updateOrderStatus(Long orderId, OrderStatus newStatus) {
        LOG.debug("Request to update CustomerOrder status : {}, {}", orderId, newStatus);
        CustomerOrder order = customerOrderRepository
            .findById(orderId)
            .orElseThrow(() -> new InvalidOrderStatusException("Order not found"));

        OrderStatus currentStatus = order.getStatus();
        validateStatusTransition(currentStatus, newStatus);

        order.setStatus(newStatus);
        return customerOrderRepository.save(order);
    }

    public void calculateTotalAmount(Long orderId) {
        LOG.debug("Request to calculate total amount for CustomerOrder : {}", orderId);
        CustomerOrder order = customerOrderRepository
            .findById(orderId)
            .orElseThrow(() -> new InvalidOrderStatusException("Order not found"));

        Set<OrderItem> items = order.getOrderItems();
        BigDecimal total = items
            .stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);
        customerOrderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Page<CustomerOrder> findByStatus(OrderStatus status, Pageable pageable) {
        LOG.debug("Request to get CustomerOrders by status : {}", status);
        return customerOrderRepository.findByStatus(status, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CustomerOrder> findByCustomerId(Long customerId, Pageable pageable) {
        LOG.debug("Request to get CustomerOrders by customerId : {}", customerId);
        return customerOrderRepository.findByCustomerId(customerId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CustomerOrder> findByOrderDateBetween(Instant startDate, Instant endDate, Pageable pageable) {
        LOG.debug("Request to get CustomerOrders between : {} and {}", startDate, endDate);
        return customerOrderRepository.findByOrderDateBetween(startDate, endDate, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CustomerOrder> findByStatusAndCustomerId(OrderStatus status, Long customerId, Pageable pageable) {
        LOG.debug("Request to get CustomerOrders by status : {} and customerId : {}", status, customerId);
        return customerOrderRepository.findByStatusAndCustomerId(status, customerId, pageable);
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus next) {
        boolean valid = switch (current) {
            case PENDING -> next == OrderStatus.CONFIRMED || next == OrderStatus.CANCELLED;
            case CONFIRMED -> next == OrderStatus.SHIPPED || next == OrderStatus.CANCELLED;
            case SHIPPED -> next == OrderStatus.DELIVERED;
            case DELIVERED, CANCELLED -> false;
        };
        if (!valid) {
            throw new InvalidOrderStatusException("Cannot transition from " + current + " to " + next);
        }
    }

    private String generateOrderCode() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "ORD-" + timestamp + "-" + random;
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
