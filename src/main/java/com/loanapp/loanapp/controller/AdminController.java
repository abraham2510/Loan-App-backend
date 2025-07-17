package com.loanapp.loanapp.controller;

import com.loanapp.loanapp.model.LoanApplication;
import com.loanapp.loanapp.model.SupportRequest;
import com.loanapp.loanapp.model.User;
import com.loanapp.loanapp.repository.LoanRepository;
import com.loanapp.loanapp.repository.SupportRepository;
import com.loanapp.loanapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final LoanRepository loanRepo;
    private final UserRepository userRepo;
    private final SupportRepository supportRepo;

    // 🔍 1. View all loan applications
    @GetMapping("/loans")
    public ResponseEntity<List<LoanApplication>> getAllLoanApplications() {
        return ResponseEntity.ok(loanRepo.findAll());
    }

    // ✅ 2. Approve a loan
    @PutMapping("/approve/{loanId}")
    public ResponseEntity<?> approveLoan(@PathVariable Long loanId) {
        LoanApplication loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        loan.setStatus("Approved");
        loanRepo.save(loan);
        return ResponseEntity.ok("Loan approved");
    }

    // ❌ 3. Reject a loan
    @PostMapping("/reject/{loanId}")
    public ResponseEntity<?> rejectLoan(@PathVariable Long loanId) {
        LoanApplication loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        loan.setStatus("Rejected");
        loanRepo.save(loan);
        return ResponseEntity.ok("Loan rejected");
    }

    // 👥 4. View all registered users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    // 📞 5. View all support requests
    @GetMapping("/support-requests")
    public ResponseEntity<List<SupportRequest>> getAllSupportRequests() {
        return ResponseEntity.ok(supportRepo.findAll());
    }

    @DeleteMapping("/support-requests/{requestId}")
    public ResponseEntity<Void> deleteSupportRequest(@PathVariable Long requestId) {
        if (!supportRepo.existsById(requestId)) {
            return ResponseEntity.notFound().build();
        }
        supportRepo.deleteById(requestId);
        return ResponseEntity.noContent().build();
    }
}

