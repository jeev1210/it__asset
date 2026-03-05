// src/main/java/com/example/itassetmanagement/config/UserDetailsServiceImpl.java

package com.example.itassetmanagement.config;

import com.example.itassetmanagement.model.Employee;
import com.example.itassetmanagement.model.enums.EmployeeStatus;
import com.example.itassetmanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // KEY FIX: Only APPROVED users can login
        boolean enabled = employee.getStatus() == EmployeeStatus.APPROVED;

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + employee.getRole().name());

        return new User(
                employee.getEmail(),
                employee.getPassword(),
                enabled,                    // ← blocks PENDING users
                true,                       // account non-expired
                true,                       // credentials non-expired
                true,                       // account non-locked
                List.of(authority)
        );
    }
}