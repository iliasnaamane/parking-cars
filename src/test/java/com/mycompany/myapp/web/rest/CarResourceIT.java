package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.Car;
import com.mycompany.myapp.domain.Slot;
import com.mycompany.myapp.domain.enumeration.Status;
import com.mycompany.myapp.repository.CarRepository;
import com.mycompany.myapp.service.CarService;
import com.mycompany.myapp.service.dto.CarDTO;
import com.mycompany.myapp.service.mapper.CarMapper;

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
import com.mycompany.myapp.service.dto.BillDTO;
import com.mycompany.myapp.service.dto.SlotDTO;
import com.mycompany.myapp.service.mapper.SlotMapper;
import jdk.nashorn.internal.ir.annotations.Ignore;
/**
 * Integration tests for the {@link CarResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class CarResourceIT {
    
    private static final Status DEFAULT_STATUS = Status.Free;
     private static final Float DEFAULT_FIXED_AMOUNT = 1F;
    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Type DEFAULT_TYPE = Type.Gasoline;
    private static final Type UPDATED_TYPE = Type.Elec;

    private static final Integer DEFAULT_ELEC_ENERGY = 20;
    private static final Integer UPDATED_ELEC_ENERGY = 21;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarMapper carMapper;
    
    @Autowired
    private SlotMapper slotMapper;

    @Autowired
    private CarService carService;

    @Autowired
    private EntityManager em;
    
    @Autowired
    private EntityManager emslot;

    @Autowired
    private MockMvc restCarMockMvc;
    
    @Autowired
    private MockMvc restSlotMockMvc;

    private Car car;
    
    private Slot slot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createEntity(EntityManager em) {
        Car car = new Car()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .elecEnergy(DEFAULT_ELEC_ENERGY);
        return car;
    }
    
        public static Slot createSlotEntity(EntityManager emslot) {
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
    public static Car createUpdatedEntity(EntityManager em) {
        Car car = new Car()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .elecEnergy(UPDATED_ELEC_ENERGY);
        return car;
    }

    @BeforeEach
    public void initTest() {
        car = createEntity(em);
        slot = createSlotEntity(emslot);
    }

    @Test
    @Transactional
    public void createCar() throws Exception {
        int databaseSizeBeforeCreate = carRepository.findAll().size();
        // Create Slot
        SlotDTO slotDTO = slotMapper.toDto(slot);
        restSlotMockMvc.perform(post("/api/slots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(slotDTO)))
            .andExpect(status().isCreated());
        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);
        restCarMockMvc.perform(post("/api/cars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carDTO)))
            .andExpect(status().isCreated());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate + 1);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCar.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCar.getElecEnergy()).isEqualTo(DEFAULT_ELEC_ENERGY);
    }

    @Test
    @Transactional
    public void createCarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carRepository.findAll().size();

        // Create the Car with an existing ID
        car.setId(1L);
        CarDTO carDTO = carMapper.toDto(car);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarMockMvc.perform(post("/api/cars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCars() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList
        restCarMockMvc.perform(get("/api/cars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].elecEnergy").value(hasItem(DEFAULT_ELEC_ENERGY)));
    }
    
    @Test
    @Transactional
    public void getCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get the car
        restCarMockMvc.perform(get("/api/cars/{id}", car.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(car.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.elecEnergy").value(DEFAULT_ELEC_ENERGY));
    }

    @Test
    @Transactional
    public void getNonExistingCar() throws Exception {
        // Get the car
        restCarMockMvc.perform(get("/api/cars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNonExistingCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc.perform(put("/api/cars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(carDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }
    
    public void deleteCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeDelete = carRepository.findAll().size();
        
        BillDTO billDTO = new BillDTO();
        billDTO.setHourPrice(10);
        billDTO.setNumberHours(10);
        billDTO.setId(car.getId());
        // Delete the car
        restCarMockMvc.perform(delete("/api/cars/{id}", car.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isOk());

        // Validate the database contains one less item
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
