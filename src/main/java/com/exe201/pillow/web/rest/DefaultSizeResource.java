package com.exe201.pillow.web.rest;

import com.exe201.pillow.domain.DefaultSize;
import com.exe201.pillow.repository.DefaultSizeRepository;
import com.exe201.pillow.service.DefaultSizeService;
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
 * REST controller for managing {@link com.exe201.pillow.domain.DefaultSize}.
 */
@RestController
@RequestMapping("/api/default-sizes")
public class DefaultSizeResource {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultSizeResource.class);

    private static final String ENTITY_NAME = "defaultSize";

    @Value("${jhipster.clientApp.name:pillowshop}")
    private String applicationName;

    private final DefaultSizeService defaultSizeService;

    private final DefaultSizeRepository defaultSizeRepository;

    public DefaultSizeResource(DefaultSizeService defaultSizeService, DefaultSizeRepository defaultSizeRepository) {
        this.defaultSizeService = defaultSizeService;
        this.defaultSizeRepository = defaultSizeRepository;
    }

    /**
     * {@code POST  /default-sizes} : Create a new defaultSize.
     *
     * @param defaultSize the defaultSize to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new defaultSize, or with status {@code 400 (Bad Request)} if the defaultSize has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DefaultSize> createDefaultSize(@Valid @RequestBody DefaultSize defaultSize) throws URISyntaxException {
        LOG.debug("REST request to save DefaultSize : {}", defaultSize);
        if (defaultSize.getId() != null) {
            throw new BadRequestAlertException("A new defaultSize cannot already have an ID", ENTITY_NAME, "idexists");
        }
        defaultSize = defaultSizeService.save(defaultSize);
        return ResponseEntity.created(new URI("/api/default-sizes/" + defaultSize.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, defaultSize.getId().toString()))
            .body(defaultSize);
    }

    /**
     * {@code PUT  /default-sizes/:id} : Updates an existing defaultSize.
     *
     * @param id the id of the defaultSize to save.
     * @param defaultSize the defaultSize to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated defaultSize,
     * or with status {@code 400 (Bad Request)} if the defaultSize is not valid,
     * or with status {@code 500 (Internal Server Error)} if the defaultSize couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DefaultSize> updateDefaultSize(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DefaultSize defaultSize
    ) throws URISyntaxException {
        LOG.debug("REST request to update DefaultSize : {}, {}", id, defaultSize);
        if (defaultSize.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, defaultSize.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!defaultSizeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        defaultSize = defaultSizeService.update(defaultSize);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, defaultSize.getId().toString()))
            .body(defaultSize);
    }

    /**
     * {@code PATCH  /default-sizes/:id} : Partial updates given fields of an existing defaultSize, field will ignore if it is null
     *
     * @param id the id of the defaultSize to save.
     * @param defaultSize the defaultSize to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated defaultSize,
     * or with status {@code 400 (Bad Request)} if the defaultSize is not valid,
     * or with status {@code 404 (Not Found)} if the defaultSize is not found,
     * or with status {@code 500 (Internal Server Error)} if the defaultSize couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DefaultSize> partialUpdateDefaultSize(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DefaultSize defaultSize
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DefaultSize partially : {}, {}", id, defaultSize);
        if (defaultSize.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, defaultSize.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!defaultSizeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DefaultSize> result = defaultSizeService.partialUpdate(defaultSize);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, defaultSize.getId().toString())
        );
    }

    /**
     * {@code GET  /default-sizes} : get all the Default Sizes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Default Sizes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DefaultSize>> getAllDefaultSizes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of DefaultSizes");
        Page<DefaultSize> page = defaultSizeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /default-sizes/:id} : get the "id" defaultSize.
     *
     * @param id the id of the defaultSize to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the defaultSize, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DefaultSize> getDefaultSize(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DefaultSize : {}", id);
        Optional<DefaultSize> defaultSize = defaultSizeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(defaultSize);
    }

    /**
     * {@code DELETE  /default-sizes/:id} : delete the "id" defaultSize.
     *
     * @param id the id of the defaultSize to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDefaultSize(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DefaultSize : {}", id);
        defaultSizeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
