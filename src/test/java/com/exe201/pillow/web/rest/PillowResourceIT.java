package com.exe201.pillow.web.rest;

import static com.exe201.pillow.domain.PillowAsserts.*;
import static com.exe201.pillow.web.rest.TestUtil.createUpdateProxyForBean;
import static com.exe201.pillow.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.exe201.pillow.IntegrationTest;
import com.exe201.pillow.domain.Pillow;
import com.exe201.pillow.repository.PillowRepository;
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
 * Integration tests for the {@link PillowResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PillowResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MATERIAL = "AAAAAAAAAA";
    private static final String UPDATED_MATERIAL = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_BASE_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_BASE_PRICE = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/pillows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PillowRepository pillowRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPillowMockMvc;

    private Pillow pillow;

    private Pillow insertedPillow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pillow createEntity() {
        return new Pillow().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).material(DEFAULT_MATERIAL).basePrice(DEFAULT_BASE_PRICE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pillow createUpdatedEntity() {
        return new Pillow().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).material(UPDATED_MATERIAL).basePrice(UPDATED_BASE_PRICE);
    }

    @BeforeEach
    void initTest() {
        pillow = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPillow != null) {
            pillowRepository.delete(insertedPillow);
            insertedPillow = null;
        }
    }

    @Test
    @Transactional
    void createPillow() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pillow
        var returnedPillow = om.readValue(
            restPillowMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pillow)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Pillow.class
        );

        // Validate the Pillow in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPillowUpdatableFieldsEquals(returnedPillow, getPersistedPillow(returnedPillow));

        insertedPillow = returnedPillow;
    }

    @Test
    @Transactional
    void createPillowWithExistingId() throws Exception {
        // Create the Pillow with an existing ID
        pillow.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPillowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pillow)))
            .andExpect(status().isBadRequest());

        // Validate the Pillow in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pillow.setName(null);

        // Create the Pillow, which fails.

        restPillowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pillow)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBasePriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pillow.setBasePrice(null);

        // Create the Pillow, which fails.

        restPillowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pillow)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPillows() throws Exception {
        // Initialize the database
        insertedPillow = pillowRepository.saveAndFlush(pillow);

        // Get all the pillowList
        restPillowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pillow.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].material").value(hasItem(DEFAULT_MATERIAL)))
            .andExpect(jsonPath("$.[*].basePrice").value(hasItem(sameNumber(DEFAULT_BASE_PRICE))));
    }

    @Test
    @Transactional
    void getPillow() throws Exception {
        // Initialize the database
        insertedPillow = pillowRepository.saveAndFlush(pillow);

        // Get the pillow
        restPillowMockMvc
            .perform(get(ENTITY_API_URL_ID, pillow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pillow.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.material").value(DEFAULT_MATERIAL))
            .andExpect(jsonPath("$.basePrice").value(sameNumber(DEFAULT_BASE_PRICE)));
    }

    @Test
    @Transactional
    void getNonExistingPillow() throws Exception {
        // Get the pillow
        restPillowMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPillow() throws Exception {
        // Initialize the database
        insertedPillow = pillowRepository.saveAndFlush(pillow);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pillow
        Pillow updatedPillow = pillowRepository.findById(pillow.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPillow are not directly saved in db
        em.detach(updatedPillow);
        updatedPillow.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).material(UPDATED_MATERIAL).basePrice(UPDATED_BASE_PRICE);

        restPillowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPillow.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPillow))
            )
            .andExpect(status().isOk());

        // Validate the Pillow in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPillowToMatchAllProperties(updatedPillow);
    }

    @Test
    @Transactional
    void putNonExistingPillow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pillow.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPillowMockMvc
            .perform(put(ENTITY_API_URL_ID, pillow.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pillow)))
            .andExpect(status().isBadRequest());

        // Validate the Pillow in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPillow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pillow.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPillowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pillow))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pillow in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPillow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pillow.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPillowMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pillow)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pillow in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePillowWithPatch() throws Exception {
        // Initialize the database
        insertedPillow = pillowRepository.saveAndFlush(pillow);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pillow using partial update
        Pillow partialUpdatedPillow = new Pillow();
        partialUpdatedPillow.setId(pillow.getId());

        partialUpdatedPillow.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).basePrice(UPDATED_BASE_PRICE);

        restPillowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPillow.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPillow))
            )
            .andExpect(status().isOk());

        // Validate the Pillow in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPillowUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPillow, pillow), getPersistedPillow(pillow));
    }

    @Test
    @Transactional
    void fullUpdatePillowWithPatch() throws Exception {
        // Initialize the database
        insertedPillow = pillowRepository.saveAndFlush(pillow);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pillow using partial update
        Pillow partialUpdatedPillow = new Pillow();
        partialUpdatedPillow.setId(pillow.getId());

        partialUpdatedPillow.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).material(UPDATED_MATERIAL).basePrice(UPDATED_BASE_PRICE);

        restPillowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPillow.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPillow))
            )
            .andExpect(status().isOk());

        // Validate the Pillow in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPillowUpdatableFieldsEquals(partialUpdatedPillow, getPersistedPillow(partialUpdatedPillow));
    }

    @Test
    @Transactional
    void patchNonExistingPillow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pillow.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPillowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pillow.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pillow))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pillow in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPillow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pillow.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPillowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pillow))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pillow in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPillow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pillow.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPillowMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pillow)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pillow in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePillow() throws Exception {
        // Initialize the database
        insertedPillow = pillowRepository.saveAndFlush(pillow);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pillow
        restPillowMockMvc
            .perform(delete(ENTITY_API_URL_ID, pillow.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pillowRepository.count();
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

    protected Pillow getPersistedPillow(Pillow pillow) {
        return pillowRepository.findById(pillow.getId()).orElseThrow();
    }

    protected void assertPersistedPillowToMatchAllProperties(Pillow expectedPillow) {
        assertPillowAllPropertiesEquals(expectedPillow, getPersistedPillow(expectedPillow));
    }

    protected void assertPersistedPillowToMatchUpdatableProperties(Pillow expectedPillow) {
        assertPillowAllUpdatablePropertiesEquals(expectedPillow, getPersistedPillow(expectedPillow));
    }
}
