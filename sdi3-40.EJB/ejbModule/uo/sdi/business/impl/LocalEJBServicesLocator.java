package uo.sdi.business.impl;

import java.io.IOException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import uo.sdi.business.ApplicationService;
import uo.sdi.business.RatingService;
import uo.sdi.business.ServicesFactory;
import uo.sdi.business.TripService;
import uo.sdi.business.UserService;
import uo.sdi.persistence.util.JdbcHelper;


public class LocalEJBServicesLocator implements ServicesFactory {

	private static final String JNDI_PROPERTIES_FILE = "/jndi_dir.properties";
	
	private static String APPLICATION_SERVICE_JNDI_KEY;
	
	private static String TRIP_SERVICE_JNDI_KEY;
	
	private static String USER_SERVICE_JNDI_KEY;
	
	private static String RATING_SERVICE_JNDI_KEY;

	private Properties properties;
	
	public LocalEJBServicesLocator(){
		properties = new Properties();
		try {
			properties.load(JdbcHelper.class.getResourceAsStream(JNDI_PROPERTIES_FILE));
		} catch (IOException e) {
			throw new RuntimeException("Properties file not found: "
					+ JNDI_PROPERTIES_FILE);
			}
		APPLICATION_SERVICE_JNDI_KEY = properties.getProperty("JNDI_APPLICATION_REMOTE");
		TRIP_SERVICE_JNDI_KEY = properties.getProperty("JNDI_TRIP_REMOTE");
		USER_SERVICE_JNDI_KEY = properties.getProperty("JNDI_USER_REMOTE");
		RATING_SERVICE_JNDI_KEY = properties.getProperty("JNDI_RATING_REMOTE");
	}

	@Override
	public ApplicationService getApplicationService() {
		try {
			Context ctx = new InitialContext();
			return (ApplicationService) ctx.lookup(APPLICATION_SERVICE_JNDI_KEY);
		} catch (NamingException e) {
			throw new RuntimeException("JNDI problem", e);
		}
	}

	@Override
	public TripService getTripService() {
		try {
			Context ctx = new InitialContext();
			return (TripService) ctx.lookup(TRIP_SERVICE_JNDI_KEY);
		} catch (NamingException e) {
			throw new RuntimeException("JNDI problem", e);
		}
	}

	@Override
	public UserService getUserService() {
		try {
			Context ctx = new InitialContext();
			return (UserService) ctx.lookup(USER_SERVICE_JNDI_KEY);
		} catch (NamingException e) {
			throw new RuntimeException("JNDI problem", e);
		}
	}
	
	@Override
	public RatingService getRatingService() {
		try {
			Context ctx = new InitialContext();
			return (RatingService) ctx.lookup(RATING_SERVICE_JNDI_KEY);
		} catch (NamingException e) {
			throw new RuntimeException("JNDI problem", e);
		}
	}
}