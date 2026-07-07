package com.exe201.pillow.web.rest;

import com.exe201.pillow.domain.Customer;
import com.exe201.pillow.repository.CustomerRepository;
import com.exe201.pillow.service.CustomerService;
import com.exe201.pillow.service.dto.CustomerDTO;
import com.exe201.pillow.service.mapper.CustomerMapper;
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
@RequestMapping("/api/customers")
public class CustomerResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerResource.class);
    private static final String ENTITY_NAME = "customer";

    @Value("${jhipster.clientApp.name:pillowshop}")
    private String applicationName;

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerResource(CustomerService customerService, CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @PostMapping("")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws URISyntaxException {
        LOG.debug("REST request to save Customer : {}", customerDTO);
        if (customerDTO.getId() != null) {
            throw new BadRequestAlertException("A new customer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerService.save(customer);
        CustomerDTO result = customerMapper.toDto(customer);
        return ResponseEntity.created(new URI("/api/customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
        @PathVariable(value = "id") final Long id,
        @Valid @RequestBody CustomerDTO customerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Customer : {}, {}", id, customerDTO);
        if (customerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(customerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!customerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerService.update(customer);
        CustomerDTO result = customerMapper.toDto(customer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerDTO> partialUpdateCustomer(
        @PathVariable(value = "id") final Long id,
        @RequestBody CustomerDTO customerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Customer : {}, {}", id, customerDTO);
        if (customerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(customerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!customerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Customer customer = customerMapper.toEntity(customerDTO);
        Optional<Customer> result = customerService.partialUpdate(customer);
        return ResponseUtil.wrapOrNotFound(
            result.map(customerMapper::toDto),
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Customers");
        Page<Customer> page;
        if (eagerload) {
            page = customerService.findAllWithEagerRelationships(pageable);
        } else {
            page = customerService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(customerMapper.toDto(page.getContent()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Customer : {}", id);
        Optional<Customer> customer = customerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customer.map(customerMapper::toDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Customer : {}", id);
        customerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
