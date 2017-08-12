package com.bluescale.business.dao;

import java.util.List;

import com.bluescale.business.model.User;

public interface UserDao {
	
	
		  String SAVE_USER   = "SAVE_NEW_USER";
		  String GET_USERS   = "GET_USER_LIST";
		  String UPDATE_USER = "UPDATE_USER";
		  String DELETE_USER = "DELETE_USER";
		  String GET_USER_ID = "GET_USER_ID";
	
	/**
	 * Save user.
	 *
	 * @param user
	 *            the user
	 */
	void saveUser(User user);

	/**
	 * Find all users.
	 *
	 * @return the list
	 */
	List<User> findAllUsers();

	/**
	 * Find by login.
	 *
	 * @param login
	 *            the login
	 * @return the user
	 */
	User findByLogin(String login);

	/**
	 * Update user.
	 *
	 * @param user
	 *            the user
	 */
	void updateUser(User user);
	/**
	 * Update attempConnection.
	 *
	 * @param isMinus
	 *            
	 */
	

}
