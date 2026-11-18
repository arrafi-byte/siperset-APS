package com.example.siperset.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.siperset.model.Dokumen;

@Repository
public interface DokumenRepository extends JpaRepository<Dokumen, String> {
}