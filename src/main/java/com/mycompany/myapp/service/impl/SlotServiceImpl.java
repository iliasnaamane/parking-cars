package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.SlotService;
import com.mycompany.myapp.domain.Slot;
import com.mycompany.myapp.repository.SlotRepository;
import com.mycompany.myapp.service.dto.SlotDTO;
import com.mycompany.myapp.service.mapper.SlotMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Slot}.
 */
@Service
@Transactional
public class SlotServiceImpl implements SlotService {

    private final Logger log = LoggerFactory.getLogger(SlotServiceImpl.class);

    private final SlotRepository slotRepository;

    private final SlotMapper slotMapper;

    public SlotServiceImpl(SlotRepository slotRepository, SlotMapper slotMapper) {
        this.slotRepository = slotRepository;
        this.slotMapper = slotMapper;
    }

    /**
     * Save a slot.
     *
     * @param slotDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SlotDTO save(SlotDTO slotDTO) {
        log.debug("Request to save Slot : {}", slotDTO);
        Slot slot = slotMapper.toEntity(slotDTO);
        slot = slotRepository.save(slot);
        return slotMapper.toDto(slot);
    }

    /**
     * Get all the slots.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<SlotDTO> findAll() {
        log.debug("Request to get all Slots");
        return slotRepository.findAll().stream()
            .map(slotMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one slot by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SlotDTO> findOne(Long id) {
        log.debug("Request to get Slot : {}", id);
        return slotRepository.findById(id)
            .map(slotMapper::toDto);
    }

    /**
     * Delete the slot by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Slot : {}", id);
        slotRepository.deleteById(id);
    }
}
