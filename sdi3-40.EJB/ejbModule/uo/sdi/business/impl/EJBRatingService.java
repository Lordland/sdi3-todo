package uo.sdi.business.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebService;

import uo.sdi.business.LocalRatingService;
import uo.sdi.business.RemoteRatingService;
import uo.sdi.model.Rating;
import uo.sdi.model.Trip;
import uo.sdi.persistence.PersistenceFactory;

@Stateless
@WebService (name = "RatingService")
public class EJBRatingService implements LocalRatingService,RemoteRatingService{

	@Override
	public List<Rating> listarComentarios(Trip t) {
		List<Rating> ratings = PersistenceFactory.newRatingDao().findAll();
		List<Rating> resultado = new ArrayList<Rating>();
		for(Rating r : ratings){
			if(r.getSeatFromTripId().equals(t.getId())){
				resultado.add(r);
			}
		}
		return resultado;
	}

	@Override
	public void eliminarComentarios(Long id) {
		PersistenceFactory.newRatingDao().delete(id);
	}

	@Override
	public List<Rating> listarRatings() {
		return PersistenceFactory.newRatingDao().findAll();
	}

	@Override
	public void borrarRating(Long id) {
		List<Rating> r = listarRatings();
		for(Rating rat : r){
			if(id != null && rat.getId().equals(id)){
				eliminarComentarios(rat.getId());
			}
		}
	}
}
