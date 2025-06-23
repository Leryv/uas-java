package view;

import model.*;
import service.*;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

public class MainView {
    Scanner scan = new Scanner(System.in);

    MahasiswaService mahasiswaService = new MahasiswaServiceImpl();
    PembayaranService pembayaranService = new PembayaranServiceImpl();
    AuthService authService = new AuthServiceImpl();

    User currentUser;
    boolean isKeluar;

    public MainView() {
        initDataAwal();
        authService.registerAdmin(); // Admin default
    }

    private void initDataAwal() {
        // Admin default
        User admin = new User("admin", "admin123", Role.ADMIN, null);
        authService.tambahUser(admin);

        // Mahasiswa + akun student
        Mahasiswa m = new Mahasiswa("Andika", "123", Grade.A);
        mahasiswaService.tambahMahasiswa(m);

        User student = new User("andika", "123", Role.STUDENT, m);
        authService.tambahUser(student);

        System.out.println("✅ Data awal berhasil dimuat.");
    }

    private void bayarSemester() {
        Mahasiswa m = null;

        if (currentUser.getRole() == Role.ADMIN) {
            System.out.print("NIM Mahasiswa: ");
            String nim = scan.nextLine();
            m = mahasiswaService.cariMahasiswa(nim);

            if (m == null) {
                System.out.println("❌ Mahasiswa tidak ditemukan.");
                return;
            }
        } else if (currentUser.getRole() == Role.STUDENT) {
            m = currentUser.getMahasiswaRef();
        }

        // Minta input semester
        System.out.print("Semester pembayaran (1-8): ");
        int semester;
        try {
            semester = Integer.parseInt(scan.nextLine());
            if (semester < 1 || semester > 8) {
                System.out.println("❌ Semester harus antara 1-8.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Input semester tidak valid.");
            return;
        }

        // Tampilkan tagihan & status
        tampilkanTagihan(m);

        // Masukkan jumlah pembayaran
        System.out.print("Masukkan jumlah pembayaran: ");
        double jumlah;
        try {
            jumlah = Double.parseDouble(scan.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Input tidak valid. Masukkan angka.");
            return;
        }

        // Validasi pembayaran
        double sisa = pembayaranService.getSisaTagihan(m);
        if (jumlah <= 0) {
            System.out.println("❌ Jumlah harus lebih dari 0.");
            return;
        }
        if (jumlah > sisa) {
            System.out.println("❌ Pembayaran melebihi sisa tagihan. Sisa: Rp " + sisa);
            return;
        }

        // Metode pembayaran
        System.out.print("Metode (kontan/cicil): ");
        String metode = scan.nextLine().toLowerCase();
        if (!metode.equals("kontan") && !metode.equals("cicil")) {
            System.out.println("❌ Metode harus 'kontan' atau 'cicil'.");
            return;
        }

        // Simpan pembayaran
        pembayaranService.bayar(m, jumlah, metode, semester);
        System.out.println("✅ Pembayaran berhasil dicatat untuk semester " + semester);
    }

    private void lihatSemuaMahasiswa() {
        List<Mahasiswa> semua = mahasiswaService.getSemuaMahasiswa();
        if (semua.isEmpty()) {
            System.out.println("Belum ada mahasiswa.");
            return;
        }

        // Cetak header tabel
        System.out.println("=====================================================================================");
        System.out.printf("%-20s %-15s %-10s %-15s %-15s %-15s%n",
                "Nama", "NIM", "Grade", "Total Bayar", "Sisa Tagihan", "Status");
        System.out.println("=====================================================================================");

        // Cetak data per mahasiswa
        for (Mahasiswa m : semua) {
            String status = pembayaranService.isLunas(m) ? "✅ LUNAS" : "❌ BELUM LUNAS";

            System.out.printf("%-20s %-15s %-10s %-15.2f %-15.2f %-15s%n",
                    m.getNama(),
                    m.getNim(),
                    m.getGrade().toString(), // Asumsikan Grade memiliki toString()
                    pembayaranService.getTotalPembayaran(m),
                    pembayaranService.getSisaTagihan(m),
                    status);
        }
        System.out.println("=====================================================================================");
    }

    public void menuAwal() {
        while (!isKeluar) {
            System.out.println("\n=== LOGIN ===");
            System.out.print("Username: ");
            String username = scan.nextLine();
            System.out.print("Password: ");
            String password = scan.nextLine();

            currentUser = authService.login(username, password);
            if (currentUser == null) {
                System.out.println("Login gagal. Coba lagi.");
            } else {
                System.out.println("Login berhasil sebagai " + currentUser.getRole());

                // Masuk ke menu sesuai role
                if (currentUser.getRole() == Role.ADMIN) {
                    menuAdmin();
                } else if (currentUser.getRole() == Role.STUDENT) {
                    menuStudent();
                }

                // ✅ Setiap selesai dari menuAdmin/menuStudent (karena logout), tanya:
                while (true) {
                    System.out.print("Ingin login kembali? (y/n): ");
                    String jawab = scan.nextLine().trim().toLowerCase();

                    if (jawab.equals("n")) {
                        isKeluar = true;
                        System.out.println("👋 Program selesai. Terima kasih.");
                        return; // keluar dari menuAwal
                    } else if (jawab.equals("y")) {
                        break; // kembali ke awal while (!isKeluar)
                    } else {
                        System.out.println("Jawaban tidak valid. Ketik y atau n.");
                    }
                }
            }
        }
    }

    public void menuAdmin() {
        while (true) {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1. Tambah Mahasiswa + Akun");
            System.out.println("2. Lihat Semua Mahasiswa");
            System.out.println("3. Lihat Semua Pembayaran"); // UBAH MENU INI
            System.out.println("0. Logout");
            System.out.print("Pilih: ");
            int pilih = Integer.parseInt(scan.nextLine());

            switch (pilih) {
                case 1:
                    tambahMahasiswaSekaligusUser();
                    break;
                case 2:
                    lihatSemuaMahasiswa();
                    break;
                case 3:
                    lihatSemuaPembayaran(); // UBAH KE METHOD BARU
                    break;
                case 0:
                    currentUser = null;
                    System.out.println("🔒 Berhasil logout.\n");
                    return;
                default:
                    System.out.println("Pilihan tidak valid");
            }
        }
    }

    private void lihatSemuaPembayaran() {
        List<Pembayaran> semuaPembayaran = mahasiswaService.getAllPembayaran();

        if (semuaPembayaran.isEmpty()) {
            System.out.println("Belum ada pembayaran.");
            return;
        }

        System.out.println("======================================================================================");
        System.out.printf("%-12s %-20s %-15s %-15s %-10s %-10s%n",
                "Tanggal", "Nama Mahasiswa", "NIM", "Jumlah", "Metode", "Semester");
        System.out.println("======================================================================================");

        for (Pembayaran p : semuaPembayaran) {
            Mahasiswa m = cariMahasiswaDariPembayaran(p);
            if (m != null) {
                System.out.printf("%-12s %-20s %-15s %-15.2f %-10s %-10d%n",
                        p.getTanggal(),
                        m.getNama(),
                        m.getNim(),
                        p.getJumlah(),
                        p.getMetode(),
                        p.getSemester()); // Tampilkan semester
            }
        }
        System.out.println("======================================================================================");
    }

    // METHOD PEMBANTU UNTUK MENCARI MAHASISWA DARI PEMBAYARAN
    private Mahasiswa cariMahasiswaDariPembayaran(Pembayaran p) {
        for (Mahasiswa m : mahasiswaService.getSemuaMahasiswa()) {
            if (m.getPembayaranList().contains(p)) {
                return m;
            }
        }
        return null;
    }

    // Method pembantu untuk menghitung total per semester
    private double hitungTotalPerSemester(Mahasiswa m, int semester) {
        return m.getPembayaranList().stream()
                .filter(p -> p.getSemester() == semester)
                .mapToDouble(Pembayaran::getJumlah)
                .sum();
    }

    private String formatRupiah(double amount) {
        // Menggunakan DecimalFormat untuk format mata uang Indonesia
        DecimalFormat formatter = new DecimalFormat("Rp.#,###");
        formatter.setDecimalSeparatorAlwaysShown(false);
        formatter.setGroupingUsed(true);
        formatter.setGroupingSize(3);

        // Format angka dan ganti koma dengan titik
        String formatted = formatter.format(amount);
        return formatted.replace(',', '.');
    }

    public void menuStudent() {
        Mahasiswa m = currentUser.getMahasiswaRef();

        // Tampilkan status per semester
        System.out.println("\nStatus Pembayaran per Semester:");
        for (int semester = 1; semester <= 8; semester++) {
            double totalPerSemester = hitungTotalPerSemester(m, semester);
            double tagihanSemester = m.getGrade().getBiayaSemester();
            boolean lunasPerSemester = totalPerSemester >= tagihanSemester;

            System.out.printf("Semester %d: %s (Rp %,.2f / Rp %,.2f)%n",
                    semester,
                    lunasPerSemester ? "✅ LUNAS" : "❌ BELUM LUNAS",
                    totalPerSemester,
                    tagihanSemester);
        }

        while (true) {
            System.out.println("\n=== MENU MAHASISWA ===");
            System.out.println("1. Bayar Semester");
            System.out.println("2. Lihat Status Pembayaran");
            System.out.println("0. Logout");
            System.out.print("Pilih: ");
            int pilih = Integer.parseInt(scan.nextLine());

            switch (pilih) {
                case 1:
                    bayarSemester();
                    break;
                case 2:
                    lihatStatusPembayaran(m);
                    break;
                case 0:
                    currentUser = null;
                    System.out.println("🔒 Berhasil logout.\n");
                    return;
                default:
                    System.out.println("Pilihan tidak valid");
            }
        }
    }

    private void tambahMahasiswaSekaligusUser() {
        System.out.print("Nama: ");
        String nama = scan.nextLine();
        System.out.print("NIM: ");
        String nim = scan.nextLine();
        System.out.print("Grade (A/B/C/D): ");
        String g = scan.nextLine().toUpperCase();
        Grade grade = Grade.valueOf(g);

        Mahasiswa m = new Mahasiswa(nama, nim, grade);
        mahasiswaService.tambahMahasiswa(m);

        System.out.print("Username untuk mahasiswa: ");
        String username = scan.nextLine();
        System.out.print("Password: ");
        String password = scan.nextLine();

        User u = new User(username, password, Role.STUDENT, m);
        authService.tambahUser(u);

        System.out.println("Mahasiswa + user berhasil ditambahkan.");
    }

    private void lihatStatusPembayaran(Mahasiswa m) {
        System.out.println("Nama: " + m.getNama());
        System.out.println("NIM: " + m.getNim());
        System.out.println("Grade: " + m.getGrade());
        System.out.println("Total Tagihan: " + m.getGrade().getBiayaSemester());
        System.out.println("Total Bayar: " + pembayaranService.getTotalPembayaran(m));
        System.out.println("Sisa Tagihan: " + pembayaranService.getSisaTagihan(m));
        System.out.println("Status: " + (pembayaranService.isLunas(m) ? "✅ LUNAS" : "❌ BELUM LUNAS"));

        System.out.println("\nRiwayat Pembayaran:");
        for (Pembayaran p : m.getPembayaranList()) {
            System.out.println("- Semester " + p.getSemester() + " | " +
                    p.getTanggal() + " | " +
                    p.getMetode() + " | Rp " + p.getJumlah());
        }
    }

    private void tampilkanTagihan(Mahasiswa m) {
        System.out.println("Tagihan: Rp " + m.getGrade().getBiayaSemester());
        double sudahBayar = pembayaranService.getTotalPembayaran(m);
        System.out.println("Sudah Bayar: Rp " + sudahBayar);
        System.out.println("Sisa: Rp " + pembayaranService.getSisaTagihan(m));
    }

}