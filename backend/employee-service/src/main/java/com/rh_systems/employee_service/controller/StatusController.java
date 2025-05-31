package com.rh_systems.employee_service.controller;

import com.rh_systems.employee_service.dto.StatusDTO;
import com.rh_systems.employee_service.dto.StatusDTOGetPostPut;
import com.rh_systems.employee_service.service.StatusService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing status endpoints
 */
@RestController
@RequestMapping("/api/status")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StatusController {

    private final StatusService statusService;

    /**
     * Constructor with required dependencies
     *
     * @param statusService the status service
     */
    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    /**
     * GET /api/status : get all statuses
     *
     * @return the list of statuses
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<StatusDTOGetPostPut> findAll() {
        return statusService.findAll();
    }

    /**
     * GET /api/status/:id : get status by id
     *
     * @param id the id of the status to retrieve
     * @return the status information
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatusDTOGetPostPut> findById(@PathVariable Long id) {
        return statusService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/status : create a new status
     *
     * @param statusDTO the status to create
     * @return the created status
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatusDTOGetPostPut> save(@Valid @RequestBody StatusDTO statusDTO) {
        return statusService.save(statusDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    /**
     * PUT /api/status/:id : update an existing status
     *
     * @param id        the id of the status to update
     * @param statusDTO the status to update
     * @return the updated status
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatusDTOGetPostPut> update(@PathVariable Long id, @Valid @RequestBody StatusDTO statusDTO) {
        return statusService.update(id, statusDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/status/:id : delete a status
     *
     * @param id the id of the status to delete
     * @return no content if deleted successfully
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        return statusService.deleteById(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }
}