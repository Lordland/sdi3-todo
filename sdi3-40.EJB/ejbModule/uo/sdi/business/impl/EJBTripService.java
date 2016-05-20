package uo.sdi.business.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebService;

import alb.util.log.Log;
import uo.sdi.business.LocalTripService;
import uo.sdi.business.RemoteTripService;
import uo.sdi.model.AddressPoint;
import uo.sdi.model.Trip;
import uo.sdi.model.TripStatus;
import uo.sdi.model.User;
import uo.sdi.model.Waypoint;
import uo.sdi.persistence.PersistenceFactory;
import uo.sdi.persistence.TripDao;

@Stateless
@WebService (name = "TripService")
public class EJBTripService implements LocalTripService , RemoteTripService{

	
	public void actualizarViaje(Trip v){
		TripDao td = PersistenceFactory.newTripDao();
		td.update(v);
	}
	
	public List<Trip> listarViajes(){
		return PersistenceFactory.newTripDao().findAll();
	}
	
	public void actualizaViajeId(Long id){
		TripDao td = PersistenceFactory.newTripDao();
		Trip t = td.findById(id);
		td.update(t);
	}
	
	public Trip iniciaViaje(){
		Trip viaje = new Trip();
		viaje.setDeparture(new AddressPoint("", "",
				"", "", "", new Waypoint(
						0.0, 0.0)));
		viaje.setDestination(new AddressPoint("", "",
				"", "", "", new Waypoint(
						0.0, 0.0)));
		viaje.setArrivalDate(null);
		viaje.setClosingDate(null);
		viaje.setDepartureDate(null);
		viaje.setComments("");
		viaje.setAvailablePax(0);
		viaje.setMaxPax(0);
		viaje.setEstimatedCost(0.0);
		viaje.setStatus(TripStatus.OPEN);
		return viaje;
	}
	
	public void creaViaje(Trip viaje, User usuario){
		try{
		viaje.setPromoterId(usuario.getId());
		viaje.setMaxPax(viaje.getAvailablePax());
		PersistenceFactory.newTripDao().save(viaje);
		Log.info("El viaje [%s] ha sido creado satisfactoriamente", viaje.getDeparture().getCity()
				+ "-" + viaje.getDestination().getCity());
		}
		catch(Exception e){
			Log.error("Ha ocurrido algo creando el viaje [%s]", viaje
					.getDeparture().getCity()
					+ "-" + viaje.getDestination().getCity());
		}
		
	}

	@Override
	public Trip buscarViaje(Long id) {
		return PersistenceFactory.newTripDao().findById(id);
	}

	@Override
	public List<Trip> listarViajesUltimoMes() {
		List<Trip> trips = listarViajes();
		List<Trip> tr = new ArrayList<Trip>();
		Date d = new Date();
		d.setTime(d.getTime() + 30 * 1000 * 60 * 60 * 24);
		for(Trip t : trips){
			if(t.getDepartureDate().compareTo(d) < 0){
				tr.add(t);
			}
		}
		return tr;
	}

	@Override
	public List<Trip> listaViajePromotor(Long id) {
		List<Trip> viajes = listarViajes();
		List<Trip> aux = new ArrayList<Trip>();
		for (Trip t : viajes) {
			if (t.getPromoterId().equals(id)) {
				aux.add(t);
			}
		}
		return aux;
	}
	
	

	
}
