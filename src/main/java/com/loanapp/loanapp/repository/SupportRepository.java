package com.loanapp.loanapp.repository;

import com.loanapp.loanapp.model.SupportRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportRepository extends JpaRepository<SupportRequest, Long> {
    List<SupportRequest> findByUserId(Long userId);
}
