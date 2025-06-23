package service;

import model.Mahasiswa;
import model.Pembayaran;

import java.util.ArrayList;
import java.util.List;

public class MahasiswaServiceImpl implements MahasiswaService {
    private List<Mahasiswa> mahasiswaList = new ArrayList<>();

    @Override
    public void tambahMahasiswa(Mahasiswa m) {
        mahasiswaList.add(m);
    }

    @Override
    public Mahasiswa cariMahasiswa(String nim) {
        return mahasiswaList.stream()
                .filter(m -> m.getNim().equals(nim))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Mahasiswa> getSemuaMahasiswa() {
        return mahasiswaList;
    }

    @Override
    public List<Pembayaran> getAllPembayaran() {
        List<Pembayaran> semuaPembayaran = new ArrayList<>();
        for (Mahasiswa m : mahasiswaList) {
            semuaPembayaran.addAll(m.getPembayaranList());
        }
        return semuaPembayaran;
    }

}
