package uo.sdi.business;

import java.util.List;

import uo.sdi.model.Rating;
import uo.sdi.model.Trip;

public interface RatingService {

	public List<Rating> listarComentarios(Trip t);
	
	public void eliminarComentarios(Long id);
	
	public List<Rating> listarRatings();
	
}
