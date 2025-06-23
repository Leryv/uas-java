package view;

import model.*;
import service.*;

import java.text.DecimalFormat;
import java.util.Scanner;

public class StudentView {
    private final MainView mainView;
    private final AuthService authService;
    private final MahasiswaService mahasiswaService;
    private final PembayaranService pembayaranService;
    private final Scanner scan = new Scanner(System.in);

    public StudentView(
            MainView mainView,
            AuthService authService,
            MahasiswaService mahasiswaService,
            PembayaranService pembayaranService) {
        this.mainView = mainView;
        this.authService = authService;
        this.mahasiswaService = mahasiswaService;
        this.pembayaranService = pembayaranService;
    }

    public void menuStudent() {
        Mahasiswa m = mainView.getCurrentUser().getMahasiswaRef();

        tampilkanStatusPerSemester(m);

        while (true) {
            System.out.println("\n=== MENU MAHASISWA ===");
            System.out.println("1. Bayar Semester");
            System.out.println("2. Lihat Status Pembayaran");
            System.out.println("0. Logout");
            System.out.print("Pilih: ");
            int pilih = Integer.parseInt(mainView.getScanner().nextLine());

            switch (pilih) {
                case 1:
                    bayarSemester();
                    break;
                case 2:
                    lihatStatusPembayaran(m);
                    break;
                case 0:
                    mainView.setCurrentUser(null); // Enkapsulasi: reset currentUser
                    System.out.println("Berhasil logout.\n");
                    return;
                default:
                    System.out.println("Pilihan tidak valid");
            }
        }
    }

    private void tampilkanStatusPerSemester(Mahasiswa m) {
        // Tampilkan status per semester
        for (int semester = 1; semester <= 8; semester++) {
            double totalPerSemester = hitungTotalPerSemester(m, semester);
            double tagihanSemester = m.getGrade().getBiayaSemester();
            boolean lunasPerSemester = totalPerSemester >= tagihanSemester;

            System.out.printf("Semester %d: %s (Rp %,.0f / Rp %,.0f)%n",
                    semester,
                    lunasPerSemester ? "LUNAS" : "BELUM LUNAS",
                    totalPerSemester,
                    tagihanSemester);
        }

    }

    // Method pembantu untuk menghitung total per semester
    private double hitungTotalPerSemester(Mahasiswa m, int semester) {
        return m.getPembayaranList().stream()
                .filter(p -> p.getSemester() == semester)
                .mapToDouble(Pembayaran::getJumlah)
                .sum();
    }

    private void bayarSemester() {
        Mahasiswa m = null;

        if (mainView.getCurrentUser().getRole() == Role.ADMIN) {
            System.out.print("NIM Mahasiswa: ");
            String nim = scan.nextLine();
            m = mahasiswaService.cariMahasiswa(nim);

            if (m == null) {
                System.out.println("Mahasiswa tidak ditemukan.");
                return;
            }
        } else if (mainView.getCurrentUser().getRole() == Role.STUDENT) {
            m = mainView.getCurrentUser().getMahasiswaRef();
        }

        // Minta input semester
        System.out.print("Semester pembayaran (1-8): ");
        int semester;
        try {
            semester = Integer.parseInt(scan.nextLine());
            if (semester < 1 || semester > 8) {
                System.out.println("Semester harus antara 1-8.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Input semester tidak valid.");
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
            System.out.println("Input tidak valid. Masukkan angka.");
            return;
        }

        // Validasi pembayaran
        double sisa = pembayaranService.getSisaTagihan(m);
        if (jumlah <= 0) {
            System.out.println("Jumlah harus lebih dari 0.");
            return;
        }
        if (jumlah > sisa) {
            System.out.println("Pembayaran melebihi sisa tagihan. Sisa: Rp " + sisa);
            return;
        }

        // Metode pembayaran
        System.out.print("Metode (kontan/cicil): ");
        String metode = scan.nextLine().toLowerCase();
        if (!metode.equals("kontan") && !metode.equals("cicil")) {
            System.out.println("Metode harus 'kontan' atau 'cicil'.");
            return;
        }

        // Simpan pembayaran
        pembayaranService.bayar(m, jumlah, metode, semester);
        System.out.println("Pembayaran berhasil dicatat untuk semester " + semester);
    }

    private void lihatStatusPembayaran(Mahasiswa m) {
        System.out.println("Nama: " + m.getNama());
        System.out.println("NIM: " + m.getNim());
        System.out.println("Grade: " + m.getGrade());
        // System.out.println("Total Tagihan: " + m.getGrade().getBiayaSemester());
        // System.out.println("Total Bayar: " +
        // pembayaranService.getTotalPembayaran(m));
        // System.out.println("Sisa Tagihan: " + pembayaranService.getSisaTagihan(m));
        // System.out.println("Status: " + (pembayaranService.isLunas(m) ? "LUNAS" :
        // "BELUM LUNAS"));

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