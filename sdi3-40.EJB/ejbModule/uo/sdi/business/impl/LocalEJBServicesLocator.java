package uo.sdi.business.impl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import uo.sdi.business.ApplicationService;
import uo.sdi.business.RatingService;
import uo.sdi.business.ServicesFactory;
import uo.sdi.business.TripService;
import uo.sdi.business.UserService;


public class LocalEJBServicesLocator implements ServicesFactory {

	private static final String APPLICATION_SERVICE_JNDI_KEY ="java:global/"
			+"sdi3-40/" + "sdi3-40.EJB/" + "EJBApplicationService!"
			+ "uo.sdi.business.LocalApplicationService";
	
	private static final String TRIP_SERVICE_JNDI_KEY = "java:global/"
			+ "sdi3-40/" + "sdi3-40.EJB/" + "EJBTripService!"
			+ "uo.sdi.business.LocalTripService";
	
	private static final String USER_SERVICE_JNDI_KEY = "java:global/"
			+ "sdi3-40/" + "sdi3-40.EJB/" + "EJBUserService!"
			+ "uo.sdi.business.LocalUserService";
	
	private static final String RATING_SERVICE_JNDI_KEY = "java:global/"
			+ "sdi3-40/" + "sdi3-40.EJB/" + "EJBRatingService!"
			+ "uo.sdi.business.LocalRatingService";

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