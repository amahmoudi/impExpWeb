package com.bluescale.business.service;

import java.util.List;

import com.bluescale.business.model.User;

public interface UserService {
	void saveUser(User user);

	List<User> findAllTestPassed();

	User findByLogin(String login);

	void updateUser(User user);
	
	User findUserFromSession ();
}
