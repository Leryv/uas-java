package model;

public class User {
    private String username;
    private String password;
    private Role role;
    private Mahasiswa mahasiswaRef; // Jika role STUDENT, mengarah ke Mahasiswa

    public User(String username, String password, Role role, Mahasiswa mahasiswaRef) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.mahasiswaRef = mahasiswaRef;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Mahasiswa getMahasiswaRef() {
        return this.mahasiswaRef;
    }

    public void setMahasiswaRef(Mahasiswa mahasiswaRef) {
        this.mahasiswaRef = mahasiswaRef;
    }

}
