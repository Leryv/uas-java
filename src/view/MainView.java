package view;

import model.*;
import service.*;

import java.util.Scanner;

public class MainView {
    private final Scanner scan = new Scanner(System.in);

    private final MahasiswaService mahasiswaService = new MahasiswaServiceImpl();
    private final PembayaranService pembayaranService = new PembayaranServiceImpl();
    private final AuthService authService = new AuthServiceImpl();
    private AdminView adminView = new AdminView(this, authService, mahasiswaService, pembayaranService);
    private StudentView studentView = new StudentView(this, authService, mahasiswaService, pembayaranService);

    private User currentUser;
    private boolean isKeluar;

    public Scanner getScanner() {
        return scan;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public boolean isKeluar() {
        return isKeluar;
    }

    public void setKeluar(boolean keluar) {
        isKeluar = keluar;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public MahasiswaService getMahasiswaService() {
        return mahasiswaService;
    }

    public PembayaranService getPembayaranService() {
        return pembayaranService;
    }

    public MainView() {
        initDataAwal();
    }

    private void initDataAwal() {
        // Admin default
        User admin = new User("admin", "admin123", Role.ADMIN, null);
        authService.tambahUser(admin);

        // Mahasiswa + akun student
        Mahasiswa m = new Mahasiswa("Andika", "2024320038", Grade.A);
        mahasiswaService.tambahMahasiswa(m);

        User student = new User("andika1505", "123", Role.STUDENT, m);
        authService.tambahUser(student);

        System.out.println("Data awal berhasil dimuat.");
    }

    public void start() {
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
                System.out.println("Login berhasil sebagai " + currentUser.getRole().getDisplayName());

                if (currentUser.getRole() == Role.ADMIN) {
                    adminView.menuAdmin();
                } else if (currentUser.getRole() == Role.STUDENT) {
                    studentView.menuStudent();
                }

                handleLogout();
            }
        }
    }

    private void handleLogout() {
        while (true) {
            System.out.print("\nIngin login kembali? (y/n): ");
            String jawab = scan.nextLine().trim().toLowerCase();

            if (jawab.equals("n")) {
                isKeluar = true;
                System.out.println("👋 Program selesai. Terima kasih.");
                break;
            } else if (jawab.equals("y")) {
                break;
            } else {
                System.out.println("Jawaban tidak valid. Ketik y atau n.");
            }
        }
    }

}