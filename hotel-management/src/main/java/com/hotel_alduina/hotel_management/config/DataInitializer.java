package com.hotel_alduina.hotel_management.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hotel_alduina.hotel_management.model.Role;
import com.hotel_alduina.hotel_management.model.User;
import com.hotel_alduina.hotel_management.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Controlla se esiste già un admin
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // Password iniziale
                admin.setRole(Role.GESTORE); // Ruolo admin
                admin.setCitizenship("IT");
                admin.setEmail("admin@hotel.com");
                admin.setFirstName("Alessio");
                admin.setLastName("Alduina");
                userRepository.save(admin);

                System.out.println("Admin creato: username=admin, password=admin123");
            }
        };
    }


 @Bean
    public CommandLineRunner createStaff(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Lista dello staff: username, password, nome, cognome, email
            String[][] staffList = {
                {"staff1", "staff123", "Mario", "Rossi", "staff1@gmail.com"},
                {"staff2", "staff123", "Lucia", "Bianchi", "staff2@gmail.com"},
                {"staff3", "staff123", "Giovanni", "Verdi", "staff3@gmail.com"}
            };

            for (String[] staffData : staffList) {
                String username = staffData[0];
                if (userRepository.findByUsername(username).isEmpty()) {
                    User staff = new User();
                    staff.setUsername(username);
                    staff.setPassword(passwordEncoder.encode(staffData[1]));
                    staff.setRole(Role.PERSONALE);
                    staff.setCitizenship("IT");
                    staff.setEmail(staffData[4]);
                    staff.setFirstName(staffData[2]);
                    staff.setLastName(staffData[3]);
                    userRepository.save(staff);
                    System.out.println("Staff creato: username=" + username + ", password=" + staffData[1]);
                }
            }
        };
    }
}
