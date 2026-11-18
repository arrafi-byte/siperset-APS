/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.siperset.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Entity
@Table(name = "pengajuan")
public class Pengajuan {
    @Id
    @Column(name = "id_pengajuan", length = 20, nullable = false)
    private String idPengajuan;

    @NotBlank(message = "Fasilitas aset wajib dipilih")
    @Column(name = "id_aset", length = 20, nullable = false)
    private String idAset;

    @NotNull(message = "Tanggal mulai sewa wajib diisi")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "tanggal_mulai_sewa", nullable = false)
    private LocalDate tanggalMulaiSewa;

    @NotNull(message = "Tanggal selesai sewa wajib diisi")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "tanggal_selesai_sewa", nullable = false)
    private LocalDate tanggalSelesaiSewa;

    @Column(name = "status_pengajuan", length = 20, nullable = false)
    private String statusPengajuan;

    @Column(name = "total_biaya", nullable = false)
    private Double totalBiaya;

    public Pengajuan() {}

    public String getIdPengajuan() { return idPengajuan; }
    public void setIdPengajuan(String idPengajuan) { this.idPengajuan = idPengajuan; }
    public String getIdAset() { return idAset; }
    public void setIdAset(String idAset) { this.idAset = idAset; }
    public LocalDate getTanggalMulaiSewa() { return tanggalMulaiSewa; }
    public void setTanggalMulaiSewa(LocalDate tanggalMulaiSewa) { this.tanggalMulaiSewa = tanggalMulaiSewa; }
    public LocalDate getTanggalSelesaiSewa() { return tanggalSelesaiSewa; }
    public void setTanggalSelesaiSewa(LocalDate tanggalSelesaiSewa) { this.tanggalSelesaiSewa = tanggalSelesaiSewa; }
    public String getStatusPengajuan() { return statusPengajuan; }
    public void setStatusPengajuan(String statusPengajuan) { this.statusPengajuan = statusPengajuan; }
    public Double getTotalBiaya() { return totalBiaya; }
    public void setTotalBiaya(Double totalBiaya) { this.totalBiaya = totalBiaya; }
}