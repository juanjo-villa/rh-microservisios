package com.rh_systems.employee_service.service;

import com.rh_systems.employee_service.Entity.Position;
import com.rh_systems.employee_service.dto.PositionDTO;
import com.rh_systems.employee_service.dto.PositionDTOGetPostPut;
import com.rh_systems.employee_service.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Position management.
 */
@Service
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;

    /**
     * Finds all positions stored in the database and returns a list of PositionDTOGetPostPut.
     *
     * @return List of PositionDTOGetPostPut.
     */
    public List<PositionDTOGetPostPut> findAll() {
        List<PositionDTOGetPostPut> positionsToReturn = new ArrayList<>();
        List<Position> positions = positionRepository.findAll();
        for (Position position : positions) {
            PositionDTOGetPostPut positionDTOGetPostPut = new PositionDTOGetPostPut();
            positionDTOGetPostPut.convertToPositionDTO(position);
            positionsToReturn.add(positionDTOGetPostPut);
        }
        return positionsToReturn;
    }

    /**
     * Finds a position by ID and returns it as an Optional of PositionDTOGetPostPut.
     *
     * @param id Position ID.
     * @return Optional of PositionDTOGetPostPut if found, otherwise empty.
     */
    public Optional<PositionDTOGetPostPut> findById(long id) {
        Optional<Position> position = positionRepository.findById(id);
        if (position.isPresent()) {
            PositionDTOGetPostPut positionDTOGetPostPut = new PositionDTOGetPostPut();
            positionDTOGetPostPut.convertToPositionDTO(position.get());
            return Optional.of(positionDTOGetPostPut);
        }
        return Optional.empty();
    }

    /**
     * Saves a new position in the database from the provided PositionDTO, ensuring that no duplicate position exists.
     *
     * @param positionDTO Position data to save.
     * @return Optional of PositionDTOGetPostPut if saved successfully, otherwise empty.
     */
    public Optional<PositionDTOGetPostPut> save(PositionDTO positionDTO) {
        if (positionRepository.findByName(positionDTO.getName()).isPresent()) {
            return Optional.empty(); // Return empty if the position name already exists
        }
        Position position = new Position();
        position.setName(positionDTO.getName());
        position.setDescription(positionDTO.getDescription());
        Position savedPosition = positionRepository.save(position);

        PositionDTOGetPostPut positionDTOGetPostPut = new PositionDTOGetPostPut();
        positionDTOGetPostPut.convertToPositionDTO(savedPosition);
        return Optional.of(positionDTOGetPostPut);
    }

    /**
     * Updates an existing position in the database based on its ID and the provided PositionDTO.
     * Validates if the new position name does not cause duplication before applying the update.
     *
     * @param id          Position ID to update.
     * @param positionDTO Position data to update.
     * @return Optional of PositionDTOGetPostPut if updated successfully, otherwise empty.
     */
    public Optional<PositionDTOGetPostPut> update(long id, PositionDTO positionDTO) {
        Optional<Position> existingPosition = positionRepository.findById(id);
        if (existingPosition.isPresent()) {
            if (!existingPosition.get().getName().equalsIgnoreCase(positionDTO.getName())) {
                if (positionRepository.findByName(positionDTO.getName()).isPresent()) {
                    return Optional.empty(); // Return empty if trying to update to an already existing name
                }
            }
            Position position = existingPosition.get();
            position.setName(positionDTO.getName());
            position.setDescription(positionDTO.getDescription());
            Position updatedPosition = positionRepository.save(position);

            PositionDTOGetPostPut positionDTOGetPostPut = new PositionDTOGetPostPut();
            positionDTOGetPostPut.convertToPositionDTO(updatedPosition);
            return Optional.of(positionDTOGetPostPut);
        }
        return Optional.empty(); // If position with the given ID does not exist
    }

    /**
     * Deletes a position by its ID if it exists.
     *
     * @param id Position ID to delete.
     * @return True if deletion was successful, false otherwise.
     */
    public boolean deleteById(long id) {
        if (positionRepository.findById(id).isPresent()) {
            positionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Finds a position by name and returns it as an Optional of PositionDTOGetPostPut.
     *
     * @param name Position name.
     * @return Optional of PositionDTOGetPostPut if found, otherwise empty.
     */
    public Optional<PositionDTOGetPostPut> findByName(String name) {
        Optional<Position> position = positionRepository.findByName(name);
        if (position.isPresent()) {
            PositionDTOGetPostPut positionDTOGetPostPut = new PositionDTOGetPostPut();
            positionDTOGetPostPut.convertToPositionDTO(position.get());
            return Optional.of(positionDTOGetPostPut);
        }
        return Optional.empty();
    }

    /**
     * Updates an existing position in the database based on its name and the provided PositionDTO.
     * Validates if the new position name does not cause duplication before applying the update.
     *
     * @param name        Position name to update.
     * @param positionDTO Position data to update.
     * @return Optional of PositionDTOGetPostPut if updated successfully, otherwise empty.
     */
    public Optional<PositionDTOGetPostPut> updateByName(String name, PositionDTO positionDTO) {
        Optional<Position> existingPosition = positionRepository.findByName(name);
        if (existingPosition.isPresent()) {
            if (!existingPosition.get().getName().equalsIgnoreCase(positionDTO.getName())) {
                if (positionRepository.findByName(positionDTO.getName()).isPresent()) {
                    return Optional.empty();
                }
            }
            Position position = existingPosition.get();
            position.setName(positionDTO.getName());
            position.setDescription(positionDTO.getDescription());
            Position updatedPosition = positionRepository.save(position);

            PositionDTOGetPostPut positionDTOGetPostPut = new PositionDTOGetPostPut();
            positionDTOGetPostPut.convertToPositionDTO(updatedPosition);
            return Optional.of(positionDTOGetPostPut);
        }
        return Optional.empty();
    }

    /**
     * Deletes a position by its name if it exists.
     *
     * @param name Position name to delete.
     * @return True if deletion was successful, false otherwise.
     */
    public boolean deleteByName(String name) {
        if (positionRepository.findByName(name).isPresent()) {
            positionRepository.deleteByName(name);
            return true;
        }
        return false;
    }


}