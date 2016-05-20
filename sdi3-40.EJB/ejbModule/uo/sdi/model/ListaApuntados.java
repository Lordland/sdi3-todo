package uo.sdi.model;

import java.io.Serializable;

public class ListaApuntados implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 828860037912086236L;

	public enum PeticionEstado {
		ACCEPTED, EXCLUDED, PENDANT, PROMOTER, NO_SEAT
	}

	Trip viaje;
	User usuario;
	Seat asiento;
	PeticionEstado relacionViaje;

	public Trip getViaje() {
		return viaje;
	}

	public void setViaje(Trip viaje) {
		this.viaje = viaje;
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public Seat getAsiento() {
		return asiento;
	}

	public void setAsiento(Seat asiento) {
		this.asiento = asiento;
	}

	public PeticionEstado getRelacionViaje() {
		return relacionViaje;
	}

	public void setRelacionViaje() {
		if (usuario.getId().equals(viaje.getPromoterId()))
			this.relacionViaje = PeticionEstado.PROMOTER;
		else if (asiento == null)
			this.relacionViaje = PeticionEstado.PENDANT;
		else if (asiento.getStatus().equals(SeatStatus.ACCEPTED))
			this.relacionViaje = PeticionEstado.ACCEPTED;
		else if (asiento.getStatus().equals(SeatStatus.EXCLUDED))
			this.relacionViaje = PeticionEstado.EXCLUDED;
		else if (viaje.getStatus().equals(TripStatus.CANCELLED)
				|| viaje.getStatus().equals(TripStatus.CLOSED)
				|| viaje.getStatus().equals(TripStatus.DONE))
			this.relacionViaje = PeticionEstado.EXCLUDED;
		else if (viaje.getAvailablePax() == 0)
			this.relacionViaje = PeticionEstado.NO_SEAT;
	}
}
