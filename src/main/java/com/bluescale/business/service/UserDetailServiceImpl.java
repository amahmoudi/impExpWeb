/**
 * 
 */
package com.bluescale.business.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.bluescale.business.exception.UserNotActivatedException;
import com.bluescale.business.model.User;


@Component("userDetailsLoader")
public class UserDetailServiceImpl implements UserDetailsService {

	@Resource(name = "userService")
	private UserService userService;

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		User user = userService.findByLogin(login);
		SimpleGrantedAuthority grantedAuthorityRole = new SimpleGrantedAuthority("ROLE_USER");
		Collection<GrantedAuthority> grantedAuthority = new ArrayList<GrantedAuthority>();
		grantedAuthority.add(grantedAuthorityRole);
		if (user == null)
			throw new UsernameNotFoundException("Unable to find user wih username " + login);
		final Boolean isEnabled = user.getUserEnabled();
		if (!isEnabled)
			throw new UserNotActivatedException("User " + login + " was not activated");
		return new org.springframework.security.core.userdetails.User(user.getUserEmail(), user.getUserPassword(),
				isEnabled, true, true, true, grantedAuthority);
	}

}
