package com.example.siperset;

import com.example.siperset.controller.PengajuanController;
import com.example.siperset.model.*;
import com.example.siperset.repository.*;
import com.example.siperset.service.PengajuanService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SipersetApplicationTests {

    @Autowired
    private PengajuanService pengajuanService;

    @Autowired
    private PengajuanController pengajuanController;

    @Autowired
    private PengajuanRepository pengajuanRepository;

    @Autowired
    private DokumenRepository dokumenRepository;

    @Autowired
    private JadwalAsetRepository jadwalAsetRepository;

    @Autowired
    private PersetujuanRepository persetujuanRepository;

    @Test
    @DisplayName("Context loads successfully")
    void contextLoads() {
        assertNotNull(pengajuanService);
        assertNotNull(pengajuanController);
    }

    @Test
    @DisplayName("Alur Utama UC-01: Submit pengajuan valid tersimpan di 4 tabel")
    void testAlurUtama_UC01() {
        Pengajuan p = new Pengajuan();
        p.setIdAset("AST-02");
        p.setTanggalMulaiSewa(LocalDate.of(2026, 11, 25));
        p.setTanggalSelesaiSewa(LocalDate.of(2026, 11, 26));

        MockMultipartFile file = new MockMultipartFile(
                "berkas", "proposal_resmi.pdf", "application/pdf", "Isi Berkas Proposal".getBytes()
        );

        Pengajuan hasil = pengajuanService.submitPengajuan(p, file, "civitas");

        assertNotNull(hasil.getIdPengajuan());
        assertTrue(hasil.getIdPengajuan().startsWith("PGJ-"));
        assertEquals("Diajukan", hasil.getStatusPengajuan());

        // Verifikasi 4 tabel
        assertTrue(pengajuanRepository.existsById(hasil.getIdPengajuan()));
        assertTrue(dokumenRepository.findAll().stream().anyMatch(d -> d.getIdPengajuan().equals(hasil.getIdPengajuan())));
        assertTrue(jadwalAsetRepository.findAll().stream().anyMatch(j -> j.getIdPengajuan() != null && j.getIdPengajuan().equals(hasil.getIdPengajuan()) && "Dipesan".equals(j.getStatusKetersediaan())));
        assertTrue(persetujuanRepository.findAll().stream().anyMatch(pr -> pr.getIdPengajuan().equals(hasil.getIdPengajuan())));
    }

    @Test
    @DisplayName("Alur Gagal A1: Penolakan jadwal bentrok (AST-01 terisi 10-12 Des 2026)")
    void testAlurGagal_A1_JadwalBentrok() {
        Pengajuan p = new Pengajuan();
        p.setIdAset("AST-01");
        p.setTanggalMulaiSewa(LocalDate.of(2026, 12, 11));
        p.setTanggalSelesaiSewa(LocalDate.of(2026, 12, 11));

        MockMultipartFile file = new MockMultipartFile(
                "berkas", "proposal.pdf", "application/pdf", "Dummy PDF Content".getBytes()
        );

        assertThrows(IllegalStateException.class, () -> {
            pengajuanService.submitPengajuan(p, file, null);
        });
    }

    @Test
    @DisplayName("Alur Gagal A2: Tanggal selesai mendahului tanggal mulai")
    void testAlurGagal_A2_TanggalTidakValid() {
        Pengajuan p = new Pengajuan();
        p.setIdAset("AST-03");
        p.setTanggalMulaiSewa(LocalDate.of(2026, 11, 20));
        p.setTanggalSelesaiSewa(LocalDate.of(2026, 11, 18));

        MockMultipartFile file = new MockMultipartFile(
                "berkas", "proposal.pdf", "application/pdf", "Dummy PDF Content".getBytes()
        );

        assertThrows(IllegalArgumentException.class, () -> {
            pengajuanService.submitPengajuan(p, file, null);
        });
    }

    @Test
    @DisplayName("Alur Gagal A3: Berkas bukan PDF atau kosong")
    void testAlurGagal_A3_BerkasTidakValid() {
        Pengajuan p = new Pengajuan();
        p.setIdAset("AST-04");
        p.setTanggalMulaiSewa(LocalDate.of(2026, 11, 28));
        p.setTanggalSelesaiSewa(LocalDate.of(2026, 11, 28));

        // Bukan PDF
        MockMultipartFile nonPdf = new MockMultipartFile(
                "berkas", "proposal.docx", "application/msword", "Dummy Docx".getBytes()
        );
        assertThrows(IllegalArgumentException.class, () -> {
            pengajuanService.submitPengajuan(p, nonPdf, null);
        });

        // Berkas kosong
        MockMultipartFile emptyFile = new MockMultipartFile(
                "berkas", "kosong.pdf", "application/pdf", new byte[0]
        );
        assertThrows(IllegalArgumentException.class, () -> {
            pengajuanService.submitPengajuan(p, emptyFile, null);
        });
    }

    @Test
    @DisplayName("Uji API Submit curl: Request pertama 200 OK, request kedua 409 Conflict, tanggal tidak valid 400 Bad Request")
    void testApiSubmitEndpoint() {
        // 1. Alur Gagal A2 via API (Bad Request)
        ResponseEntity<Object> badReq = pengajuanController.apiSubmit(Map.of(
                "idAset", "AST-06",
                "tanggalMulai", "2026-11-20",
                "tanggalSelesai", "2026-11-19"
        ));
        assertEquals(HttpStatus.BAD_REQUEST, badReq.getStatusCode());

        // 2. Request Pertama (200 OK)
        ResponseEntity<Object> res1 = pengajuanController.apiSubmit(Map.of(
                "idAset", "AST-06",
                "tanggalMulai", "2026-11-22",
                "tanggalSelesai", "2026-11-22"
        ));
        assertEquals(HttpStatus.OK, res1.getStatusCode());

        // 3. Request Kedua pada aset & tanggal yang sama -> Wajib 409 Conflict (Alur Gagal A1)
        ResponseEntity<Object> res2 = pengajuanController.apiSubmit(Map.of(
                "idAset", "AST-06",
                "tanggalMulai", "2026-11-22",
                "tanggalSelesai", "2026-11-22"
        ));
        assertEquals(HttpStatus.CONFLICT, res2.getStatusCode());
    }
}
