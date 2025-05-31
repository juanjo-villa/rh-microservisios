package com.rh_systems.employee_service.repository;
import com.rh_systems.employee_service.Entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Position entity that provides CRUD operations and custom queries
 */
@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    /**
     * Finds a position by its name
     *
     * @param name The position name to search for
     * @return Optional containing the position if found, empty otherwise
     */
    Optional<Position> findByName(String name);

    /**
     * Checks if a position exists with the given name
     *
     * @param name The position name to check
     * @return true if position exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Deletes a position by its name
     *
     * @param name The name of the position to delete
     */
    void deleteByName(String name);

    /**
     * Counts how many positions have the given name
     *
     * @param name The position name to count
     * @return Number of positions with that name
     */
    Long countByName(String name);
}