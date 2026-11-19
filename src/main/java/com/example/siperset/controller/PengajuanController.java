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
        model.addAttribute("pengajuan", new Pengajuan());
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

    // Endpoint tambahan khusus untuk pengujian curl di Subbab 6.7 (JSON)
    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Object> apiSubmit(@RequestBody Map<String, String> req) {
        try {
            Pengajuan p = new Pengajuan();
            p.setIdAset(req.get("idAset"));
            p.setTanggalMulaiSewa(java.time.LocalDate.parse(req.get("tanggalMulai")));
            p.setTanggalSelesaiSewa(java.time.LocalDate.parse(req.get("tanggalSelesai")));

            if (!pengajuanService.cekKetersediaan(p.getIdAset(), p.getTanggalMulaiSewa(), p.getTanggalSelesaiSewa())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("status", "gagal", "pesan", "Jadwal aset pada tanggal tersebut sudah terisi"));
            }
            return ResponseEntity.ok(Map.of("status", "berhasil", "idAset", p.getIdAset(), "statusJadwal", "Dipesan"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "gagal", "pesan", e.getMessage()));
        }
    }
}