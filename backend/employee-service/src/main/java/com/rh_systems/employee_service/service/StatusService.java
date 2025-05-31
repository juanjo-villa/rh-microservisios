package com.rh_systems.employee_service.service;

import com.rh_systems.employee_service.Entity.Status;
import com.rh_systems.employee_service.Entity.Employee;
import com.rh_systems.employee_service.Entity.StatusPermission;
import com.rh_systems.employee_service.dto.StatusDTO;
import com.rh_systems.employee_service.dto.StatusDTOGetPostPut;
import com.rh_systems.employee_service.repository.StatusRepository;
import com.rh_systems.employee_service.repository.EmployeeRepository;
import com.rh_systems.employee_service.repository.StatusPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Employee Status.
 */
@Service
public class StatusService {
    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private StatusPermissionRepository statusPermissionRepository;

    /**
     * Retrieves all statuses and returns them as a list of StatusDTOGetPostPut objects.
     *
     * @return List of StatusDTOGetPostPut objects.
     */
    public List<StatusDTOGetPostPut> findAll() {
        List<StatusDTOGetPostPut> statusesToReturn = new ArrayList<>();
        List<Status> statuses = statusRepository.findAll();
        for (Status status : statuses) {
            StatusDTOGetPostPut dto = new StatusDTOGetPostPut();
            dto.convertToStatus(status);
            statusesToReturn.add(dto);
        }
        return statusesToReturn;
    }

    /**
     * Finds a status by its ID and returns it as a StatusDTOGetPostPut object.
     *
     * @param id The ID of the status to find.
     * @return Optional of StatusDTOGetPostPut if found, otherwise empty.
     */
    public Optional<StatusDTOGetPostPut> findById(long id) {
        Optional<Status> status = statusRepository.findById(id);
        if (status.isPresent()) {
            StatusDTOGetPostPut dto = new StatusDTOGetPostPut();
            dto.convertToStatus(status.get());
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    /**
     * Saves a new status and assigns it to the relevant employee and permission entities.
     *
     * @param statusDTO Data transfer object representing the new status.
     * @return Optional of StatusDTOGetPostPut if saved successfully, otherwise empty if related entities do not exist.
     */
    public Optional<StatusDTOGetPostPut> save(StatusDTO statusDTO) {
        Optional<Employee> employee = employeeRepository.findById(statusDTO.getEmployeeId());
        if (!employee.isPresent()) {
            return Optional.empty();
        }

        Optional<StatusPermission> statusPermission = statusPermissionRepository.findById(statusDTO.getStatusPermissionId());
        if (!statusPermission.isPresent()) {
            return Optional.empty();
        }

        Status status = new Status();
        status.setType(statusDTO.getType());
        status.setStartDate(statusDTO.getStartDate());
        status.setEndDate(statusDTO.getEndDate());
        status.setPaid(statusDTO.getPaid());
        status.setDescription(statusDTO.getDescription());
        status.setEmployee(employee.get());
        status.setStatusPermission(statusPermission.get());

        Status savedStatus = statusRepository.save(status);
        StatusDTOGetPostPut dto = new StatusDTOGetPostPut();
        dto.convertToStatus(savedStatus);
        return Optional.of(dto);
    }

    /**
     * Updates an existing status by its ID.
     *
     * @param id        Status ID to update.
     * @param statusDTO Data transfer object containing updated information.
     * @return Optional of StatusDTOGetPostPut if updated successfully, otherwise empty if the status or related entities are not found.
     */
    public Optional<StatusDTOGetPostPut> update(long id, StatusDTO statusDTO) {
        Optional<Status> existingStatus = statusRepository.findById(id);
        if (!existingStatus.isPresent()) {
            return Optional.empty();
        }

        Optional<Employee> employee = employeeRepository.findById(statusDTO.getEmployeeId());
        if (!employee.isPresent()) {
            return Optional.empty();
        }

        Optional<StatusPermission> statusPermission = statusPermissionRepository.findById(statusDTO.getStatusPermissionId());
        if (!statusPermission.isPresent()) {
            return Optional.empty();
        }

        Status statusToUpdate = existingStatus.get();
        statusToUpdate.setType(statusDTO.getType());
        statusToUpdate.setStartDate(statusDTO.getStartDate());
        statusToUpdate.setEndDate(statusDTO.getEndDate());
        statusToUpdate.setPaid(statusDTO.getPaid());
        statusToUpdate.setDescription(statusDTO.getDescription());
        statusToUpdate.setEmployee(employee.get());
        statusToUpdate.setStatusPermission(statusPermission.get());

        Status updatedStatus = statusRepository.save(statusToUpdate);
        StatusDTOGetPostPut dto = new StatusDTOGetPostPut();
        dto.convertToStatus(updatedStatus);
        return Optional.of(dto);
    }

    /**
     * Deletes a status by its ID.
     *
     * @param id The ID of the status to delete.
     * @return True if deleted successfully, otherwise false.
     */
    public boolean deleteById(long id) {
        if (statusRepository.existsById(id)) {
            statusRepository.deleteById(id);
            return true;
        }
        return false; // Return false if the status does not exist.
    }
}
