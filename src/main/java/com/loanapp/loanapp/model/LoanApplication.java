package com.loanapp.loanapp.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class LoanApplication {
    @Id
    @GeneratedValue
    private Long id;

    private String loanType;
    private Double amount;
    private Integer duration;
    private String status; // Pending, Approved, Rejected, Withdrawn

    private Long userId;

    @ElementCollection
    private List<String> documentPaths;
}

