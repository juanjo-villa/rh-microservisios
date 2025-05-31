package com.rh_systems.schedule_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh_systems.schedule_service.Entity.EmployeeSchedule;
import com.rh_systems.schedule_service.Entity.Schedule;
import com.rh_systems.schedule_service.dto.EmployeeScheduleDTO;
import com.rh_systems.schedule_service.dto.EmployeeScheduleDTOGetPostPut;
import com.rh_systems.schedule_service.repository.EmployeeScheduleRepository;
import com.rh_systems.schedule_service.repository.ScheduleRepository;


@Service
public class EmployeeScheduleService {
    @Autowired
    private EmployeeScheduleRepository employeeScheduleRepository;

    // @Autowired
    // private EmployeeRepository employeeRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<EmployeeScheduleDTOGetPostPut> getAllEmployeeSchedule() {
        List<EmployeeScheduleDTOGetPostPut> employeeScheduleToReturn = new ArrayList<>();
        List<EmployeeSchedule> employeeSchedules = employeeScheduleRepository.findAll();
        for (EmployeeSchedule es : employeeSchedules) {
            EmployeeScheduleDTOGetPostPut employeeScheduleDTO = new EmployeeScheduleDTOGetPostPut();
            employeeScheduleDTO.convertToEmployeeScheduleDTO(es);
            employeeScheduleToReturn.add(employeeScheduleDTO);
        }
        return employeeScheduleToReturn;
    }

    public Optional<EmployeeScheduleDTOGetPostPut> getEmployeeScheduleById(Long id) {
        Optional<EmployeeSchedule> employeeSchedule = employeeScheduleRepository.findById(id);
        if (employeeSchedule.isPresent()) {
            EmployeeScheduleDTOGetPostPut employeeScheduleDTO = new EmployeeScheduleDTOGetPostPut();
            employeeScheduleDTO.convertToEmployeeScheduleDTO(employeeSchedule.get());
            return Optional.of(employeeScheduleDTO);
        }
        return Optional.empty();
    }

    public Optional<EmployeeScheduleDTOGetPostPut> getEmployeeScheduleByEmployeeIdAndScheduleId(Long employeeId, Long scheduleId) {
        Optional<EmployeeSchedule> employeeSchedule = employeeScheduleRepository.findByEmployeeIdAndScheduleId(employeeId, scheduleId);
        if (employeeSchedule.isPresent()) {
            EmployeeScheduleDTOGetPostPut employeeScheduleDTO = new EmployeeScheduleDTOGetPostPut();
            employeeScheduleDTO.convertToEmployeeScheduleDTO(employeeSchedule.get());
            return Optional.of(employeeScheduleDTO);
        }
        return Optional.empty();
    }

    public Optional<EmployeeScheduleDTOGetPostPut> createEmployeeSchedule(EmployeeScheduleDTO employeeScheduleDTO) {
        if (employeeScheduleRepository.findByEmployeeIdAndScheduleId(employeeScheduleDTO.getEmployeeId(), employeeScheduleDTO.getScheduleId()).isPresent()) {
            return Optional.empty();
        }
        // Optional<Employee> employee = employeeRepository.findById(employeeScheduleDTO.getEmployeeId());
        Optional<Schedule> schedule = scheduleRepository.findById(employeeScheduleDTO.getScheduleId());
        if (/*employee.isPresent() &&*/ schedule.isPresent()) {
            EmployeeSchedule employeeSchedule = new EmployeeSchedule();
            // employeeSchedule.setEmployee(employee.get());
            employeeSchedule.setEmployeeId(employeeScheduleDTO.getEmployeeId());
            employeeSchedule.setSchedule(schedule.get());
            EmployeeScheduleDTOGetPostPut createdEmployeeScheduleDTO = new EmployeeScheduleDTOGetPostPut();
            createdEmployeeScheduleDTO.convertToEmployeeScheduleDTO(employeeScheduleRepository.save(employeeSchedule));
            return Optional.of(createdEmployeeScheduleDTO);
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteEmployeeSchedule(Long id) {
        if (employeeScheduleRepository.findById(id).isPresent()) {
            employeeScheduleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
