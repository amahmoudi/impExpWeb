package com.bluescale.business.dao;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bluescale.business.model.User;

@Repository("userDao")
public class UserDAOImpl extends AbstractDao implements UserDao {

	
	@Autowired
	DataSource dataSource;
	
	
	@Override
	public void saveUser(final User user) {
		 getSession().merge(user);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAllUsers() {
		Criteria criteria = getSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("adminEnabled", false));
		criteria.add(Restrictions.eq("userEnabled", false));
		return (List<User>) criteria.list();
	}

	@Override
	public User findByLogin(final String login) {
		Criteria criteria = getSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("userEmail", login));
		return (User) criteria.uniqueResult();
	}

	@Override
	public void updateUser(final User user) {
		getSession().update(user);
	}


}
