package model;

import java.time.LocalDate;

public class Pembayaran {
    private double jumlah;
    private LocalDate tanggal;
    private String metode; // "kontan" atau "cicil"

    public Pembayaran(double jumlah, LocalDate tanggal, String metode) {
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.metode = metode;
    }

    public double getJumlah() {
        return this.jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

    public LocalDate getTanggal() {
        return this.tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public String getMetode() {
        return this.metode;
    }

    public void setMetode(String metode) {
        this.metode = metode;
    }

    // Getter & Setter
}
