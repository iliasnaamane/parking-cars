package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.SlotService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.SlotDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Slot}.
 */
@RestController
@RequestMapping("/api")
public class SlotResource {

    private final Logger log = LoggerFactory.getLogger(SlotResource.class);

    private static final String ENTITY_NAME = "slot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SlotService slotService;

    public SlotResource(SlotService slotService) {
        this.slotService = slotService;
    }

    /**
     * {@code POST  /slots} : Create a new slot.
     *
     * @param slotDTO the slotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new slotDTO, or with status {@code 400 (Bad Request)} if the slot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/slots")
    public ResponseEntity<SlotDTO> createSlot(@Valid @RequestBody SlotDTO slotDTO) throws URISyntaxException {
        log.debug("REST request to save Slot : {}", slotDTO);
        if (slotDTO.getId() != null) {
            throw new BadRequestAlertException("A new slot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SlotDTO result = slotService.save(slotDTO);
        return ResponseEntity.created(new URI("/api/slots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /slots} : Updates an existing slot.
     *
     * @param slotDTO the slotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated slotDTO,
     * or with status {@code 400 (Bad Request)} if the slotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the slotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/slots")
    public ResponseEntity<SlotDTO> updateSlot(@Valid @RequestBody SlotDTO slotDTO) throws URISyntaxException {
        log.debug("REST request to update Slot : {}", slotDTO);
        if (slotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SlotDTO result = slotService.save(slotDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, slotDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /slots} : get all the slots.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of slots in body.
     */
    @GetMapping("/slots")
    public List<SlotDTO> getAllSlots() {
        log.debug("REST request to get all Slots");
        return slotService.findAll();
    }

    /**
     * {@code GET  /slots/:id} : get the "id" slot.
     *
     * @param id the id of the slotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the slotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/slots/{id}")
    public ResponseEntity<SlotDTO> getSlot(@PathVariable Long id) {
        log.debug("REST request to get Slot : {}", id);
        Optional<SlotDTO> slotDTO = slotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(slotDTO);
    }

    /**
     * {@code DELETE  /slots/:id} : delete the "id" slot.
     *
     * @param id the id of the slotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/slots/{id}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long id) {
        log.debug("REST request to delete Slot : {}", id);
        slotService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
