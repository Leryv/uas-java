package service;

import model.Mahasiswa;
import model.Pembayaran;

import java.util.List;

public interface MahasiswaService {
    void tambahMahasiswa(Mahasiswa m);

    Mahasiswa cariMahasiswa(String nim);

    List<Mahasiswa> getSemuaMahasiswa();

    List<Pembayaran> getAllPembayaran();

    double getTotalPembayaranPerSemester(String nim, int semester);

}
