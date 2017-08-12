package com.bluescale.business.security;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.bluescale.business.model.User;
import com.bluescale.business.service.UserService;

@Component("authenticationProvider")
public class LimitLoginAuthenticationProvider extends DaoAuthenticationProvider {
	@Resource(name = "userService")
	UserService userService;

	@Autowired
	@Qualifier("userDetailsLoader")
	@Override
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		super.setUserDetailsService(userDetailsService);
	}
	

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		try {

			Authentication auth = super.authenticate(authentication);
			User findByLogin = userService.findByLogin(authentication.getName());
			userService.updateUser(findByLogin);
			return auth;

		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

}
