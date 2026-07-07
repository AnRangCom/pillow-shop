package com.exe201.pillow.web.rest;

import static com.exe201.pillow.domain.DefaultSizeAsserts.*;
import static com.exe201.pillow.web.rest.TestUtil.createUpdateProxyForBean;
import static com.exe201.pillow.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.exe201.pillow.IntegrationTest;
import com.exe201.pillow.domain.DefaultSize;
import com.exe201.pillow.repository.DefaultSizeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DefaultSizeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DefaultSizeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_LENGTH = 0D;
    private static final Double UPDATED_LENGTH = 1D;

    private static final Double DEFAULT_WIDTH = 0D;
    private static final Double UPDATED_WIDTH = 1D;

    private static final BigDecimal DEFAULT_EXTRA_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_EXTRA_PRICE = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/default-sizes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DefaultSizeRepository defaultSizeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDefaultSizeMockMvc;

    private DefaultSize defaultSize;

    private DefaultSize insertedDefaultSize;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DefaultSize createEntity() {
        return new DefaultSize().name(DEFAULT_NAME).length(DEFAULT_LENGTH).width(DEFAULT_WIDTH).extraPrice(DEFAULT_EXTRA_PRICE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DefaultSize createUpdatedEntity() {
        return new DefaultSize().name(UPDATED_NAME).length(UPDATED_LENGTH).width(UPDATED_WIDTH).extraPrice(UPDATED_EXTRA_PRICE);
    }

    @BeforeEach
    void initTest() {
        defaultSize = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDefaultSize != null) {
            defaultSizeRepository.delete(insertedDefaultSize);
            insertedDefaultSize = null;
        }
    }

    @Test
    @Transactional
    void createDefaultSize() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DefaultSize
        var returnedDefaultSize = om.readValue(
            restDefaultSizeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defaultSize)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DefaultSize.class
        );

        // Validate the DefaultSize in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDefaultSizeUpdatableFieldsEquals(returnedDefaultSize, getPersistedDefaultSize(returnedDefaultSize));

        insertedDefaultSize = returnedDefaultSize;
    }

    @Test
    @Transactional
    void createDefaultSizeWithExistingId() throws Exception {
        // Create the DefaultSize with an existing ID
        defaultSize.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDefaultSizeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defaultSize)))
            .andExpect(status().isBadRequest());

        // Validate the DefaultSize in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        defaultSize.setName(null);

        // Create the DefaultSize, which fails.

        restDefaultSizeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defaultSize)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLengthIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        defaultSize.setLength(null);

        // Create the DefaultSize, which fails.

        restDefaultSizeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defaultSize)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWidthIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        defaultSize.setWidth(null);

        // Create the DefaultSize, which fails.

        restDefaultSizeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defaultSize)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExtraPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        defaultSize.setExtraPrice(null);

        // Create the DefaultSize, which fails.

        restDefaultSizeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defaultSize)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDefaultSizes() throws Exception {
        // Initialize the database
        insertedDefaultSize = defaultSizeRepository.saveAndFlush(defaultSize);

        // Get all the defaultSizeList
        restDefaultSizeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(defaultSize.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH)))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].extraPrice").value(hasItem(sameNumber(DEFAULT_EXTRA_PRICE))));
    }

    @Test
    @Transactional
    void getDefaultSize() throws Exception {
        // Initialize the database
        insertedDefaultSize = defaultSizeRepository.saveAndFlush(defaultSize);

        // Get the defaultSize
        restDefaultSizeMockMvc
            .perform(get(ENTITY_API_URL_ID, defaultSize.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(defaultSize.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.extraPrice").value(sameNumber(DEFAULT_EXTRA_PRICE)));
    }

    @Test
    @Transactional
    void getNonExistingDefaultSize() throws Exception {
        // Get the defaultSize
        restDefaultSizeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDefaultSize() throws Exception {
        // Initialize the database
        insertedDefaultSize = defaultSizeRepository.saveAndFlush(defaultSize);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the defaultSize
        DefaultSize updatedDefaultSize = defaultSizeRepository.findById(defaultSize.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDefaultSize are not directly saved in db
        em.detach(updatedDefaultSize);
        updatedDefaultSize.name(UPDATED_NAME).length(UPDATED_LENGTH).width(UPDATED_WIDTH).extraPrice(UPDATED_EXTRA_PRICE);

        restDefaultSizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDefaultSize.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDefaultSize))
            )
            .andExpect(status().isOk());

        // Validate the DefaultSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDefaultSizeToMatchAllProperties(updatedDefaultSize);
    }

    @Test
    @Transactional
    void putNonExistingDefaultSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defaultSize.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDefaultSizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, defaultSize.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(defaultSize))
            )
            .andExpect(status().isBadRequest());

        // Validate the DefaultSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDefaultSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defaultSize.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDefaultSizeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(defaultSize))
            )
            .andExpect(status().isBadRequest());

        // Validate the DefaultSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDefaultSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defaultSize.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDefaultSizeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(defaultSize)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DefaultSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDefaultSizeWithPatch() throws Exception {
        // Initialize the database
        insertedDefaultSize = defaultSizeRepository.saveAndFlush(defaultSize);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the defaultSize using partial update
        DefaultSize partialUpdatedDefaultSize = new DefaultSize();
        partialUpdatedDefaultSize.setId(defaultSize.getId());

        partialUpdatedDefaultSize.name(UPDATED_NAME).length(UPDATED_LENGTH).extraPrice(UPDATED_EXTRA_PRICE);

        restDefaultSizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDefaultSize.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDefaultSize))
            )
            .andExpect(status().isOk());

        // Validate the DefaultSize in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDefaultSizeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDefaultSize, defaultSize),
            getPersistedDefaultSize(defaultSize)
        );
    }

    @Test
    @Transactional
    void fullUpdateDefaultSizeWithPatch() throws Exception {
        // Initialize the database
        insertedDefaultSize = defaultSizeRepository.saveAndFlush(defaultSize);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the defaultSize using partial update
        DefaultSize partialUpdatedDefaultSize = new DefaultSize();
        partialUpdatedDefaultSize.setId(defaultSize.getId());

        partialUpdatedDefaultSize.name(UPDATED_NAME).length(UPDATED_LENGTH).width(UPDATED_WIDTH).extraPrice(UPDATED_EXTRA_PRICE);

        restDefaultSizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDefaultSize.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDefaultSize))
            )
            .andExpect(status().isOk());

        // Validate the DefaultSize in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDefaultSizeUpdatableFieldsEquals(partialUpdatedDefaultSize, getPersistedDefaultSize(partialUpdatedDefaultSize));
    }

    @Test
    @Transactional
    void patchNonExistingDefaultSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defaultSize.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDefaultSizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, defaultSize.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(defaultSize))
            )
            .andExpect(status().isBadRequest());

        // Validate the DefaultSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDefaultSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defaultSize.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDefaultSizeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(defaultSize))
            )
            .andExpect(status().isBadRequest());

        // Validate the DefaultSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDefaultSize() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        defaultSize.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDefaultSizeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(defaultSize)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DefaultSize in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDefaultSize() throws Exception {
        // Initialize the database
        insertedDefaultSize = defaultSizeRepository.saveAndFlush(defaultSize);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the defaultSize
        restDefaultSizeMockMvc
            .perform(delete(ENTITY_API_URL_ID, defaultSize.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return defaultSizeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected DefaultSize getPersistedDefaultSize(DefaultSize defaultSize) {
        return defaultSizeRepository.findById(defaultSize.getId()).orElseThrow();
    }

    protected void assertPersistedDefaultSizeToMatchAllProperties(DefaultSize expectedDefaultSize) {
        assertDefaultSizeAllPropertiesEquals(expectedDefaultSize, getPersistedDefaultSize(expectedDefaultSize));
    }

    protected void assertPersistedDefaultSizeToMatchUpdatableProperties(DefaultSize expectedDefaultSize) {
        assertDefaultSizeAllUpdatablePropertiesEquals(expectedDefaultSize, getPersistedDefaultSize(expectedDefaultSize));
    }
}
