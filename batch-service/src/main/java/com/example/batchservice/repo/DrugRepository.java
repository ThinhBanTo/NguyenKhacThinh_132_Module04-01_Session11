package com.example.batchservice.repo;

import com.example.batchservice.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DrugRepository extends JpaRepository<Drug, Long> {
    List<Drug> findByExpiryDateBetween(
            LocalDate start,
            LocalDate end
    );
}
