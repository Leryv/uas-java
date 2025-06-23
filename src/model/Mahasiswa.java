package model;

import java.util.ArrayList;
import java.util.List;

public class Mahasiswa {
    private String nama;
    private String nim;
    private Grade grade;
    private Major major;
    private List<Pembayaran> pembayaranList = new ArrayList<>();

    public Mahasiswa(String nama, String nim, Grade grade, Major major) {
        this.nama = nama;
        this.nim = nim;
        this.grade = grade;
        this.major = major;
    }

    public String getNama() {
        return this.nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNim() {
        return this.nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public Grade getGrade() {
        return this.grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Major getMajor() {
        return this.major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public List<Pembayaran> getPembayaranList() {
        return this.pembayaranList;
    }

    public void setPembayaranList(List<Pembayaran> pembayaranList) {
        this.pembayaranList = pembayaranList;
    }

}
