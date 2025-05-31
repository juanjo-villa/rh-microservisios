package com.rh_systems.schedule_service.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh_systems.schedule_service.Entity.CountEmployeeSchedule;
import com.rh_systems.schedule_service.Entity.EmployeeSchedule;
import com.rh_systems.schedule_service.dto.CountEmployeeScheduleDTO;
import com.rh_systems.schedule_service.dto.CountEmployeeScheduleDTOGetPostPut;
import com.rh_systems.schedule_service.repository.CountEmployeeScheduleRepository;
import com.rh_systems.schedule_service.repository.EmployeeScheduleRepository;

@Service
public class CountEmployeeScheduleService {
    @Autowired
    private CountEmployeeScheduleRepository countEmployeeScheduleRepository;

    @Autowired
    private EmployeeScheduleRepository employeeScheduleRepository;

    /**
     * Gets all count employee schedules.
     * @return a list of CountEmployeeScheduleDTOGetPostPut
     */
    public List<CountEmployeeScheduleDTOGetPostPut> getAllEmployeeSchedule() {
        List<CountEmployeeScheduleDTOGetPostPut> countEmployeeScheduleToReturn = new ArrayList<>();
        List<CountEmployeeSchedule> countEmployeeSchedules = countEmployeeScheduleRepository.findAll();
        for (CountEmployeeSchedule ces : countEmployeeSchedules) {
            CountEmployeeScheduleDTOGetPostPut countEmployeeScheduleDTO = new CountEmployeeScheduleDTOGetPostPut();
            countEmployeeScheduleDTO.convertToCountEmployeeScheduleDTO(ces);
            countEmployeeScheduleToReturn.add(countEmployeeScheduleDTO);
        }
        return countEmployeeScheduleToReturn;
    }

    /**
     * Gets a count employee schedule by its ID.
     * @param id the count employee schedule ID
     * @return an Optional containing the CountEmployeeScheduleDTOGetPostPut if found, or empty otherwise
     */
    public Optional<CountEmployeeScheduleDTOGetPostPut> getCountEmployeeScheduleById(Long id) {
        Optional<CountEmployeeSchedule> countEmployeeSchedule = countEmployeeScheduleRepository.findById(id);
        if (countEmployeeSchedule.isPresent()) {
            CountEmployeeScheduleDTOGetPostPut countEmployeeScheduleDTO = new CountEmployeeScheduleDTOGetPostPut();
            countEmployeeScheduleDTO.convertToCountEmployeeScheduleDTO(countEmployeeSchedule.get());
            return Optional.of(countEmployeeScheduleDTO);
        }
        return Optional.empty();
    }

    /**
     * Gets a count employee schedule by work hours.
     * @param workHours the work hours
     * @return an Optional containing the CountEmployeeScheduleDTOGetPostPut if found, or empty otherwise
     */
    public Optional<CountEmployeeScheduleDTOGetPostPut> getCountEmployeeScheduleByWorkHours(Float workHours) {
        Optional<CountEmployeeSchedule> countEmployeeSchedule = countEmployeeScheduleRepository.findByWorkHours(workHours);
        if (countEmployeeSchedule.isPresent()) {
            CountEmployeeScheduleDTOGetPostPut countEmployeeScheduleDTO = new CountEmployeeScheduleDTOGetPostPut();
            countEmployeeScheduleDTO.convertToCountEmployeeScheduleDTO(countEmployeeSchedule.get());
            return Optional.of(countEmployeeScheduleDTO);
        }
        return Optional.empty();
    }

    /**
     * Creates a new count employee schedule.
     * @param countEmployeeScheduleDTO the count employee schedule data
     * @return an Optional containing the created CountEmployeeScheduleDTOGetPostPut, or empty if a schedule with the same work hours exists
     */
    public Optional<CountEmployeeScheduleDTOGetPostPut> createCountEmployeeSchedule(CountEmployeeScheduleDTO countEmployeeScheduleDTO) {
        if (countEmployeeScheduleRepository.findByWorkHours(countEmployeeScheduleDTO.getWorkHours()).isPresent()) {
            return Optional.empty();
        } 
        CountEmployeeSchedule countEmployeeSchedule = new CountEmployeeSchedule();
        countEmployeeSchedule.setWorkHours(countEmployeeScheduleDTO.getWorkHours());
        countEmployeeSchedule.setWorkDate(countEmployeeScheduleDTO.getWorkDate());
        EmployeeSchedule employeeSchedule = new EmployeeSchedule();
        employeeSchedule.setId(countEmployeeScheduleDTO.getEmployeeScheduleId());
        countEmployeeSchedule.setEmployeeSchedule(employeeSchedule);
        CountEmployeeScheduleDTOGetPostPut createdCountEmployeeScheduleDTO = new CountEmployeeScheduleDTOGetPostPut();
        createdCountEmployeeScheduleDTO.convertToCountEmployeeScheduleDTO(countEmployeeScheduleRepository.save(countEmployeeSchedule));
        return Optional.of(createdCountEmployeeScheduleDTO);
    }

    /**
     * Updates an existing count employee schedule.
     * @param id the count employee schedule ID
     * @param countEmployeeScheduleDTO the count employee schedule data to update
     * @return an Optional containing the updated CountEmployeeScheduleDTOGetPostPut, or empty if not found or work hours conflict
     */
    public Optional<CountEmployeeScheduleDTOGetPostPut> updateCountEmployeeSchedule(Long id, CountEmployeeScheduleDTO countEmployeeScheduleDTO) {
        Optional<CountEmployeeSchedule> countEmployeeSchedule = countEmployeeScheduleRepository.findById(id);
        if (countEmployeeSchedule.isPresent()) {
            if (!countEmployeeSchedule.get().getWorkHours().equals(countEmployeeScheduleDTO.getWorkHours())) {
                if (countEmployeeScheduleRepository.findByWorkHours(countEmployeeScheduleDTO.getWorkHours()).isPresent()) {
                    return Optional.empty();
                }
            }
            CountEmployeeSchedule updatedCountEmployeeSchedule = countEmployeeSchedule.get();
            updatedCountEmployeeSchedule.setWorkHours(countEmployeeScheduleDTO.getWorkHours());
            updatedCountEmployeeSchedule.setWorkDate(countEmployeeScheduleDTO.getWorkDate());
            EmployeeSchedule employeeSchedule = new EmployeeSchedule();
            employeeSchedule.setId(countEmployeeScheduleDTO.getEmployeeScheduleId());
            updatedCountEmployeeSchedule.setEmployeeSchedule(employeeSchedule);
            CountEmployeeScheduleDTOGetPostPut updatedCountEmployeeScheduleDTO = new CountEmployeeScheduleDTOGetPostPut();
            updatedCountEmployeeScheduleDTO.convertToCountEmployeeScheduleDTO(countEmployeeScheduleRepository.save(updatedCountEmployeeSchedule));
            return Optional.of(updatedCountEmployeeScheduleDTO);
        }
        return Optional.empty();
    }

    /**
     * Deletes a count employee schedule by its ID.
     * @param id the count employee schedule ID
     * @return true if the count employee schedule was deleted, false otherwise
     */
    public boolean deleteCountEmployeeSchedule(Long id) {
        if (countEmployeeScheduleRepository.findById(id).isPresent()) {
            countEmployeeScheduleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Registers work hours for an employee.
     * If the employee already has a count, it adds the hours to the existing count,
     * ensuring that the total doesn't exceed 8 hours.
     * If the employee doesn't have a count, it creates a new one.
     * 
     * @param employeeId the employee ID
     * @param hoursWorked the hours worked to register
     */
    public void registerWorkHours(Long employeeId, float hoursWorked) {
        CountEmployeeSchedule count = countEmployeeScheduleRepository.findByEmployeeId(employeeId)
            .orElse(new CountEmployeeSchedule());

        if (count.getId() == null) {
            // New count - need to set up the employee schedule relationship
            List<EmployeeSchedule> employeeSchedules = employeeScheduleRepository.findByEmployeeId(employeeId);
            if (!employeeSchedules.isEmpty()) {
                // Use the first employee schedule found
                count.setEmployeeSchedule(employeeSchedules.get(0));
                count.setWorkDate(LocalDate.now());
                count.setWorkHours(0f);
            } else {
                // Cannot register hours for an employee that doesn't have a schedule
                return;
            }
        }

        // Only add hours if current count is less than 8
        if (count.getWorkHours() < 8f) {
            float newHours = count.getWorkHours() + hoursWorked;
            count.setWorkHours(Math.min(newHours, 8f));
            countEmployeeScheduleRepository.save(count);
        }
    }
}
