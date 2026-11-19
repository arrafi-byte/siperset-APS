# 🏛️ SIPERSET UB — Sistem Informasi Penyewaan Aset & Fasilitas Universitas Brawijaya

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)
[![H2 Database](https://img.shields.io/badge/Database-H2_In--Memory-003B57?style=for-the-badge&logo=sqlite&logoColor=white)](https://www.h2database.com/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3.2-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)](https://getbootstrap.com/)

SIPERSET UB adalah platform web berbasis **Spring Boot & Thymeleaf** yang dirancang untuk memodernisasi dan mendigitalisasi birokrasi peminjaman dan penyewaan aset/fasilitas di lingkungan Universitas Brawijaya. Proyek ini mengimplementasikan alur bisnis berjenjang, validasi jadwal anti-bentrok, penghitungan subsidi otomatis.

---

## 📁 Struktur Repositori

```text
siperset/
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties        # Konfigurasi Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/com/example/siperset/
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java       # Routing root (redirect otomatis ke form)
│   │   │   │   └── PengajuanController.java  # Endpoint form pengajuan, simpan, tracking, & API
│   │   │   ├── model/
│   │   │   │   ├── Aset.java                 # Entitas fasilitas kampus & tarif sewa
│   │   │   │   ├── Dokumen.java              # Entitas berkas persyaratan (PDF)
│   │   │   │   ├── JadwalAset.java           # Entitas penguncian jadwal (status: Dipesan)
│   │   │   │   ├── Pengajuan.java            # Entitas utama transaksi peminjaman
│   │   │   │   └── Persetujuan.java          # Entitas alur persetujuan berjenjang
│   │   │   ├── repository/                   # Spring Data JPA Repositories
│   │   │   │   ├── AsetRepository.java
│   │   │   │   ├── DokumenRepository.java
│   │   │   │   ├── JadwalAsetRepository.java # Query deteksi jadwal bentrok
│   │   │   │   ├── PengajuanRepository.java
│   │   │   │   └── PersetujuanRepository.java
│   │   │   ├── service/
│   │   │   │   └── PengajuanService.java     # Logika bisnis, validasi, kalkulasi, & seeding data
│   │   │   └── SipersetApplication.java      # Main class Spring Boot
│   │   └── resources/
│   │       ├── static/
│   │       │   └── images/
│   │       │       └── logo-ub.png           # Aset visual logo resmi Universitas Brawijaya
│   │       ├── templates/
│   │       │   └── Pengajuan/
│   │       │       ├── form-pengajuan.html   # Halaman formulir wizard 4 langkah (Figma)
│   │       │       └── hasil.html            # Halaman tracking & log database (Figma)
│   │       └── application.properties        # Konfigurasi server, multipart file, & H2
│   └── test/
│       └── java/com/example/siperset/
│           └── SipersetApplicationTests.java # Unit testing konteks Spring Boot
├── mvnw                                      # Script wrapper Linux/macOS
├── mvnw.cmd                                  # Script wrapper Windows
├── pom.xml                                   # Manajemen dependensi Maven
└── README.md
```

---

## ⚙️ Prasyarat Sistem (Prerequisites)

Sebelum menjalankan aplikasi, pastikan komputer Anda telah terpasang:
- **Java Development Kit (JDK):** Versi 17 atau lebih baru (disarankan JDK 17 / 21 LTS).
- **Git:** Untuk melakukan *clone* repositori.
- **Web Browser:** Google Chrome, Microsoft Edge, atau Mozilla Firefox.

---

## 🚀 Panduan Instalasi & Menjalankan Aplikasi

### 1. Clone Repositori
Buka terminal (PowerShell / Git Bash / Command Prompt) dan jalankan:
```bash
git clone https://github.com/arrafi-byte/siperset-APS.git
cd siperset-APS/siperset
```

### 2. Menjalankan Aplikasi

#### Opsi A: Melalui Terminal / PowerShell (Direkomendasikan)
- **Windows (PowerShell / CMD):**
  ```powershell
  .\mvnw.cmd spring-boot:run
  ```
- **Linux / macOS:**
  ```bash
  chmod +x mvnw
  ./mvnw spring-boot:run
  ```

#### Opsi B: Melalui Apache NetBeans IDE
1. Buka Apache NetBeans.
2. Pilih menu **File $\rightarrow$ Open Project...** (atau `Ctrl + Shift + O`).
3. Pilih folder proyek `siperset` (ditandai dengan ikon kubus Maven), lalu klik **Open Project**.
4. Buka file `src/main/java/com/example/siperset/SipersetApplication.java`.
5. **Klik kanan** pada file tersebut $\rightarrow$ pilih **Run File** (atau tekan `Shift + F6`).

---

## 🌐 Akses Aplikasi & Alur Penggunaan

Setelah aplikasi berhasil berjalan dengan pesan `Tomcat started on port 8080 (http)`:

| Halaman | URL Akses | Keterangan |
| :--- | :--- | :--- |
| **Beranda & Form Sewa** | [`http://localhost:8080/`](http://localhost:8080/) | Langsung dialihkan ke **Step 1 Formulir Pengajuan Sewa Aset**. |
| **Tracking / Lacak Status** | [`http://localhost:8080/pengajuan`](http://localhost:8080/pengajuan) | Melihat status pengajuan terakhir dan visualisasi stepper 5 tahap. |
| **H2 Database Console** | [`http://localhost:8080/h2-console`](http://localhost:8080/h2-console) | Konsol database in-memory (*JDBC URL:* `jdbc:h2:mem:db_siperset`, *Username:* `sa`, *Password:* [kosong]). |

### Alur Singkat Peminjaman:
1. **Langkah 1:** Pilih salah satu dari 6 gedung kampus yang tersedia, pilih tanggal peminjaman, dan pilih kategori pemohon (*Civitas Academica* otomatis mendapatkan diskon 30%).
2. **Langkah 2:** Isi data pemohon penanggung jawab kegiatan.
3. **Langkah 3:** *Drag & drop* atau klik untuk mengunggah berkas proposal resmi berformat PDF.
4. **Langkah 4:** Tinjau rincian estimasi biaya, lalu klik **Kirim Pengajuan Sewa**.
5. Sistem akan mengunci jadwal fasilitas menjadi **Dipesan** dan menampilkan ID registrasi resmi di halaman pelacakan.

---

## 🧪 Menjalankan Pengujian Otomatis (Unit Test)

Untuk memastikan seluruh modul, entitas, dan logika bisnis berjalan tanpa kendala:
```powershell
.\mvnw.cmd test
```

---
