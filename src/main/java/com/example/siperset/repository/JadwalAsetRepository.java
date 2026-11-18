package com.example.siperset.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.siperset.model.JadwalAset;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JadwalAsetRepository extends JpaRepository<JadwalAset, String> {

    @Query("SELECT j FROM JadwalAset j WHERE j.idAset = :idAset " +
           "AND j.statusKetersediaan <> 'Tersedia' " +
           "AND j.tanggalMulai <= :selesai AND j.tanggalSelesai >= :mulai")
    List<JadwalAset> findJadwalBentrok(@Param("idAset") String idAset,
                                       @Param("mulai") LocalDate mulai,
                                       @Param("selesai") LocalDate selesai);
}