package view;

import model.*;
import service.*;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

public class AdminView {
    // private final User currentUser;
    private final MainView mainView;
    private final AuthService authService;
    private final MahasiswaService mahasiswaService;
    private final PembayaranService pembayaranService;
    private final Scanner scan = new Scanner(System.in);

    public AdminView(
            MainView mainView,
            AuthService authService,
            MahasiswaService mahasiswaService,
            PembayaranService pembayaranService) {
        this.mainView = mainView;
        this.authService = authService;
        this.mahasiswaService = mahasiswaService;
        this.pembayaranService = pembayaranService;
    }

    public void menuAdmin() {
        while (true) {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1. Tambah Mahasiswa + Akun");
            System.out.println("2. Lihat Semua Mahasiswa");
            System.out.println("3. Lihat Semua Pembayaran");
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
                    lihatSemuaPembayaran();
                    break;
                case 0:
                    mainView.setCurrentUser(null);
                    System.out.println("Berhasil logout.\n");
                    return;
                default:
                    System.out.println("Pilihan tidak valid");
            }
        }
    }

    private void lihatSemuaMahasiswa() {
        List<Mahasiswa> semua = mahasiswaService.getSemuaMahasiswa();
        if (semua.isEmpty()) {
            System.out.println("Belum ada mahasiswa.");
            return;
        }

        System.out.println("=====================================================================================");
        System.out.printf("%-20s %-15s %-10s %-15s %-15s %-15s%n",
                "Nama", "NIM", "Grade", "Total Bayar", "Sisa Tagihan", "Status");
        System.out.println("=====================================================================================");

        for (Mahasiswa m : semua) {
            String status = pembayaranService.isLunas(m) ? "LUNAS" : "BELUM LUNAS";

            System.out.printf("%-20s %-15s %-10s %-15.2f %-15.2f %-15s%n",
                    m.getNama(),
                    m.getNim(),
                    m.getGrade().toString(),
                    pembayaranService.getTotalPembayaran(m),
                    pembayaranService.getSisaTagihan(m),
                    status);
        }
        System.out.println("=====================================================================================");
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
                        p.getSemester());
            }
        }
        System.out.println("======================================================================================");
    }

    private Mahasiswa cariMahasiswaDariPembayaran(Pembayaran p) {
        for (Mahasiswa m : mahasiswaService.getSemuaMahasiswa()) {
            if (m.getPembayaranList().contains(p)) {
                return m;
            }
        }
        return null;
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
}