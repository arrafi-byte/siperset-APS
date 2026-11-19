package com.example.siperset.controller;

import com.example.siperset.model.Pengajuan;
import com.example.siperset.service.PengajuanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/pengajuan")
public class PengajuanController {

    @Autowired
    private PengajuanService pengajuanService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("daftarPengajuan", pengajuanService.getAllPengajuan());
        model.addAttribute("daftarJadwal", pengajuanService.getAllJadwal());
        return "pengajuan/hasil";
    }

    @GetMapping("/tambah")
    public String tampilkanFormPengajuan(Model model) {
        if (!model.containsAttribute("pengajuan")) {
            model.addAttribute("pengajuan", new Pengajuan());
        }
        model.addAttribute("daftarAset", pengajuanService.getAllAset());
        return "pengajuan/form-pengajuan";
    }

    @PostMapping("/simpan")
    public String kirimPengajuan(@Valid @ModelAttribute("pengajuan") Pengajuan pengajuan,
                                 BindingResult result,
                                 @RequestParam("berkas") MultipartFile berkas,
                                 @RequestParam(value = "kategoriPemohon", required = false) String kategoriPemohon,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("daftarAset", pengajuanService.getAllAset());
            String errorMsg = result.getAllErrors().stream()
                    .map(org.springframework.context.support.DefaultMessageSourceResolvable::getDefaultMessage)
                    .findFirst().orElse("Data formulir tidak lengkap atau tidak sesuai.");
            model.addAttribute("pesanError", errorMsg);
            return "pengajuan/form-pengajuan";
        }
        try {
            Pengajuan tersimpan = pengajuanService.submitPengajuan(pengajuan, berkas, kategoriPemohon);
            redirectAttributes.addFlashAttribute("pesanSukses",
                    "Pengajuan berhasil dikirim! Nomor Registrasi: " + tersimpan.getIdPengajuan()
                    + " | Total Biaya: Rp " + String.format("%,.0f", tersimpan.getTotalBiaya()));
            return "redirect:/pengajuan";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("daftarAset", pengajuanService.getAllAset());
            model.addAttribute("pesanError", e.getMessage());
            return "pengajuan/form-pengajuan";
        }
    }

    @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(org.springframework.web.multipart.MaxUploadSizeExceededException exc,
                                         RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("pesanError", "Ukuran berkas dokumen melebihi batas maksimal 10 MB.");
        return "redirect:/pengajuan/tambah";
    }

    // Endpoint tambahan khusus untuk pengujian curl di Subbab 6.7 (JSON)
    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Object> apiSubmit(@RequestBody Map<String, String> req) {
        try {
            String idAset = req.get("idAset");
            String tglMulaiStr = req.get("tanggalMulai");
            String tglSelesaiStr = req.get("tanggalSelesai");
            String kategori = req.get("kategoriPemohon");

            if (idAset == null || idAset.trim().isEmpty() || tglMulaiStr == null || tglSelesaiStr == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status", "gagal", "pesan", "Data isian belum lengkap (idAset, tanggalMulai, dan tanggalSelesai wajib diisi)."));
            }

            java.time.LocalDate mulai;
            java.time.LocalDate selesai;
            try {
                mulai = java.time.LocalDate.parse(tglMulaiStr);
                selesai = java.time.LocalDate.parse(tglSelesaiStr);
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status", "gagal", "pesan", "Format tanggal tidak valid. Gunakan format YYYY-MM-DD."));
            }

            // Alur Gagal A2: Tanggal selesai mendahului tanggal mulai
            if (selesai.isBefore(mulai)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status", "gagal", "pesan", "Tanggal selesai sewa tidak boleh mendahului tanggal mulai sewa."));
            }

            // Alur Gagal A1: Ketersediaan Jadwal Anti-Bentrok
            if (!pengajuanService.cekKetersediaan(idAset, mulai, selesai)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("status", "gagal", "pesan", "Jadwal aset pada tanggal tersebut sudah terisi (Dipesan). Silakan pilih tanggal lain."));
            }

            Pengajuan p = new Pengajuan();
            p.setIdAset(idAset);
            p.setTanggalMulaiSewa(mulai);
            p.setTanggalSelesaiSewa(selesai);

            // Simpan transaksi lengkap ke database H2
            Pengajuan tersimpan = pengajuanService.submitPengajuanApi(p, kategori);

            return ResponseEntity.ok(Map.of(
                    "status", "berhasil",
                    "idPengajuan", tersimpan.getIdPengajuan(),
                    "idAset", tersimpan.getIdAset(),
                    "statusJadwal", "Dipesan",
                    "totalBiaya", tersimpan.getTotalBiaya()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "gagal", "pesan", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("status", "gagal", "pesan", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "pesan", e.getMessage()));
        }
    }
}