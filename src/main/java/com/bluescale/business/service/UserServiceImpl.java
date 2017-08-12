package com.bluescale.business.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluescale.business.dao.UserDao;
import com.bluescale.business.model.User;

@Service("userService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class UserServiceImpl implements UserService {
    @Resource(name = "userDao")
    private UserDao         dao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(User user) {
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        dao.saveUser(user);

    }

    @Override
    public List<User> findAllTestPassed() {

        return dao.findAllUsers();
    }


    @Override
    public User findByLogin(String login) {
        return dao.findByLogin(login);
    }

    @Override
    public void updateUser(User user) {
        dao.updateUser(user);

    }
    @Override
    public User findUserFromSession (){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName(); 
        return dao.findByLogin(login);
    }

}
