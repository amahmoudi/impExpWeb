package com.bluescale.business.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractDao {
        
        /** The session factory. */
        @Autowired
	    private SessionFactory sessionFactory;

	    /**
    	 * Gets the session.
    	 *
    	 * @return the session
    	 */
    	protected Session getSession(){
	        return sessionFactory.getCurrentSession();
	    }

	    /**
    	 * Persist.
    	 *
    	 * @param entity the entity
    	 */
    	public void persist(Object entity){
	        getSession().persist(entity);
	    }

		/**
		 * Delete.
		 *
		 * @param entity the entity
		 */
		public void delete(Object entity){
		        getSession().delete(entity);
		}
}
