package com.rh_systems.employee_service.controller;

import com.rh_systems.employee_service.dto.StatusPermissionDTOGetPostPut;
import com.rh_systems.employee_service.service.StatusPermissionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status-permissions")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StatusPermissionController {

    private final StatusPermissionService statusPermissionService;

    public StatusPermissionController(StatusPermissionService statusPermissionService) {
        this.statusPermissionService = statusPermissionService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<StatusPermissionDTOGetPostPut> findAll() {
        return statusPermissionService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatusPermissionDTOGetPostPut> findById(@PathVariable Long id) {
        return statusPermissionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatusPermissionDTOGetPostPut> save(@Valid @RequestBody StatusPermissionDTOGetPostPut statusPermissionDTO) {
        return statusPermissionService.save(statusPermissionDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatusPermissionDTOGetPostPut> update(@PathVariable Long id, @Valid @RequestBody StatusPermissionDTOGetPostPut statusPermissionDTO) {
        return statusPermissionService.update(id, statusPermissionDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        return statusPermissionService.deleteById(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }
}