/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.sipreset.model;

import jakarta.persistence.*;

@Entity
@Table(name = "aset")
public class Aset {
    @Id
    @Column(name = "id_aset", length = 20, nullable = false)
    private String idAset;

    @Column(name = "nama_aset", length = 100, nullable = false)
    private String namaAset;

    @Column(nullable = false)
    private Integer kapasitas;

    @Column(name = "tarif_sewa", nullable = false)
    private Double tarifSewa;

    public Aset() {}

    public Aset(String idAset, String namaAset, Integer kapasitas, Double tarifSewa) {
        this.idAset = idAset;
        this.namaAset = namaAset;
        this.kapasitas = kapasitas;
        this.tarifSewa = tarifSewa;
    }

    public String getIdAset() { return idAset; }
    public void setIdAset(String idAset) { this.idAset = idAset; }
    public String getNamaAset() { return namaAset; }
    public void setNamaAset(String namaAset) { this.namaAset = namaAset; }
    public Integer getKapasitas() { return kapasitas; }
    public void setKapasitas(Integer kapasitas) { this.kapasitas = kapasitas; }
    public Double getTarifSewa() { return tarifSewa; }
    public void setTarifSewa(Double tarifSewa) { this.tarifSewa = tarifSewa; }
}