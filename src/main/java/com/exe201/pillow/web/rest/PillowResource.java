package com.exe201.pillow.web.rest;

import com.exe201.pillow.domain.Pillow;
import com.exe201.pillow.repository.PillowRepository;
import com.exe201.pillow.service.PillowService;
import com.exe201.pillow.service.dto.PillowDTO;
import com.exe201.pillow.service.mapper.PillowMapper;
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
@RequestMapping("/api/pillows")
public class PillowResource {

    private static final Logger LOG = LoggerFactory.getLogger(PillowResource.class);
    private static final String ENTITY_NAME = "pillow";

    @Value("${jhipster.clientApp.name:pillowshop}")
    private String applicationName;

    private final PillowService pillowService;
    private final PillowRepository pillowRepository;
    private final PillowMapper pillowMapper;

    public PillowResource(PillowService pillowService, PillowRepository pillowRepository, PillowMapper pillowMapper) {
        this.pillowService = pillowService;
        this.pillowRepository = pillowRepository;
        this.pillowMapper = pillowMapper;
    }

    @PostMapping("")
    public ResponseEntity<PillowDTO> createPillow(@Valid @RequestBody PillowDTO pillowDTO) throws URISyntaxException {
        LOG.debug("REST request to save Pillow : {}", pillowDTO);
        if (pillowDTO.getId() != null) {
            throw new BadRequestAlertException("A new pillow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pillow pillow = pillowMapper.toEntity(pillowDTO);
        pillow = pillowService.save(pillow);
        PillowDTO result = pillowMapper.toDto(pillow);
        return ResponseEntity.created(new URI("/api/pillows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PillowDTO> updatePillow(@PathVariable(value = "id") final Long id, @Valid @RequestBody PillowDTO pillowDTO)
        throws URISyntaxException {
        LOG.debug("REST request to update Pillow : {}, {}", id, pillowDTO);
        if (pillowDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(pillowDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!pillowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Pillow pillow = pillowMapper.toEntity(pillowDTO);
        pillow = pillowService.update(pillow);
        PillowDTO result = pillowMapper.toDto(pillow);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PillowDTO> partialUpdatePillow(@PathVariable(value = "id") final Long id, @RequestBody PillowDTO pillowDTO)
        throws URISyntaxException {
        LOG.debug("REST request to partial update Pillow : {}, {}", id, pillowDTO);
        if (pillowDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(pillowDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!pillowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Pillow pillow = pillowMapper.toEntity(pillowDTO);
        Optional<Pillow> result = pillowService.partialUpdate(pillow);
        return ResponseUtil.wrapOrNotFound(
            result.map(pillowMapper::toDto),
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pillowDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<PillowDTO>> getAllPillows(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Pillows");
        Page<Pillow> page = pillowService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(pillowMapper.toDto(page.getContent()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PillowDTO> getPillow(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Pillow : {}", id);
        Optional<Pillow> pillow = pillowService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pillow.map(pillowMapper::toDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePillow(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Pillow : {}", id);
        pillowService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
