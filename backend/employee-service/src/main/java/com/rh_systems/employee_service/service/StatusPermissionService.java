package com.rh_systems.employee_service.service;

import com.rh_systems.employee_service.Entity.StatusPermission;
import com.rh_systems.employee_service.dto.StatusPermissionDTOGetPostPut;
import com.rh_systems.employee_service.repository.StatusPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing StatusPermission entities.
 */
@Service
public class StatusPermissionService {

    @Autowired
    private StatusPermissionRepository statusPermissionRepository;

    /**
     * Retrieves all StatusPermission entities and converts them to DTOs.
     *
     * @return List of StatusPermissionDTOGetPostPut objects.
     */
    public List<StatusPermissionDTOGetPostPut> findAll() {
        List<StatusPermissionDTOGetPostPut> permissionsToReturn = new ArrayList<>();
        List<StatusPermission> permissions = statusPermissionRepository.findAll();

        for (StatusPermission permission : permissions) {
            StatusPermissionDTOGetPostPut dto = new StatusPermissionDTOGetPostPut();
            dto.convertToStatusPermissionDTO(permission);
            permissionsToReturn.add(dto);
        }
        return permissionsToReturn;
    }

    /**
     * Finds a StatusPermission by its ID and converts it to a DTO.
     *
     * @param id The ID of the StatusPermission to find.
     * @return Optional of StatusPermissionDTOGetPostPut if found, otherwise empty.
     */
    public Optional<StatusPermissionDTOGetPostPut> findById(Long id) {
        Optional<StatusPermission> permission = statusPermissionRepository.findById(id);

        if (permission.isPresent()) {
            StatusPermissionDTOGetPostPut dto = new StatusPermissionDTOGetPostPut();
            dto.convertToStatusPermissionDTO(permission.get());
            return Optional.of(dto);
        }

        return Optional.empty();
    }

    /**
     * Saves a new StatusPermission entity.
     *
     * @param dto DTO containing the StatusPermission details.
     * @return Optional of StatusPermissionDTOGetPostPut if saved successfully.
     */
    public Optional<StatusPermissionDTOGetPostPut> save(StatusPermissionDTOGetPostPut dto) {
        StatusPermission permission = new StatusPermission();
        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());

        StatusPermission savedPermission = statusPermissionRepository.save(permission);

        StatusPermissionDTOGetPostPut resultDTO = new StatusPermissionDTOGetPostPut();
        resultDTO.convertToStatusPermissionDTO(savedPermission);

        return Optional.of(resultDTO);
    }

    /**
     * Updates an existing StatusPermission by its ID.
     *
     * @param id  The ID of the StatusPermission to update.
     * @param dto DTO containing the updated details.
     * @return Optional of StatusPermissionDTOGetPostPut if updated successfully, otherwise empty.
     */
    public Optional<StatusPermissionDTOGetPostPut> update(Long id, StatusPermissionDTOGetPostPut dto) {
        Optional<StatusPermission> existingPermission = statusPermissionRepository.findById(id);

        if (existingPermission.isPresent()) {
            StatusPermission permissionToUpdate = existingPermission.get();
            permissionToUpdate.setName(dto.getName());
            permissionToUpdate.setDescription(dto.getDescription());

            StatusPermission updatedPermission = statusPermissionRepository.save(permissionToUpdate);

            StatusPermissionDTOGetPostPut resultDTO = new StatusPermissionDTOGetPostPut();
            resultDTO.convertToStatusPermissionDTO(updatedPermission);

            return Optional.of(resultDTO);
        }

        return Optional.empty();
    }

    /**
     * Deletes a StatusPermission by its ID.
     *
     * @param id The ID of the StatusPermission to delete.
     * @return True if deleted successfully, otherwise false.
     */
    public boolean deleteById(Long id) {
        if (statusPermissionRepository.existsById(id)) {
            statusPermissionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}