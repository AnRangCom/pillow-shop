package com.exe201.pillow.service;

import com.exe201.pillow.domain.DefaultSize;
import com.exe201.pillow.domain.OrderItem;
import com.exe201.pillow.domain.Pillow;
import com.exe201.pillow.domain.enumeration.SizeType;
import com.exe201.pillow.repository.OrderItemRepository;
import com.exe201.pillow.service.exceptions.InsufficientDataException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderItemService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderItemService.class);

    private static final double STANDARD_AREA = 1.0;

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public OrderItem save(OrderItem orderItem) {
        LOG.debug("Request to save OrderItem : {}", orderItem);
        calculatePrice(orderItem);
        return orderItemRepository.save(orderItem);
    }

    public OrderItem update(OrderItem orderItem) {
        LOG.debug("Request to update OrderItem : {}", orderItem);
        calculatePrice(orderItem);
        return orderItemRepository.save(orderItem);
    }

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

    @Transactional(readOnly = true)
    public Page<OrderItem> findAll(Pageable pageable) {
        LOG.debug("Request to get all OrderItems");
        return orderItemRepository.findAll(pageable);
    }

    public Page<OrderItem> findAllWithEagerRelationships(Pageable pageable) {
        return orderItemRepository.findAllWithEagerRelationships(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<OrderItem> findOne(Long id) {
        LOG.debug("Request to get OrderItem : {}", id);
        return orderItemRepository.findOneWithEagerRelationships(id);
    }

    public void delete(Long id) {
        LOG.debug("Request to delete OrderItem : {}", id);
        orderItemRepository.deleteById(id);
    }

    private void calculatePrice(OrderItem orderItem) {
        if (orderItem.getPillow() == null) {
            throw new InsufficientDataException("Pillow is required for order item");
        }

        SizeType sizeType = orderItem.getSizeType();
        if (sizeType == null) {
            throw new InsufficientDataException("SizeType is required");
        }

        Pillow pillow = orderItem.getPillow();
        BigDecimal basePrice = pillow.getBasePrice();

        if (sizeType == SizeType.DEFAULT) {
            if (orderItem.getDefaultSize() == null) {
                throw new InsufficientDataException("DefaultSize is required when SizeType is DEFAULT");
            }
            DefaultSize defaultSize = orderItem.getDefaultSize();
            BigDecimal extraPrice = defaultSize.getExtraPrice();
            orderItem.setPrice(basePrice.add(extraPrice));
        } else {
            if (orderItem.getCustomLength() == null || orderItem.getCustomWidth() == null) {
                throw new InsufficientDataException("Custom length and width are required when SizeType is CUSTOM");
            }
            double area = orderItem.getCustomLength() * orderItem.getCustomWidth();
            BigDecimal areaFactor = BigDecimal.valueOf(area / STANDARD_AREA);
            orderItem.setPrice(basePrice.multiply(areaFactor).setScale(2, RoundingMode.HALF_UP));
        }
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
