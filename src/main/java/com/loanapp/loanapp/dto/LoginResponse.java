package com.loanapp.loanapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


    @Data
    @AllArgsConstructor
    public class LoginResponse {
        private String role;
        private String message;
    }
