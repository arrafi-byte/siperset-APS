Panduan Instalasi & Menjalankan Proyek
1. Clone Repositori
Buka terminal (Git Bash / PowerShell) dan jalankan perintah berikut:

Bash
git clone https://github.com/arrafi-byte/siperset-APS.git
cd siperset-ub-poc
(Sesuaikan URL di atas dengan link repositori GitHub kelompok).

2. Buka di Apache NetBeans
Buka Apache NetBeans.

Pilih File -> Open Project... (Ctrl + Shift + O).

Pilih folder siperset (ditandai dengan ikon kubus Maven) lalu klik Open Project.

Tunggu hingga proses pengunduhan dependensi Maven selesai.

3. Jalankan Aplikasi
Via NetBeans: Klik kanan pada file SipersetApplication.java -> pilih Run File (Shift + F6).

Via Terminal:

Bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
Pastikan muncul log: Tomcat started on port 8080 (http).
