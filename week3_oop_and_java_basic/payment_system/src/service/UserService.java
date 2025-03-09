package service;

import model.User;

public interface UserService {
    User login(String username, String password);
    void logout();
    boolean changePassword(String userId);
    void viewSecurityInfo(String userId);
}
