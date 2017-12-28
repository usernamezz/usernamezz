package com.example.service;

import com.example.model.User;

public interface UserService {
	User findUserByEmail(String email);
	User findUserById(int id);
	void saveUser(User user);
	Boolean isUserHasRole(User user, String role);
}
