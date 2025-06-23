package service;

import model.User;
import model.Role;

import java.util.ArrayList;
import java.util.List;

public class AuthServiceImpl implements AuthService {
    private List<User> userList = new ArrayList<>();

    @Override
    public void registerAdmin() {
        User admin = new User("admin", "admin123", Role.ADMIN, null);
        User student = new User("andika", "andika123", Role.STUDENT, null);
        userList.add(admin);
        userList.add(student);
    }

    @Override
    public void tambahUser(User user) {
        userList.add(user);
    }

    @Override
    public User login(String username, String password) {
        return userList.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
}
