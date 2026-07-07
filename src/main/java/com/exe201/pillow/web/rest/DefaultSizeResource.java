package com.exe201.pillow.web.rest;

import com.exe201.pillow.domain.DefaultSize;
import com.exe201.pillow.repository.DefaultSizeRepository;
import com.exe201.pillow.service.DefaultSizeService;
import com.exe201.pillow.service.dto.DefaultSizeDTO;
import com.exe201.pillow.service.mapper.DefaultSizeMapper;
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
@RequestMapping("/api/default-sizes")
public class DefaultSizeResource {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultSizeResource.class);
    private static final String ENTITY_NAME = "defaultSize";

    @Value("${jhipster.clientApp.name:pillowshop}")
    private String applicationName;

    private final DefaultSizeService defaultSizeService;
    private final DefaultSizeRepository defaultSizeRepository;
    private final DefaultSizeMapper defaultSizeMapper;

    public DefaultSizeResource(
        DefaultSizeService defaultSizeService,
        DefaultSizeRepository defaultSizeRepository,
        DefaultSizeMapper defaultSizeMapper
    ) {
        this.defaultSizeService = defaultSizeService;
        this.defaultSizeRepository = defaultSizeRepository;
        this.defaultSizeMapper = defaultSizeMapper;
    }

    @PostMapping("")
    public ResponseEntity<DefaultSizeDTO> createDefaultSize(@Valid @RequestBody DefaultSizeDTO defaultSizeDTO) throws URISyntaxException {
        LOG.debug("REST request to save DefaultSize : {}", defaultSizeDTO);
        if (defaultSizeDTO.getId() != null) {
            throw new BadRequestAlertException("A new defaultSize cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DefaultSize defaultSize = defaultSizeMapper.toEntity(defaultSizeDTO);
        defaultSize = defaultSizeService.save(defaultSize);
        DefaultSizeDTO result = defaultSizeMapper.toDto(defaultSize);
        return ResponseEntity.created(new URI("/api/default-sizes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DefaultSizeDTO> updateDefaultSize(
        @PathVariable(value = "id") final Long id,
        @Valid @RequestBody DefaultSizeDTO defaultSizeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DefaultSize : {}, {}", id, defaultSizeDTO);
        if (defaultSizeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(defaultSizeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!defaultSizeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        DefaultSize defaultSize = defaultSizeMapper.toEntity(defaultSizeDTO);
        defaultSize = defaultSizeService.update(defaultSize);
        DefaultSizeDTO result = defaultSizeMapper.toDto(defaultSize);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DefaultSizeDTO> partialUpdateDefaultSize(
        @PathVariable(value = "id") final Long id,
        @RequestBody DefaultSizeDTO defaultSizeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DefaultSize : {}, {}", id, defaultSizeDTO);
        if (defaultSizeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(defaultSizeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!defaultSizeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        DefaultSize defaultSize = defaultSizeMapper.toEntity(defaultSizeDTO);
        Optional<DefaultSize> result = defaultSizeService.partialUpdate(defaultSize);
        return ResponseUtil.wrapOrNotFound(
            result.map(defaultSizeMapper::toDto),
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, defaultSizeDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<DefaultSizeDTO>> getAllDefaultSizes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of DefaultSizes");
        Page<DefaultSize> page = defaultSizeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(defaultSizeMapper.toDto(page.getContent()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DefaultSizeDTO> getDefaultSize(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DefaultSize : {}", id);
        Optional<DefaultSize> defaultSize = defaultSizeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(defaultSize.map(defaultSizeMapper::toDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDefaultSize(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DefaultSize : {}", id);
        defaultSizeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
