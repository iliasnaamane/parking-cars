package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.Slot;
import com.mycompany.myapp.repository.SlotRepository;
import com.mycompany.myapp.service.SlotService;
import com.mycompany.myapp.service.dto.SlotDTO;
import com.mycompany.myapp.service.mapper.SlotMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.Type;
import com.mycompany.myapp.domain.enumeration.Status;
/**
 * Integration tests for the {@link SlotResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class SlotResourceIT {

    private static final Type DEFAULT_TYPE = Type.Gasoline;
    private static final Type UPDATED_TYPE = Type.Elec;

    private static final Integer DEFAULT_ELEC_ENERGY = 20;
    private static final Integer UPDATED_ELEC_ENERGY = 21;

    private static final Status DEFAULT_STATUS = Status.Free;
    private static final Status UPDATED_STATUS = Status.Full;

    private static final Float DEFAULT_FIXED_AMOUNT = 1F;
    private static final Float UPDATED_FIXED_AMOUNT = 2F;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private SlotMapper slotMapper;

    @Autowired
    private SlotService slotService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSlotMockMvc;

    private Slot slot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Slot createEntity(EntityManager em) {
        Slot slot = new Slot()
            .type(DEFAULT_TYPE)
            .elecEnergy(DEFAULT_ELEC_ENERGY)
            .status(DEFAULT_STATUS)
            .fixedAmount(DEFAULT_FIXED_AMOUNT);
        return slot;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Slot createUpdatedEntity(EntityManager em) {
        Slot slot = new Slot()
            .type(UPDATED_TYPE)
            .elecEnergy(UPDATED_ELEC_ENERGY)
            .status(UPDATED_STATUS)
            .fixedAmount(UPDATED_FIXED_AMOUNT);
        return slot;
    }

    @BeforeEach
    public void initTest() {
        slot = createEntity(em);
    }

    @Test
    @Transactional
    public void createSlot() throws Exception {
        int databaseSizeBeforeCreate = slotRepository.findAll().size();

        // Create the Slot
        SlotDTO slotDTO = slotMapper.toDto(slot);
        restSlotMockMvc.perform(post("/api/slots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(slotDTO)))
            .andExpect(status().isCreated());

        // Validate the Slot in the database
        List<Slot> slotList = slotRepository.findAll();
        assertThat(slotList).hasSize(databaseSizeBeforeCreate + 1);
        Slot testSlot = slotList.get(slotList.size() - 1);
        assertThat(testSlot.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSlot.getElecEnergy()).isEqualTo(DEFAULT_ELEC_ENERGY);
        assertThat(testSlot.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSlot.getFixedAmount()).isEqualTo(DEFAULT_FIXED_AMOUNT);
    }

    @Test
    @Transactional
    public void createSlotWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = slotRepository.findAll().size();

        // Create the Slot with an existing ID
        slot.setId(1L);
        SlotDTO slotDTO = slotMapper.toDto(slot);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSlotMockMvc.perform(post("/api/slots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(slotDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Slot in the database
        List<Slot> slotList = slotRepository.findAll();
        assertThat(slotList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSlots() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList
        restSlotMockMvc.perform(get("/api/slots?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slot.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].elecEnergy").value(hasItem(DEFAULT_ELEC_ENERGY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].fixedAmount").value(hasItem(DEFAULT_FIXED_AMOUNT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getSlot() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get the slot
        restSlotMockMvc.perform(get("/api/slots/{id}", slot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(slot.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.elecEnergy").value(DEFAULT_ELEC_ENERGY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.fixedAmount").value(DEFAULT_FIXED_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSlot() throws Exception {
        // Get the slot
        restSlotMockMvc.perform(get("/api/slots/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSlot() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        int databaseSizeBeforeUpdate = slotRepository.findAll().size();

        // Update the slot
        Slot updatedSlot = slotRepository.findById(slot.getId()).get();
        // Disconnect from session so that the updates on updatedSlot are not directly saved in db
        em.detach(updatedSlot);
        updatedSlot
            .type(UPDATED_TYPE)
            .elecEnergy(UPDATED_ELEC_ENERGY)
            .status(UPDATED_STATUS)
            .fixedAmount(UPDATED_FIXED_AMOUNT);
        SlotDTO slotDTO = slotMapper.toDto(updatedSlot);

        restSlotMockMvc.perform(put("/api/slots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(slotDTO)))
            .andExpect(status().isOk());

        // Validate the Slot in the database
        List<Slot> slotList = slotRepository.findAll();
        assertThat(slotList).hasSize(databaseSizeBeforeUpdate);
        Slot testSlot = slotList.get(slotList.size() - 1);
        assertThat(testSlot.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSlot.getElecEnergy()).isEqualTo(UPDATED_ELEC_ENERGY);
        assertThat(testSlot.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSlot.getFixedAmount()).isEqualTo(UPDATED_FIXED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingSlot() throws Exception {
        int databaseSizeBeforeUpdate = slotRepository.findAll().size();

        // Create the Slot
        SlotDTO slotDTO = slotMapper.toDto(slot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlotMockMvc.perform(put("/api/slots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(slotDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Slot in the database
        List<Slot> slotList = slotRepository.findAll();
        assertThat(slotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSlot() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        int databaseSizeBeforeDelete = slotRepository.findAll().size();

        // Delete the slot
        restSlotMockMvc.perform(delete("/api/slots/{id}", slot.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Slot> slotList = slotRepository.findAll();
        assertThat(slotList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
