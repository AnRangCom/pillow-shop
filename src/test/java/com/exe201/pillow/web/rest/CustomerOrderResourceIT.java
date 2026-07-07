package com.exe201.pillow.web.rest;

import static com.exe201.pillow.domain.CustomerOrderAsserts.*;
import static com.exe201.pillow.web.rest.TestUtil.createUpdateProxyForBean;
import static com.exe201.pillow.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.exe201.pillow.IntegrationTest;
import com.exe201.pillow.domain.CustomerOrder;
import com.exe201.pillow.domain.enumeration.OrderStatus;
import com.exe201.pillow.repository.CustomerOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link CustomerOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerOrderResourceIT {

    private static final String DEFAULT_ORDER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.PENDING;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.CONFIRMED;

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/customer-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerOrderMockMvc;

    private CustomerOrder customerOrder;

    private CustomerOrder insertedCustomerOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerOrder createEntity() {
        return new CustomerOrder()
            .orderCode(DEFAULT_ORDER_CODE)
            .orderDate(DEFAULT_ORDER_DATE)
            .status(DEFAULT_STATUS)
            .totalAmount(DEFAULT_TOTAL_AMOUNT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerOrder createUpdatedEntity() {
        return new CustomerOrder()
            .orderCode(UPDATED_ORDER_CODE)
            .orderDate(UPDATED_ORDER_DATE)
            .status(UPDATED_STATUS)
            .totalAmount(UPDATED_TOTAL_AMOUNT);
    }

    @BeforeEach
    void initTest() {
        customerOrder = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCustomerOrder != null) {
            customerOrderRepository.delete(insertedCustomerOrder);
            insertedCustomerOrder = null;
        }
    }

    @Test
    @Transactional
    void createCustomerOrder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CustomerOrder
        var returnedCustomerOrder = om.readValue(
            restCustomerOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerOrder)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CustomerOrder.class
        );

        // Validate the CustomerOrder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustomerOrderUpdatableFieldsEquals(returnedCustomerOrder, getPersistedCustomerOrder(returnedCustomerOrder));

        insertedCustomerOrder = returnedCustomerOrder;
    }

    @Test
    @Transactional
    void createCustomerOrderWithExistingId() throws Exception {
        // Create the CustomerOrder with an existing ID
        customerOrder.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerOrder)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerOrder.setOrderCode(null);

        // Create the CustomerOrder, which fails.

        restCustomerOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerOrder)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerOrder.setOrderDate(null);

        // Create the CustomerOrder, which fails.

        restCustomerOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerOrder)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerOrder.setStatus(null);

        // Create the CustomerOrder, which fails.

        restCustomerOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerOrder)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customerOrder.setTotalAmount(null);

        // Create the CustomerOrder, which fails.

        restCustomerOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerOrder)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomerOrders() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get all the customerOrderList
        restCustomerOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderCode").value(hasItem(DEFAULT_ORDER_CODE)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))));
    }

    @Test
    @Transactional
    void getCustomerOrder() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        // Get the customerOrder
        restCustomerOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, customerOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerOrder.getId().intValue()))
            .andExpect(jsonPath("$.orderCode").value(DEFAULT_ORDER_CODE))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)));
    }

    @Test
    @Transactional
    void getNonExistingCustomerOrder() throws Exception {
        // Get the customerOrder
        restCustomerOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerOrder() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerOrder
        CustomerOrder updatedCustomerOrder = customerOrderRepository.findById(customerOrder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomerOrder are not directly saved in db
        em.detach(updatedCustomerOrder);
        updatedCustomerOrder
            .orderCode(UPDATED_ORDER_CODE)
            .orderDate(UPDATED_ORDER_DATE)
            .status(UPDATED_STATUS)
            .totalAmount(UPDATED_TOTAL_AMOUNT);

        restCustomerOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomerOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustomerOrder))
            )
            .andExpect(status().isOk());

        // Validate the CustomerOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomerOrderToMatchAllProperties(updatedCustomerOrder);
    }

    @Test
    @Transactional
    void putNonExistingCustomerOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerOrder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customerOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customerOrder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerOrderWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerOrder using partial update
        CustomerOrder partialUpdatedCustomerOrder = new CustomerOrder();
        partialUpdatedCustomerOrder.setId(customerOrder.getId());

        partialUpdatedCustomerOrder.orderCode(UPDATED_ORDER_CODE).orderDate(UPDATED_ORDER_DATE).totalAmount(UPDATED_TOTAL_AMOUNT);

        restCustomerOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerOrder))
            )
            .andExpect(status().isOk());

        // Validate the CustomerOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerOrderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomerOrder, customerOrder),
            getPersistedCustomerOrder(customerOrder)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomerOrderWithPatch() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customerOrder using partial update
        CustomerOrder partialUpdatedCustomerOrder = new CustomerOrder();
        partialUpdatedCustomerOrder.setId(customerOrder.getId());

        partialUpdatedCustomerOrder
            .orderCode(UPDATED_ORDER_CODE)
            .orderDate(UPDATED_ORDER_DATE)
            .status(UPDATED_STATUS)
            .totalAmount(UPDATED_TOTAL_AMOUNT);

        restCustomerOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomerOrder))
            )
            .andExpect(status().isOk());

        // Validate the CustomerOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomerOrderUpdatableFieldsEquals(partialUpdatedCustomerOrder, getPersistedCustomerOrder(partialUpdatedCustomerOrder));
    }

    @Test
    @Transactional
    void patchNonExistingCustomerOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerOrder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customerOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customerOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customerOrder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerOrder() throws Exception {
        // Initialize the database
        insertedCustomerOrder = customerOrderRepository.saveAndFlush(customerOrder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customerOrder
        restCustomerOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customerOrderRepository.count();
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

    protected CustomerOrder getPersistedCustomerOrder(CustomerOrder customerOrder) {
        return customerOrderRepository.findById(customerOrder.getId()).orElseThrow();
    }

    protected void assertPersistedCustomerOrderToMatchAllProperties(CustomerOrder expectedCustomerOrder) {
        assertCustomerOrderAllPropertiesEquals(expectedCustomerOrder, getPersistedCustomerOrder(expectedCustomerOrder));
    }

    protected void assertPersistedCustomerOrderToMatchUpdatableProperties(CustomerOrder expectedCustomerOrder) {
        assertCustomerOrderAllUpdatablePropertiesEquals(expectedCustomerOrder, getPersistedCustomerOrder(expectedCustomerOrder));
    }
}
