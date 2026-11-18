/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.sipreset.model;

import jakarta.persistence.*;

@Entity
@Table(name = "persetujuan")
public class Persetujuan {
    @Id
    @Column(name = "id_persetujuan", length = 20, nullable = false)
    private String idPersetujuan;

    @Column(name = "id_pengajuan", length = 20, nullable = false)
    private String idPengajuan;

    @Column(name = "level_persetujuan", length = 50, nullable = false)
    private String levelPersetujuan;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

    public Persetujuan() {}

    public Persetujuan(String idPersetujuan, String idPengajuan, String levelPersetujuan, String status) {
        this.idPersetujuan = idPersetujuan;
        this.idPengajuan = idPengajuan;
        this.levelPersetujuan = levelPersetujuan;
        this.status = status;
    }

    public String getIdPersetujuan() { return idPersetujuan; }
    public void setIdPersetujuan(String idPersetujuan) { this.idPersetujuan = idPersetujuan; }
    public String getIdPengajuan() { return idPengajuan; }
    public void setIdPengajuan(String idPengajuan) { this.idPengajuan = idPengajuan; }
    public String getLevelPersetujuan() { return levelPersetujuan; }
    public void setLevelPersetujuan(String levelPersetujuan) { this.levelPersetujuan = levelPersetujuan; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}