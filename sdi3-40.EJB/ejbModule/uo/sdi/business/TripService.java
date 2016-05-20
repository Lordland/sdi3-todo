package uo.sdi.business;

import java.util.List;

import uo.sdi.model.Trip;
import uo.sdi.model.User;

public interface TripService {

	
	public void actualizarViaje(Trip v);
	
	public List<Trip> listarViajes();
	
	public void actualizaViajeId(Long id);
	
	public Trip iniciaViaje();
	
	public void creaViaje(Trip viaje, User usuario);
	
	public Trip buscarViaje(Long id);
}
