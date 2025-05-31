package com.rh_systems.employee_service.config;

import com.rh_systems.employee_service.Entity.Employee;
import com.rh_systems.employee_service.Entity.Position;
import com.rh_systems.employee_service.repository.EmployeeRepository;
import com.rh_systems.employee_service.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Component that loads initial data into the database when the application starts.
 */
@Component
public class InitialDataLoader implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor with dependency injection
     */
    @Autowired
    public InitialDataLoader(EmployeeRepository employeeRepository,
                             PositionRepository positionRepository,
                             PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Method that runs on application startup to load initial data
     */
    @Override
    public void run(String... args) throws Exception {
        // Create admin position if it doesn't exist
        Position adminPosition = positionRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> positionRepository.save(new Position(null, "ROLE_ADMIN", "Administrator role", 3000000.0f, null)));

        // Create user position if it doesn't exist
        Position userPosition = positionRepository.findByName("ROLE_USER")
                .orElseGet(() -> positionRepository.save(new Position(null, "ROLE_USER", "Basic user role", 2000000.0f, null)));

        // Create admin user if it doesn't exist
        if (!employeeRepository.existsByEmail("admin@noexiste12345")) {
            Employee admin = new Employee();
            admin.setDni("adminDRRL");
            admin.setName("AdminSofia");
            admin.setLastName("AdminRicardo");
            admin.setAddress("Address calle 18A");
            admin.setEmail("admin@noexiste12345");
            admin.setPhone("3232943634");
            admin.setPassword(passwordEncoder.encode("admin12345"));
            admin.setPosition(adminPosition);
            employeeRepository.save(admin);
        }else{
            System.out.println("Admin with email admin@noexiste12345 already exists");
        }

        // Create basic user if it doesn't exist
        if (!employeeRepository.existsByEmail("user@noexiste12345")) {
            Employee user = new Employee();
            user.setDni("userDRRM");
            user.setName("UserDaniel");
            user.setLastName("UserReina");
            user.setAddress("Address calle #21");
            user.setEmail("user@noexiste12345");
            user.setPhone("3148753513");
            user.setPassword(passwordEncoder.encode("user12345"));
            user.setPosition(userPosition);
            employeeRepository.save(user);
        }else{
            System.out.println("User with email user@noexiste12345 already exists");
        }
    }
}
