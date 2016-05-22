package uo.sdi.rest.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import uo.sdi.business.ApplicationService;
import uo.sdi.business.TripService;
import uo.sdi.business.UserService;
import uo.sdi.infraestructure.Factory;
import uo.sdi.model.ListaApuntados;
import uo.sdi.model.SeatStatus;
import uo.sdi.model.Trip;
import uo.sdi.model.User;
import uo.sdi.model.ListaApuntados.PeticionEstado;
import uo.sdi.rest.Sdi3ServicesRest;

public class Sdi3ServicesRestImpl implements Sdi3ServicesRest{

	UserService serviceU = Factory.services.getUserService();
	ApplicationService serviceA = Factory.services.getApplicationService();
	TripService serviceT = Factory.services.getTripService();

	
	@Override
	public User buscarUsuario(String login,String password) {
		return serviceU.buscaUsuarioPass(login,password);
	}
	
	

	@Override
	public List<ListaApuntados> listarApuntadosAUnViaje(Trip t) {
		return serviceA.peticionesParaUnViaje(t);
	}

	@Override
	public void cancelarUsuario(ListaApuntados apuntado)
			throws EntityNotFoundException {
		serviceA.cancelarUsuario(apuntado);
	}
	
	@Override
	public void aceptarUsuario(ListaApuntados apuntado)
			throws EntityNotFoundException {
		serviceA.aceptarUsuario(apuntado);
		
	}

	@Override
	public List<Trip> listarViajes() {
		return serviceT.listarViajes();
	}

	@Override
	public void actualizarViaje(Trip viaje) throws EntityNotFoundException {
		serviceT.actualizarViaje(viaje);
		
	}

	@Override
	public void actualizarViajeId(Long id) throws EntityNotFoundException {
		serviceT.actualizaViajeId(id);
		
	}

	@Override
	public Trip buscarViaje(Long id) throws EntityNotFoundException {
		return serviceT.buscarViaje(id);
	}



	@Override
	public List<Trip> listarViajesPromotorParticipado(Long idUsuario) {
		User u = serviceU.findById(idUsuario);
		List<Trip> lista = serviceT.listaViajePromotor(idUsuario);
		List<ListaApuntados> l = serviceA.listaApuntadosUsuario(u);
		List<Trip> v = new ArrayList<Trip>();
		for(ListaApuntados list : l){
			if(list.getAsiento().getStatus().equals(SeatStatus.ACCEPTED)){
				v.add(list.getViaje());
			}
		}
		for(Trip t : lista){
			v.add(t);
		}
		return v;
	}



	
}
