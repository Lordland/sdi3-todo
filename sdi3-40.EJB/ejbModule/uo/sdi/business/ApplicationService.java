package uo.sdi.business;

import java.util.List;

import uo.sdi.model.Application;
import uo.sdi.model.ListaApuntados;
import uo.sdi.model.Seat;
import uo.sdi.model.Trip;
import uo.sdi.model.User;

public interface ApplicationService {

	
	public Seat aceptarUsuario(ListaApuntados apuntado);
	
	public Seat cancelarUsuario(ListaApuntados apuntado);
	
	public void apuntarseViaje(ListaApuntados apuntado);
	
	public int desapuntarseViaje(ListaApuntados apuntado);
	
	public List<ListaApuntados> listaApuntadosUsuario(User usuario);
	
	public List<ListaApuntados> listaApuntadosPromotor(List<Trip> viajes);
	
	public List<Application> getApplication();
	
	public List<ListaApuntados> peticionesParaUnViaje(Trip t);
}
