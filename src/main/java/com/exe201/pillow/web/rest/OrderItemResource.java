package com.exe201.pillow.web.rest;

import com.exe201.pillow.domain.OrderItem;
import com.exe201.pillow.repository.OrderItemRepository;
import com.exe201.pillow.service.OrderItemService;
import com.exe201.pillow.service.dto.OrderItemDTO;
import com.exe201.pillow.service.mapper.OrderItemMapper;
import com.exe201.pillow.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderItemResource.class);
    private static final String ENTITY_NAME = "orderItem";

    @Value("${jhipster.clientApp.name:pillowshop}")
    private String applicationName;

    private final OrderItemService orderItemService;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    public OrderItemResource(OrderItemService orderItemService, OrderItemRepository orderItemRepository, OrderItemMapper orderItemMapper) {
        this.orderItemService = orderItemService;
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
    }

    @PostMapping("")
    public ResponseEntity<OrderItemDTO> createOrderItem(@Valid @RequestBody OrderItemDTO orderItemDTO) throws URISyntaxException {
        LOG.debug("REST request to save OrderItem : {}", orderItemDTO);
        if (orderItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDTO);
        orderItem = orderItemService.save(orderItem);
        OrderItemDTO result = orderItemMapper.toDto(orderItem);
        return ResponseEntity.created(new URI("/api/order-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(
        @PathVariable(value = "id") final Long id,
        @Valid @RequestBody OrderItemDTO orderItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderItem : {}, {}", id, orderItemDTO);
        if (orderItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(orderItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!orderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDTO);
        orderItem = orderItemService.update(orderItem);
        OrderItemDTO result = orderItemMapper.toDto(orderItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderItemDTO> partialUpdateOrderItem(
        @PathVariable(value = "id") final Long id,
        @RequestBody OrderItemDTO orderItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderItem : {}, {}", id, orderItemDTO);
        if (orderItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(orderItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!orderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDTO);
        Optional<OrderItem> result = orderItemService.partialUpdate(orderItem);
        return ResponseUtil.wrapOrNotFound(
            result.map(orderItemMapper::toDto),
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orderItemDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItems(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of OrderItems");
        Page<OrderItem> page;
        if (eagerload) {
            page = orderItemService.findAllWithEagerRelationships(pageable);
        } else {
            page = orderItemService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(orderItemMapper.toDto(page.getContent()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDTO> getOrderItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderItem : {}", id);
        Optional<OrderItem> orderItem = orderItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderItem.map(orderItemMapper::toDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderItem : {}", id);
        orderItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
