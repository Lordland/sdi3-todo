package uo.sdi.business;

public interface ServicesFactory {

	
	public TripService getTripService();
	
	public UserService getUserService();
	
	public ApplicationService getApplicationService();
	
	public RatingService getRatingService();
}
