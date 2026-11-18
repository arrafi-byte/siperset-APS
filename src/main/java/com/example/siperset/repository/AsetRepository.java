package com.example.siperset.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.siperset.model.Aset;

@Repository
public interface AsetRepository extends JpaRepository<Aset, String> {
}