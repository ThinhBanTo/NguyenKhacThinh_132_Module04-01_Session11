package com.example.warehouseservice.repo;

import com.example.warehouseservice.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugRepository
        extends JpaRepository<Drug, Long> {
}