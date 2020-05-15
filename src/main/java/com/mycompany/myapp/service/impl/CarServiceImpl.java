package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.CarService;
import com.mycompany.myapp.domain.Car;
import com.mycompany.myapp.domain.Slot;
import com.mycompany.myapp.domain.enumeration.Status;
import com.mycompany.myapp.domain.enumeration.Type;
import com.mycompany.myapp.repository.CarRepository;
import com.mycompany.myapp.repository.SlotRepository;
import com.mycompany.myapp.service.dto.BillDTO;
import com.mycompany.myapp.service.dto.CarDTO;
import com.mycompany.myapp.service.mapper.CarMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Car}.
 */
@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final Logger log = LoggerFactory.getLogger(CarServiceImpl.class);

    private final CarRepository carRepository;
    
    private final SlotRepository slotRepository;

    private final CarMapper carMapper;

    public CarServiceImpl(CarRepository carRepository, CarMapper carMapper, SlotRepository slotRepository) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
        this.slotRepository = slotRepository;
    }

    /**
     * Save a car.
     *
     * @param carDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CarDTO save(CarDTO carDTO) {
        Slot freeslot;
        
        log.debug("Request to  save Car : {}", carDTO);
        Car car = carMapper.toEntity(carDTO);
        
       List<Slot> slots = slotRepository.findSlotByType(car.getType(), Status.Free);
       freeslot = slots.get(0);
       freeslot.setStatus(Status.Full);
       car.setSlot(freeslot);
       car = carRepository.save(car);
       slotRepository.save(freeslot);
        return carMapper.toDto(car);
    }

    /**
     * Get all the cars.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CarDTO> findAll() {
        log.debug("Request to get all Cars");
        return carRepository.findAll().stream()
            .map(carMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one car by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CarDTO> findOne(Long id) {
        log.debug("Request to get Car : {}", id);
        return carRepository.findById(id)
            .map(carMapper::toDto);
    }

    /**
     * Delete the car by id.
     *
     * @param id the id of the entity.
     * @param numberHours
     * @param hourPrice
     * @return 
     */
    @Override
    public BillDTO delete(Long id, Integer numberHours, Integer hourPrice) {
        Float price;
        log.debug("Request to delete Car : {}", id);
        Car car;
        car = carRepository.getOne(id);
        Slot slot = car.getSlot();
        
        price = slot.getFixedAmount()+ numberHours*hourPrice;
        slot.setStatus(Status.Free);
        slotRepository.save(slot);
        carRepository.deleteById(id);
        BillDTO bill = new BillDTO();
        bill.setId(id);
        bill.setFinalPrice(price);
        bill.setHourPrice(hourPrice);
        bill.setNumberHours(numberHours);
        return bill;
    }
}
