/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.sipreset.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "jadwal_aset")
public class JadwalAset {
    @Id
    @Column(name = "id_jadwal", length = 20, nullable = false)
    private String idJadwal;

    @Column(name = "id_aset", length = 20, nullable = false)
    private String idAset;

    @Column(name = "id_pengajuan", length = 20)
    private String idPengajuan;

    @Column(name = "tanggal_mulai", nullable = false)
    private LocalDate tanggalMulai;

    @Column(name = "tanggal_selesai", nullable = false)
    private LocalDate tanggalSelesai;

    @Column(name = "status_ketersediaan", length = 20, nullable = false)
    private String statusKetersediaan;

    public JadwalAset() {}

    public JadwalAset(String idJadwal, String idAset, String idPengajuan,
                      LocalDate tanggalMulai, LocalDate tanggalSelesai, String statusKetersediaan) {
        this.idJadwal = idJadwal;
        this.idAset = idAset;
        this.idPengajuan = idPengajuan;
        this.tanggalMulai = tanggalMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.statusKetersediaan = statusKetersediaan;
    }

    public String getIdJadwal() { return idJadwal; }
    public void setIdJadwal(String idJadwal) { this.idJadwal = idJadwal; }
    public String getIdAset() { return idAset; }
    public void setIdAset(String idAset) { this.idAset = idAset; }
    public String getIdPengajuan() { return idPengajuan; }
    public void setIdPengajuan(String idPengajuan) { this.idPengajuan = idPengajuan; }
    public LocalDate getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(LocalDate tanggalMulai) { this.tanggalMulai = tanggalMulai; }
    public LocalDate getTanggalSelesai() { return tanggalSelesai; }
    public void setTanggalSelesai(LocalDate tanggalSelesai) { this.tanggalSelesai = tanggalSelesai; }
    public String getStatusKetersediaan() { return statusKetersediaan; }
    public void setStatusKetersediaan(String statusKetersediaan) { this.statusKetersediaan = statusKetersediaan; }
}