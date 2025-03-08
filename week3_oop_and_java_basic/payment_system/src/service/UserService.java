package service;

import model.User;

public interface UserService {
    User login(String username, String password);
    void logout();
    boolean changePassword(String userId, String oldPassword, String newPassword);
    void viewSecurityInfo(String userId);
}
