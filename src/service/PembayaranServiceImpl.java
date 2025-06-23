package service;

import model.Mahasiswa;
import model.Pembayaran;

import java.time.LocalDate;

public class PembayaranServiceImpl implements PembayaranService {

    @Override
    public void bayar(Mahasiswa m, double jumlah, String metode) {
        double sisaTagihan = getSisaTagihan(m);

        if (jumlah > sisaTagihan) {
            System.out.println("❌ Pembayaran melebihi tagihan. Sisa tagihan: Rp " + sisaTagihan);
            return;
        }

        Pembayaran p = new Pembayaran(jumlah, LocalDate.now(), metode);
        m.getPembayaranList().add(p);
        System.out.println("✅ Pembayaran berhasil dicatat.");

    }

    @Override
    public double getTotalPembayaran(Mahasiswa m) {
        return m.getPembayaranList().stream()
                .mapToDouble(Pembayaran::getJumlah)
                .sum();
    }

    @Override
    public double getSisaTagihan(Mahasiswa m) {
        double totalTagihan = m.getGrade().getBiayaSemester();
        double totalDibayar = getTotalPembayaran(m);
        return totalTagihan - totalDibayar;
    }

    @Override
    public boolean isLunas(Mahasiswa m) {
        return getTotalPembayaran(m) >= m.getGrade().getBiayaSemester();
    }

}
