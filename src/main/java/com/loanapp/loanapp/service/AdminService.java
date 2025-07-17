package com.loanapp.loanapp.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AdminService {
    public enum Role {
        USER,
        ADMIN
    }

    @Autowired
    PasswordEncoder encoder;

    @PostConstruct
    public void encodePassword() {
        System.out.println("✅ New hash: " + encoder.encode("admin123"));
    }

}
