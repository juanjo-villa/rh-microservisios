package com.rh_systems.employee_service.controller;

import com.rh_systems.employee_service.dto.PositionDTO;
import com.rh_systems.employee_service.dto.PositionDTOGetPostPut;
import com.rh_systems.employee_service.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Position entities
 */
@RestController
@RequestMapping("/api/position")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PositionController {

    private final PositionService positionService;

    /**
     * Constructor injection of PositionService
     *
     * @param positionService Service layer for Position operations
     */
    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    /**
     * GET /api/position : Get all positions
     *
     * @return List of positions
     */
    @GetMapping
    public List<PositionDTOGetPostPut> findAll() {
        return positionService.findAll();
    }

    /**
     * GET /api/position/:id : Get position by id
     *
     * @param id Position id
     * @return Position if found, 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<PositionDTOGetPostPut> findById(@PathVariable Long id) {
        return positionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/position/name/:name : Get position by name
     *
     * @param name Position name
     * @return Position if found, 404 if not found
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<PositionDTOGetPostPut> findByName(@PathVariable String name) {
        return positionService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/position : Create a new position
     *
     * @param positionDTO Position to create
     * @return Created position, or null if creation failed
     */
    @PostMapping
    public ResponseEntity<PositionDTOGetPostPut> save(@RequestBody PositionDTO positionDTO) {
        return positionService.save(positionDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    /**
     * PUT /api/position/:id : Update position by id
     *
     * @param id          Position id
     * @param positionDTO Updated position data
     * @return Updated position if found, 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<PositionDTOGetPostPut> update(@PathVariable Long id, @RequestBody PositionDTO positionDTO) {
        return positionService.update(id, positionDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/position/name/:name : Update position by name
     *
     * @param name        Position name
     * @param positionDTO Updated position data
     * @return Updated position if found, 404 if not found
     */
    @PutMapping("/name/{name}")
    public ResponseEntity<PositionDTOGetPostPut> updateByName(@PathVariable String name, @RequestBody PositionDTO positionDTO) {
        return positionService.updateByName(name, positionDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/position/:id : Delete position by id
     *
     * @param id Position id to delete
     * @return 200 if deleted, 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        return positionService.deleteById(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    /**
     * DELETE /api/position/name/:name : Delete position by name
     *
     * @param name Position name to delete
     * @return 200 if deleted, 404 if not found
     */
    @DeleteMapping("/name/{name}")
    public ResponseEntity<Void> deleteByName(@PathVariable String name) {
        return positionService.deleteByName(name) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }
}