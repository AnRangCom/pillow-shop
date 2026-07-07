package com.exe201.pillow.web.rest;

import com.exe201.pillow.domain.CustomerOrder;
import com.exe201.pillow.repository.CustomerOrderRepository;
import com.exe201.pillow.service.CustomerOrderService;
import com.exe201.pillow.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
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

/**
 * REST controller for managing {@link com.exe201.pillow.domain.CustomerOrder}.
 */
@RestController
@RequestMapping("/api/customer-orders")
public class CustomerOrderResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerOrderResource.class);

    private static final String ENTITY_NAME = "customerOrder";

    @Value("${jhipster.clientApp.name:pillowshop}")
    private String applicationName;

    private final CustomerOrderService customerOrderService;

    private final CustomerOrderRepository customerOrderRepository;

    public CustomerOrderResource(CustomerOrderService customerOrderService, CustomerOrderRepository customerOrderRepository) {
        this.customerOrderService = customerOrderService;
        this.customerOrderRepository = customerOrderRepository;
    }

    /**
     * {@code POST  /customer-orders} : Create a new customerOrder.
     *
     * @param customerOrder the customerOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerOrder, or with status {@code 400 (Bad Request)} if the customerOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerOrder> createCustomerOrder(@Valid @RequestBody CustomerOrder customerOrder) throws URISyntaxException {
        LOG.debug("REST request to save CustomerOrder : {}", customerOrder);
        if (customerOrder.getId() != null) {
            throw new BadRequestAlertException("A new customerOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerOrder = customerOrderService.save(customerOrder);
        return ResponseEntity.created(new URI("/api/customer-orders/" + customerOrder.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, customerOrder.getId().toString()))
            .body(customerOrder);
    }

    /**
     * {@code PUT  /customer-orders/:id} : Updates an existing customerOrder.
     *
     * @param id the id of the customerOrder to save.
     * @param customerOrder the customerOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerOrder,
     * or with status {@code 400 (Bad Request)} if the customerOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerOrder> updateCustomerOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerOrder customerOrder
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerOrder : {}, {}", id, customerOrder);
        if (customerOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customerOrder = customerOrderService.update(customerOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerOrder.getId().toString()))
            .body(customerOrder);
    }

    /**
     * {@code PATCH  /customer-orders/:id} : Partial updates given fields of an existing customerOrder, field will ignore if it is null
     *
     * @param id the id of the customerOrder to save.
     * @param customerOrder the customerOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerOrder,
     * or with status {@code 400 (Bad Request)} if the customerOrder is not valid,
     * or with status {@code 404 (Not Found)} if the customerOrder is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerOrder> partialUpdateCustomerOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerOrder customerOrder
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerOrder partially : {}, {}", id, customerOrder);
        if (customerOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerOrder> result = customerOrderService.partialUpdate(customerOrder);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerOrder.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-orders} : get all the Customer Orders.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Customer Orders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomerOrder>> getAllCustomerOrders(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of CustomerOrders");
        Page<CustomerOrder> page = customerOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-orders/:id} : get the "id" customerOrder.
     *
     * @param id the id of the customerOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrder> getCustomerOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerOrder : {}", id);
        Optional<CustomerOrder> customerOrder = customerOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerOrder);
    }

    /**
     * {@code DELETE  /customer-orders/:id} : delete the "id" customerOrder.
     *
     * @param id the id of the customerOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomerOrder : {}", id);
        customerOrderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
