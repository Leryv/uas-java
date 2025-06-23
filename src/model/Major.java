package model;

public enum Major {
    A(
            "Teknologi Informasi", 1200000),
    B("Sistem Informasi", 1300000), C("Rekayasa Perangkat Lunak", 1000000);

    private String displayMajorName;
    private double displayPaymentOfMajor;

    Major(String displayMajorName, double displayPaymentOfMajor) {
        this.displayMajorName = displayMajorName;
        this.displayPaymentOfMajor = displayPaymentOfMajor;
    }

    public String getDisplayName() {
        return displayMajorName;
    }

    public double getDisplayPaymentOfMajor() {
        return displayPaymentOfMajor;
    }

}
