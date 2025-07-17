package com.loanapp.loanapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

    @Entity
    @Data
    public class SupportRequest {
        @Id
        @GeneratedValue
        private Long id;

        private Long userId;
        private String subject;
        private String message;
    }


