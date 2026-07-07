package com.exe201.pillow.service;

import com.exe201.pillow.domain.DefaultSize;
import com.exe201.pillow.repository.DefaultSizeRepository;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.exe201.pillow.domain.DefaultSize}.
 */
@Service
@Transactional
public class DefaultSizeService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultSizeService.class);

    private final DefaultSizeRepository defaultSizeRepository;

    public DefaultSizeService(DefaultSizeRepository defaultSizeRepository) {
        this.defaultSizeRepository = defaultSizeRepository;
    }

    /**
     * Save a defaultSize.
     *
     * @param defaultSize the entity to save.
     * @return the persisted entity.
     */
    public DefaultSize save(DefaultSize defaultSize) {
        LOG.debug("Request to save DefaultSize : {}", defaultSize);
        return defaultSizeRepository.save(defaultSize);
    }

    /**
     * Update a defaultSize.
     *
     * @param defaultSize the entity to save.
     * @return the persisted entity.
     */
    public DefaultSize update(DefaultSize defaultSize) {
        LOG.debug("Request to update DefaultSize : {}", defaultSize);
        return defaultSizeRepository.save(defaultSize);
    }

    /**
     * Partially update a defaultSize.
     *
     * @param defaultSize the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DefaultSize> partialUpdate(DefaultSize defaultSize) {
        LOG.debug("Request to partially update DefaultSize : {}", defaultSize);

        return defaultSizeRepository
            .findById(defaultSize.getId())
            .map(existingDefaultSize -> {
                updateIfPresent(existingDefaultSize::setName, defaultSize.getName());
                updateIfPresent(existingDefaultSize::setLength, defaultSize.getLength());
                updateIfPresent(existingDefaultSize::setWidth, defaultSize.getWidth());
                updateIfPresent(existingDefaultSize::setExtraPrice, defaultSize.getExtraPrice());

                return existingDefaultSize;
            })
            .map(defaultSizeRepository::save);
    }

    /**
     * Get all the defaultSizes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DefaultSize> findAll(Pageable pageable) {
        LOG.debug("Request to get all DefaultSizes");
        return defaultSizeRepository.findAll(pageable);
    }

    /**
     * Get one defaultSize by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DefaultSize> findOne(Long id) {
        LOG.debug("Request to get DefaultSize : {}", id);
        return defaultSizeRepository.findById(id);
    }

    /**
     * Delete the defaultSize by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DefaultSize : {}", id);
        defaultSizeRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
