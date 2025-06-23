package service;

import model.Mahasiswa;

public interface PembayaranService {
    void bayar(Mahasiswa m, double jumlah, String metode); // "kontan" / "cicil"

    double getTotalPembayaran(Mahasiswa m);

    double getSisaTagihan(Mahasiswa m);

    boolean isLunas(Mahasiswa m);

}
