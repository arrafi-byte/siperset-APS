# 🏛️ SIPERSET UB — Sistem Informasi Penyewaan Aset & Fasilitas Universitas Brawijaya

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)
[![H2 Database](https://img.shields.io/badge/Database-H2_In--Memory-003B57?style=for-the-badge&logo=sqlite&logoColor=white)](https://www.h2database.com/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3.2-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)](https://getbootstrap.com/)

SIPERSET UB adalah platform web berbasis **Spring Boot & Thymeleaf** yang dirancang untuk memodernisasi dan mendigitalisasi birokrasi peminjaman dan penyewaan aset/fasilitas di lingkungan Universitas Brawijaya. Proyek ini mengimplementasikan alur bisnis berjenjang, validasi jadwal anti-bentrok, penghitungan subsidi otomatis, drag-and-drop berkas dokumen.

---

## 📁 Struktur Repositori

```text
siperset/
├── .github/                                  # Direktori konfigurasi & automasi GitHub
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties        # Konfigurasi versi Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/com/example/siperset/
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java       # Routing root (/) langsung redirect ke form pengajuan
│   │   │   │   └── PengajuanController.java  # Handler form sewa, submit data, pelacakan, & REST API
│   │   │   ├── model/
│   │   │   │   ├── Aset.java                 # Entitas fasilitas kampus & tarif sewa
│   │   │   │   ├── Dokumen.java              # Entitas berkas persyaratan (PDF)
│   │   │   │   ├── JadwalAset.java           # Entitas slot jadwal fasilitas (status: Dipesan)
│   │   │   │   ├── Pengajuan.java            # Entitas utama transaksi peminjaman
│   │   │   │   └── Persetujuan.java          # Entitas alur persetujuan berjenjang
│   │   │   ├── repository/                   # Spring Data JPA Data Access Object (DAO)
│   │   │   │   ├── AsetRepository.java
│   │   │   │   ├── DokumenRepository.java
│   │   │   │   ├── JadwalAsetRepository.java # Query penentu ketersediaan jadwal anti-bentrok
│   │   │   │   ├── PengajuanRepository.java
│   │   │   │   └── PersetujuanRepository.java
│   │   │   ├── service/
│   │   │   │   └── PengajuanService.java     # Logika bisnis, validasi, kalkulasi, & seeding 6 aset
│   │   │   └── SipersetApplication.java      # Entry point aplikasi Spring Boot
│   │   └── resources/
│   │       ├── static/
│   │       │   └── images/
│   │       │       └── logo-ub.png           # Aset visual logo resmi Universitas Brawijaya
│   │       ├── templates/
│   │       │   └── pengajuan/
│   │       │       ├── form-pengajuan.html   # Halaman formulir wizard 4 langkah (Figma)
│   │       │       └── hasil.html            # Halaman tracking & log database (Figma)
│   │       └── application.properties        # Konfigurasi database H2, port, & upload multipart
│   └── test/
│       └── java/com/example/siperset/
│           └── SipersetApplicationTests.java # Unit testing konteks Spring Boot
├── dummy berkas 1.pdf                        # Berkas PDF dummy 1 (untuk uji coba upload proposal)
├── dummy berkas 2.pdf                        # Berkas PDF dummy 2 (untuk uji coba upload surat dekanat)
├── Universitas Brawijaya (UB) Logo - Colored - 254x256 - zonalogo.com.png # Asset gambar logo resmi UB (resolusi tinggi)
├── .gitattributes
├── .gitignore
├── HELP.md                                   # Dokumentasi bawaan Spring Boot
├── mvnw                                      # Script Maven Wrapper untuk Linux/macOS
├── mvnw.cmd                                  # Script Maven Wrapper untuk Windows (PowerShell/CMD)
├── pom.xml                                   # Manajemen dependensi Maven
└── README.md                                 # Dokumentasi utama proyek
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
cd siperset-APS
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
1. **Langkah 1:** Pilih salah satu dari 6 gedung kampus yang tersedia, tentukan rentang tanggal peminjaman (status jadwal akan dicek real-time), dan pilih kategori pemohon.
2. **Langkah 2:** Isi identitas lengkap pemohon dan deskripsi acara.
3. **Langkah 3:** *Drag & drop* atau klik untuk mengunggah dokumen proposal resmi berformat PDF (dapat menggunakan `dummy berkas 1.pdf` dan `dummy berkas 2.pdf` yang sudah tersedia di root proyek).
4. **Langkah 4:** Tinjau rincian estimasi biaya yang telah dikurangi subsidi, lalu klik **Kirim Pengajuan Sewa**.
5. Sistem akan mengunci jadwal fasilitas menjadi **Dipesan** dan menampilkan ID registrasi resmi di halaman pelacakan (*Tracking*).

---

## 🧪 Menjalankan Pengujian Otomatis (Unit Test)

Untuk memastikan seluruh modul, entitas, dan logika bisnis berjalan tanpa kendala:
```powershell
.\mvnw.cmd test
```

