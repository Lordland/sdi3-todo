package uo.sdi.rest.impl;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import uo.sdi.business.TripService;
import uo.sdi.infraestructure.Factory;
import uo.sdi.model.Trip;
import uo.sdi.rest.TripServicesRest;

public class TripServicesRestImpl implements TripServicesRest{
	
	TripService service = Factory.services.getTripService();

	@Override
	public List<Trip> listarViajes() {
		return service.listarViajes();
	}

	@Override
	public void actualizarViaje(Trip viaje) throws EntityNotFoundException {
		service.actualizarViaje(viaje);
		
	}

	@Override
	public void actualizarViajeId(Long id) throws EntityNotFoundException {
		service.actualizaViajeId(id);
		
	}

}
