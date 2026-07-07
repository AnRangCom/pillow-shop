package com.exe201.pillow.service;

import com.exe201.pillow.domain.Pillow;
import com.exe201.pillow.repository.PillowRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.exe201.pillow.domain.Pillow}.
 */
@Service
@Transactional
public class PillowService {

    private static final Logger LOG = LoggerFactory.getLogger(PillowService.class);

    private final PillowRepository pillowRepository;

    public PillowService(PillowRepository pillowRepository) {
        this.pillowRepository = pillowRepository;
    }

    /**
     * Save a pillow.
     *
     * @param pillow the entity to save.
     * @return the persisted entity.
     */
    public Pillow save(Pillow pillow) {
        LOG.debug("Request to save Pillow : {}", pillow);
        return pillowRepository.save(pillow);
    }

    /**
     * Update a pillow.
     *
     * @param pillow the entity to save.
     * @return the persisted entity.
     */
    public Pillow update(Pillow pillow) {
        LOG.debug("Request to update Pillow : {}", pillow);
        return pillowRepository.save(pillow);
    }

    /**
     * Partially update a pillow.
     *
     * @param pillow the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Pillow> partialUpdate(Pillow pillow) {
        LOG.debug("Request to partially update Pillow : {}", pillow);

        return pillowRepository
            .findById(pillow.getId())
            .map(existingPillow -> {
                updateIfPresent(existingPillow::setName, pillow.getName());
                updateIfPresent(existingPillow::setDescription, pillow.getDescription());
                updateIfPresent(existingPillow::setMaterial, pillow.getMaterial());
                updateIfPresent(existingPillow::setBasePrice, pillow.getBasePrice());

                return existingPillow;
            })
            .map(pillowRepository::save);
    }

    /**
     * Get all the pillows.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Pillow> findAll(Pageable pageable) {
        LOG.debug("Request to get all Pillows");
        return pillowRepository.findAll(pageable);
    }

    /**
     * Get one pillow by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Pillow> findOne(Long id) {
        LOG.debug("Request to get Pillow : {}", id);
        return pillowRepository.findById(id);
    }

    /**
     * Delete the pillow by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Pillow : {}", id);
        pillowRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
