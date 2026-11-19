package com.example.siperset.service;

import com.example.siperset.model.*;
import com.example.siperset.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class PengajuanService {

    @Autowired private AsetRepository asetRepository;
    @Autowired private JadwalAsetRepository jadwalAsetRepository;
    @Autowired private PengajuanRepository pengajuanRepository;
    @Autowired private DokumenRepository dokumenRepository;
    @Autowired private PersetujuanRepository persetujuanRepository;

    // Seeder Data Awal Otomatis saat aplikasi dijalankan (Sesuai Katalog Figma)
    @PostConstruct
    public void initDataAwal() {
        asetRepository.save(new Aset("AST-01", "Gedung Samantha Krida (SAMKRI)", 4500, 25000000.0));
        asetRepository.save(new Aset("AST-02", "Auditorium Widyaloka", 600, 8500000.0));
        asetRepository.save(new Aset("AST-03", "GOR Pertamina UB", 2000, 12000000.0));
        asetRepository.save(new Aset("AST-04", "Gedung Kebudayaan Mahasiswa (GKM)", 400, 5000000.0));
        asetRepository.save(new Aset("AST-05", "Bus Eksekutif Pariwisata UB", 45, 2200000.0));
        asetRepository.save(new Aset("AST-06", "Gazebo Raden Wijaya", 100, 750000.0));

        // Satu jadwal yang sudah "Dipesan" untuk menguji Alur Gagal A1 (Jadwal Bentrok)
        jadwalAsetRepository.save(new JadwalAset(
                "JDW-001", "AST-01", "PGJ-LAMA",
                LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 12), "Dipesan"
        ));
    }

    public List<Aset> getAllAset() {
        return asetRepository.findAll();
    }

    public List<Pengajuan> getAllPengajuan() {
        return pengajuanRepository.findAll();
    }

    public List<JadwalAset> getAllJadwal() {
        return jadwalAsetRepository.findAll();
    }

    public boolean cekKetersediaan(String idAset, LocalDate mulai, LocalDate selesai) {
        return jadwalAsetRepository.findJadwalBentrok(idAset, mulai, selesai).isEmpty();
    }

    public Double hitungTotalBiaya(Double tarifSewa, LocalDate mulai, LocalDate selesai) {
        return hitungTotalBiaya(tarifSewa, mulai, selesai, null);
    }

    public Double hitungTotalBiaya(Double tarifSewa, LocalDate mulai, LocalDate selesai, String kategori) {
        long durasiHari = ChronoUnit.DAYS.between(mulai, selesai) + 1;
        double total = durasiHari * tarifSewa;
        if ("civitas".equalsIgnoreCase(kategori)) {
            total = total * 0.70; // Subsidi 30% untuk internal civitas
        }
        return total;
    }

    @Transactional
    public Pengajuan submitPengajuan(Pengajuan pengajuan, MultipartFile berkasPdf) {
        return submitPengajuan(pengajuan, berkasPdf, null);
    }

    @Transactional
    public Pengajuan submitPengajuan(Pengajuan pengajuan, MultipartFile berkasPdf, String kategoriPemohon) {
        // 1. Validasi Tanggal (Alur Gagal A2)
        if (pengajuan.getTanggalSelesaiSewa().isBefore(pengajuan.getTanggalMulaiSewa())) {
            throw new IllegalArgumentException("Tanggal selesai sewa tidak boleh mendahului tanggal mulai sewa.");
        }

        // 2. Validasi Berkas PDF maks 10 MB (Alur Gagal A3)
        if (berkasPdf == null || berkasPdf.isEmpty()) {
            throw new IllegalArgumentException("Berkas dokumen persyaratan (PDF) wajib diunggah.");
        }
        String namaFile = berkasPdf.getOriginalFilename();
        if (namaFile == null || !namaFile.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Dokumen persyaratan wajib berformat PDF.");
        }
        if (berkasPdf.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("Ukuran berkas dokumen melebihi batas maksimal 10 MB.");
        }

        // 3. Cek Ketersediaan Jadwal Anti-Bentrok (Alur Gagal A1)
        if (!cekKetersediaan(pengajuan.getIdAset(), pengajuan.getTanggalMulaiSewa(), pengajuan.getTanggalSelesaiSewa())) {
            throw new IllegalStateException("Jadwal aset pada tanggal tersebut sudah terisi (Dipesan). Silakan pilih tanggal lain.");
        }

        // 4. Hitung Total Biaya & Simpan Pengajuan (Alur Utama U7)
        Aset aset = asetRepository.findById(pengajuan.getIdAset())
                .orElseThrow(() -> new IllegalArgumentException("Aset tidak ditemukan."));

        String idBaru = "PGJ-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        pengajuan.setIdPengajuan(idBaru);
        pengajuan.setStatusPengajuan("Diajukan");
        pengajuan.setTotalBiaya(hitungTotalBiaya(aset.getTarifSewa(), pengajuan.getTanggalMulaiSewa(), pengajuan.getTanggalSelesaiSewa(), kategoriPemohon));
        pengajuanRepository.save(pengajuan);

        // 5. Simpan Dokumen dengan status "Menunggu"
        Dokumen dok = new Dokumen("DOK-" + idBaru.substring(4), idBaru, "/uploads/" + namaFile, "Menunggu");
        dokumenRepository.save(dok);

        // 6. Kunci Jadwal Aset menjadi "Dipesan"
        JadwalAset jadwal = new JadwalAset("JDW-" + idBaru.substring(4), aset.getIdAset(), idBaru,
                pengajuan.getTanggalMulaiSewa(), pengajuan.getTanggalSelesaiSewa(), "Dipesan");
        jadwalAsetRepository.save(jadwal);

        // 7. Buat Antrean Persetujuan Baru dengan status "Menunggu"
        Persetujuan persetujuan = new Persetujuan("APR-" + idBaru.substring(4), idBaru, "Humas & Kepala DTUK", "Menunggu");
        persetujuanRepository.save(persetujuan);

        return pengajuan;
    }
}