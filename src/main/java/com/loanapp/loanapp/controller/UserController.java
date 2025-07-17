package com.loanapp.loanapp.controller;
import com.loanapp.loanapp.model.LoanApplication;
import com.loanapp.loanapp.model.SupportRequest;
import com.loanapp.loanapp.model.User;
import com.loanapp.loanapp.repository.LoanRepository;
import com.loanapp.loanapp.repository.SupportRepository;
import com.loanapp.loanapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserRepository userRepo;

    @Autowired
    private final LoanRepository loanRepo;

    @Autowired
    private final SupportRepository supportRepo;

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        String email = authentication.getName(); // ✅ from Spring Security
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }

    // 1. APPLY FOR LOAN
    @PostMapping({"/apply", "/apply/"})
    public ResponseEntity<?> applyLoan(
            @RequestParam String loanType,
            @RequestParam Double amount,
            @RequestParam Integer duration,
            @RequestParam Long userId,
            @RequestParam MultipartFile[] documents
    )throws IOException {
        List<String> docPaths = new ArrayList<>();
        for (MultipartFile doc : documents) {
            if (doc.isEmpty()) continue;
            String filename = UUID.randomUUID() + "_" + doc.getOriginalFilename();
            Path path = Paths.get("uploads", filename);
            Files.createDirectories(path.getParent()); // Ensure directory exists
            Files.write(path, doc.getBytes());
            docPaths.add(path.toString());
        }

        LoanApplication loan = new LoanApplication();
        loan.setUserId(userId);
        loan.setLoanType(loanType);
        loan.setAmount(amount);
        loan.setDuration(duration);
        loan.setStatus("Pending");
        loan.setDocumentPaths(docPaths);

        loanRepo.save(loan);
        return ResponseEntity.ok("Loan application submitted successfully");
    }

    // 2. VIEW MY LOANS
    @GetMapping("/loans")
    public ResponseEntity<?> getMyLoans(@RequestParam Long userId) {
        List<LoanApplication> loans = loanRepo.findByUserId(userId);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/loan/status")
    public ResponseEntity<?> getLoanStatus(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<LoanApplication> loans = loanRepo.findByUserId(user.getId());
        return ResponseEntity.ok(loans);
    }

    // 3. REPAY LOAN
    @PostMapping("/repay/{id}")
    public ResponseEntity<?> repayLoan(@PathVariable Long id) {
        LoanApplication loan = loanRepo.findById(id).orElseThrow();
        if (!loan.getStatus().equalsIgnoreCase("Approved")) {
            return ResponseEntity.badRequest().body("Loan must be approved to repay");
        }
        loan.setStatus("Repaid");
        loanRepo.save(loan);
        return ResponseEntity.ok("Loan marked as repaid");
    }

    // 4. WITHDRAW LOAN
    @PostMapping("/withdraw/{id}")
    public ResponseEntity<?> withdrawLoan(@PathVariable Long id) {
        LoanApplication loan = loanRepo.findById(id).orElseThrow();
        if (!loan.getStatus().equalsIgnoreCase("Approved")) {
            return ResponseEntity.badRequest().body("Loan is not approved");
        }
        loan.setStatus("Withdrawn");
        loanRepo.save(loan);
        return ResponseEntity.ok("Loan successfully withdrawn");
    }

    // 5. UPDATE PROFILE
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody User updatedUser) {
        User user = userRepo.findById(updatedUser.getId()).orElseThrow();
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        userRepo.save(user);
        return ResponseEntity.ok("Profile updated");
    }

    // 6. CONTACT SUPPORT
    @PostMapping("/support")
    public ResponseEntity<?> support(@RequestBody SupportRequest request) {
        supportRepo.save(request);
        return ResponseEntity.ok("Support request submitted");
    }
}

