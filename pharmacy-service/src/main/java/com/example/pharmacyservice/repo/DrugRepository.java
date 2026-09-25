package com.example.pharmacyservice.repo;

import com.example.pharmacyservice.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugRepository
        extends JpaRepository<Drug, Long> {
}