package model;

public enum Grade {
    A(1000000), B(1500000), C(2000000), D(2500000);

    private final double biayaSemester;

    Grade(double biayaSemester) {
        this.biayaSemester = biayaSemester;
    }

    public double getBiayaSemester() {
        return biayaSemester;
    }
}
