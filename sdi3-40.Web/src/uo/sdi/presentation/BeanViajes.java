package uo.sdi.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.*;

import org.primefaces.event.RowEditEvent;

import uo.sdi.infraestructure.Factory;
import uo.sdi.model.Trip;
import uo.sdi.model.TripStatus;

@ManagedBean(name = "viajes")
@ApplicationScoped
public class BeanViajes implements Serializable {
	private static final long serialVersionUID = 56897L;

	private Trip viaje;
	private List<Trip> viajes = null;
	private List<Trip> viajesUsuario = null;
	private List<Trip> viajesPromotor = null;
	private List<TripStatus> estados = null;
	private TripStatus estado;

	/**
	 * Crea el managed bean e inicializa los valores necesarios
	 */
	public BeanViajes() {
		iniciaViaje();
		listaViaje();
		viajesUsuario = new ArrayList<Trip>();
		viajesPromotor = new ArrayList<Trip>();
		estados = new ArrayList<TripStatus>();
		estados.add(TripStatus.CANCELLED);
		estados.add(TripStatus.CLOSED);
		estados.add(TripStatus.DONE);
		estados.add(TripStatus.OPEN);
	}

	public List<TripStatus> getEstados() {
		return estados;
	}

	public void setEstados(List<TripStatus> estados) {
		this.estados = estados;
	}

	public List<Trip> getViajesUsuario() {
		return viajesUsuario;
	}

	public void setViajesUsuario(List<Trip> viajesUsuario) {
		this.viajesUsuario = viajesUsuario;
	}

	public List<Trip> getViajesPromotor() {
		return viajesPromotor;
	}

	public void setViajesPromotor(List<Trip> viajesPromotor) {
		this.viajesPromotor = viajesPromotor;
	}

	public Trip getViaje() {
		return viaje;
	}

	public void setViaje(Trip viaje) {
		this.viaje = viaje;
	}

	public List<Trip> getViajes() {
		return viajes;
	}

	public void setViajes(List<Trip> viajes) {
		this.viajes = viajes;
	}
	
	public TripStatus getEstado() {
		return estado;
	}

	public void setEstado(TripStatus estado) {
		this.estado = estado;
	}
	
	/**
	 * Método que crea un viaje ejemplo para ser rellenado
	 */
	public void iniciaViaje() {
		setViaje(Factory.services.getTripService().iniciaViaje());
	}
	

	/**
	 * Método que registra un evento RowEdit y actualiza un viaje al modificarse su estado
	 * en dicho evento
	 * @param event
	 */
	public void actualizar(RowEditEvent event){
		Trip v = (Trip) event.getObject();
		Factory.services.getTripService().actualizarViaje(v);
	} 
	
	/**
	 * Método que registra un evento RowEdit y lo cancela
	 * @param event
	 */
	public void cancelarActualizar(RowEditEvent event){
		
	}
	/**
	 * Lista todos los viajes existentes en la BD
	 */
	public void listaViaje() {
		List<Trip> lista = Factory.services.getTripService().listarViajes();
		setViajes(lista);
	}

	/**
	 * Lista los viajes de la BD menos aquellos a los que el usuario que ha
	 * iniciado sesión es promotor de ellos
	 */
	public void listaViajeUsuario(long id) {
		List<Trip> aux = new ArrayList<Trip>();
		for (Trip t : viajes) {
			if (!t.getPromoterId().equals(id)) {
				aux.add(t);
			}
		}
		setViajesUsuario(aux);
	}

	/**
	 * Lista los viajes de la BD los cuales el usuario que ha iniciado sesión
	 * es promotor
	 */
	public void listaViajePromotor(long id) {
		List<Trip> aux = new ArrayList<Trip>();
		for (Trip t : viajes) {
			if (t.getPromoterId().equals(id)) {
				aux.add(t);
			}
		}
		setViajesPromotor(aux);
	}

	/**
	 * Modifica un viaje concreto que el usuario seleccione
	 */
	public String cambiaViaje() {
		try {
			Factory.services.getTripService().actualizaViajeId(viaje.getId());
			return "exito";
		} catch (Exception e) {
			return "fracaso";
		}
	}

}