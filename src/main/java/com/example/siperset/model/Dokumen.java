/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.siperset.model;

import jakarta.persistence.*;

@Entity
@Table(name = "dokumen")
public class Dokumen {
    @Id
    @Column(name = "id_dokumen", length = 20, nullable = false)
    private String idDokumen;

    @Column(name = "id_pengajuan", length = 20, nullable = false)
    private String idPengajuan;

    @Column(name = "path_file", length = 255, nullable = false)
    private String pathFile;

    @Column(name = "status_verifikasi", length = 20, nullable = false)
    private String statusVerifikasi;

    public Dokumen() {}

    public Dokumen(String idDokumen, String idPengajuan, String pathFile, String statusVerifikasi) {
        this.idDokumen = idDokumen;
        this.idPengajuan = idPengajuan;
        this.pathFile = pathFile;
        this.statusVerifikasi = statusVerifikasi;
    }

    public String getIdDokumen() { return idDokumen; }
    public void setIdDokumen(String idDokumen) { this.idDokumen = idDokumen; }
    public String getIdPengajuan() { return idPengajuan; }
    public void setIdPengajuan(String idPengajuan) { this.idPengajuan = idPengajuan; }
    public String getPathFile() { return pathFile; }
    public void setPathFile(String pathFile) { this.pathFile = pathFile; }
    public String getStatusVerifikasi() { return statusVerifikasi; }
    public void setStatusVerifikasi(String statusVerifikasi) { this.statusVerifikasi = statusVerifikasi; }
}