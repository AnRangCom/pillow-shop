package com.exe201.pillow.web.rest;

import com.exe201.pillow.domain.Pillow;
import com.exe201.pillow.repository.PillowRepository;
import com.exe201.pillow.service.PillowService;
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
 * REST controller for managing {@link com.exe201.pillow.domain.Pillow}.
 */
@RestController
@RequestMapping("/api/pillows")
public class PillowResource {

    private static final Logger LOG = LoggerFactory.getLogger(PillowResource.class);

    private static final String ENTITY_NAME = "pillow";

    @Value("${jhipster.clientApp.name:pillowshop}")
    private String applicationName;

    private final PillowService pillowService;

    private final PillowRepository pillowRepository;

    public PillowResource(PillowService pillowService, PillowRepository pillowRepository) {
        this.pillowService = pillowService;
        this.pillowRepository = pillowRepository;
    }

    /**
     * {@code POST  /pillows} : Create a new pillow.
     *
     * @param pillow the pillow to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pillow, or with status {@code 400 (Bad Request)} if the pillow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Pillow> createPillow(@Valid @RequestBody Pillow pillow) throws URISyntaxException {
        LOG.debug("REST request to save Pillow : {}", pillow);
        if (pillow.getId() != null) {
            throw new BadRequestAlertException("A new pillow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pillow = pillowService.save(pillow);
        return ResponseEntity.created(new URI("/api/pillows/" + pillow.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, pillow.getId().toString()))
            .body(pillow);
    }

    /**
     * {@code PUT  /pillows/:id} : Updates an existing pillow.
     *
     * @param id the id of the pillow to save.
     * @param pillow the pillow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pillow,
     * or with status {@code 400 (Bad Request)} if the pillow is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pillow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pillow> updatePillow(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Pillow pillow
    ) throws URISyntaxException {
        LOG.debug("REST request to update Pillow : {}, {}", id, pillow);
        if (pillow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pillow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pillowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pillow = pillowService.update(pillow);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pillow.getId().toString()))
            .body(pillow);
    }

    /**
     * {@code PATCH  /pillows/:id} : Partial updates given fields of an existing pillow, field will ignore if it is null
     *
     * @param id the id of the pillow to save.
     * @param pillow the pillow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pillow,
     * or with status {@code 400 (Bad Request)} if the pillow is not valid,
     * or with status {@code 404 (Not Found)} if the pillow is not found,
     * or with status {@code 500 (Internal Server Error)} if the pillow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pillow> partialUpdatePillow(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Pillow pillow
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Pillow partially : {}, {}", id, pillow);
        if (pillow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pillow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pillowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pillow> result = pillowService.partialUpdate(pillow);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pillow.getId().toString())
        );
    }

    /**
     * {@code GET  /pillows} : get all the Pillows.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Pillows in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Pillow>> getAllPillows(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Pillows");
        Page<Pillow> page = pillowService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pillows/:id} : get the "id" pillow.
     *
     * @param id the id of the pillow to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pillow, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pillow> getPillow(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Pillow : {}", id);
        Optional<Pillow> pillow = pillowService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pillow);
    }

    /**
     * {@code DELETE  /pillows/:id} : delete the "id" pillow.
     *
     * @param id the id of the pillow to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePillow(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Pillow : {}", id);
        pillowService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
