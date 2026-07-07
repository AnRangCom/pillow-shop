package com.exe201.pillow.web.rest;

import com.exe201.pillow.domain.CustomerOrder;
import com.exe201.pillow.domain.enumeration.OrderStatus;
import com.exe201.pillow.repository.CustomerOrderRepository;
import com.exe201.pillow.service.CustomerOrderService;
import com.exe201.pillow.service.dto.CustomerOrderDTO;
import com.exe201.pillow.service.mapper.CustomerOrderMapper;
import com.exe201.pillow.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
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
@RequestMapping("/api/customer-orders")
public class CustomerOrderResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerOrderResource.class);
    private static final String ENTITY_NAME = "customerOrder";

    @Value("${jhipster.clientApp.name:pillowshop}")
    private String applicationName;

    private final CustomerOrderService customerOrderService;
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerOrderMapper customerOrderMapper;

    public CustomerOrderResource(
        CustomerOrderService customerOrderService,
        CustomerOrderRepository customerOrderRepository,
        CustomerOrderMapper customerOrderMapper
    ) {
        this.customerOrderService = customerOrderService;
        this.customerOrderRepository = customerOrderRepository;
        this.customerOrderMapper = customerOrderMapper;
    }

    @PostMapping("")
    public ResponseEntity<CustomerOrderDTO> createCustomerOrder(@Valid @RequestBody CustomerOrderDTO customerOrderDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CustomerOrder : {}", customerOrderDTO);
        if (customerOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerOrder customerOrder = customerOrderMapper.toEntity(customerOrderDTO);
        customerOrder = customerOrderService.save(customerOrder);
        CustomerOrderDTO result = customerOrderMapper.toDto(customerOrder);
        return ResponseEntity.created(new URI("/api/customer-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerOrderDTO> updateCustomerOrder(
        @PathVariable(value = "id") final Long id,
        @Valid @RequestBody CustomerOrderDTO customerOrderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerOrder : {}, {}", id, customerOrderDTO);
        if (customerOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(customerOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!customerOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        CustomerOrder customerOrder = customerOrderMapper.toEntity(customerOrderDTO);
        customerOrder = customerOrderService.update(customerOrder);
        CustomerOrderDTO result = customerOrderMapper.toDto(customerOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerOrderDTO> partialUpdateCustomerOrder(
        @PathVariable(value = "id") final Long id,
        @RequestBody CustomerOrderDTO customerOrderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerOrder : {}, {}", id, customerOrderDTO);
        if (customerOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(customerOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!customerOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        CustomerOrder customerOrder = customerOrderMapper.toEntity(customerOrderDTO);
        Optional<CustomerOrder> result = customerOrderService.partialUpdate(customerOrder);
        return ResponseUtil.wrapOrNotFound(
            result.map(customerOrderMapper::toDto),
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerOrderDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<CustomerOrderDTO>> getAllCustomerOrders(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) OrderStatus status,
        @RequestParam(required = false) Long customerId,
        @RequestParam(required = false) Instant startDate,
        @RequestParam(required = false) Instant endDate
    ) {
        LOG.debug("REST request to get a page of CustomerOrders");
        Page<CustomerOrder> page;
        if (status != null && customerId != null) {
            page = customerOrderService.findByStatusAndCustomerId(status, customerId, pageable);
        } else if (status != null) {
            page = customerOrderService.findByStatus(status, pageable);
        } else if (customerId != null) {
            page = customerOrderService.findByCustomerId(customerId, pageable);
        } else if (startDate != null && endDate != null) {
            page = customerOrderService.findByOrderDateBetween(startDate, endDate, pageable);
        } else {
            page = customerOrderService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(customerOrderMapper.toDto(page.getContent()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrderDTO> getCustomerOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerOrder : {}", id);
        Optional<CustomerOrder> customerOrder = customerOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerOrder.map(customerOrderMapper::toDto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CustomerOrderDTO> updateOrderStatus(@PathVariable("id") Long id, @RequestParam OrderStatus status) {
        LOG.debug("REST request to update CustomerOrder status : {}, {}", id, status);
        CustomerOrder customerOrder = customerOrderService.updateOrderStatus(id, status);
        CustomerOrderDTO result = customerOrderMapper.toDto(customerOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomerOrder : {}", id);
        customerOrderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
