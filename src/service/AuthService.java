package service;

import model.User;

public interface AuthService {
    void registerAdmin();

    User login(String username, String password);

    void tambahUser(User user);
}
