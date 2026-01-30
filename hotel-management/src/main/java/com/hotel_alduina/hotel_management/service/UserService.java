package com.hotel_alduina.hotel_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.hotel_alduina.hotel_management.dto.RegistrationForm;
import com.hotel_alduina.hotel_management.model.Role;
import com.hotel_alduina.hotel_management.model.User;
import com.hotel_alduina.hotel_management.repository.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registraCliente(RegistrationForm form) {

        User user = new User();

        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setCitizenship(form.getCitizenship()); 

        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setRole(Role.CLIENTE);

        return userRepository.save(user);
    }




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow( () -> new UsernameNotFoundException("Utente non trovate: " + username));
    
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

}
