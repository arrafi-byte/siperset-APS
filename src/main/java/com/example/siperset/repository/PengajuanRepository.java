package com.example.siperset.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.siperset.model.Pengajuan;

@Repository
public interface PengajuanRepository extends JpaRepository<Pengajuan, String> {
}