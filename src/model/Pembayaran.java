package model;

import java.time.LocalDate;

public class Pembayaran {
    private double jumlah;
    private LocalDate tanggal;
    private String metode; // "kontan" atau "cicil"
    private int semester; // Tambahkan atribut semester

    public Pembayaran(double jumlah, LocalDate tanggal, String metode, int semester) {
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.metode = metode;
        this.semester = semester; // Inisialisasi semester
    }

    // Getter & Setter
    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public String getMetode() {
        return metode;
    }

    public void setMetode(String metode) {
        this.metode = metode;
    }

    // Tambahkan getter dan setter untuk semester
    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
}