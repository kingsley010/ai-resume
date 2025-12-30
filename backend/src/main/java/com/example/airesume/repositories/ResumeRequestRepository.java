package com.example.airesume.repositories;

import com.example.airesume.entities.ResumeRequest;
import com.example.airesume.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.UUID;

public interface ResumeRequestRepository extends JpaRepository<ResumeRequest, UUID> {
    long countByUserAndCreatedAtAfter(User user, Instant after);
}
