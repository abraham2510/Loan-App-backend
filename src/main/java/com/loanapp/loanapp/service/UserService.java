package com.loanapp.loanapp.service;

import com.loanapp.loanapp.model.Role;
import jakarta.persistence.*;
import lombok.Data;

public class UserService {
    @Entity
    @Data
    public class User {
        @Id
        @GeneratedValue
        private Long id;

        private String name;
        private String email;
        private String password;

        @Enumerated(EnumType.STRING)
        private Role role;
    }

}
