package com.example.siperset.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.siperset.model.Persetujuan;

@Repository
public interface PersetujuanRepository extends JpaRepository<Persetujuan, String> {
}