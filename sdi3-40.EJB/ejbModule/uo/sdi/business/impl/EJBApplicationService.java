package uo.sdi.business.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.jws.WebService;

import alb.util.log.Log;
import uo.sdi.business.LocalApplicationService;
import uo.sdi.business.RemoteApplicationService;
import uo.sdi.model.Application;
import uo.sdi.model.ListaApuntados;
import uo.sdi.model.Seat;
import uo.sdi.model.SeatStatus;
import uo.sdi.model.Trip;
import uo.sdi.model.User;
import uo.sdi.model.ListaApuntados.PeticionEstado;
import uo.sdi.persistence.PersistenceFactory;
import uo.sdi.persistence.SeatDao;
import uo.sdi.persistence.TripDao;

@Stateless
@WebService (name = "ApplicationService")
public class EJBApplicationService implements LocalApplicationService, RemoteApplicationService {

	public Seat aceptarUsuario(ListaApuntados apuntado) {
		try {
			Seat s = new Seat();
			SeatDao sd = PersistenceFactory.newSeatDao();
			TripDao td = PersistenceFactory.newTripDao();
			// Añadido en BD
			Long ids[] = { apuntado.getUsuario().getId(),
					apuntado.getViaje().getId() };
			Seat seat = sd.findById(ids);
			if (seat == null) {
				s.setStatus(SeatStatus.ACCEPTED);
				s.setTripId(apuntado.getViaje().getId());
				s.setUserId(apuntado.getUsuario().getId());
				sd.save(s);
				seat = s;
			} else {
				seat.setStatus(SeatStatus.ACCEPTED);
				sd.update(seat);
			}
			apuntado.getViaje().setAvailablePax(
					apuntado.getViaje().getAvailablePax() - 1);
			td.update(apuntado.getViaje());
			Log.info(
					"El usuario [%s] ha sido aceptado en el viaje satisfactoriamente",
					apuntado.getUsuario());
			return seat;
		} catch (Exception e) {
			Log.info("Algo ha pasado aceptando al usuario [%s] ",
					apuntado.getUsuario());
			return null;
		}

	}

	public Seat cancelarUsuario(ListaApuntados apuntado) {
		try {
			SeatDao sd = PersistenceFactory.newSeatDao();
			TripDao td = PersistenceFactory.newTripDao();
			Long ids[] = { apuntado.getUsuario().getId(),
					apuntado.getViaje().getId() };
			Seat s = sd.findById(ids);
			if (s != null) {
				s.setStatus(SeatStatus.EXCLUDED);
				sd.update(s);
				apuntado.getViaje().setAvailablePax(
						apuntado.getViaje().getAvailablePax() + 1);
				td.update(apuntado.getViaje());
			} else {
				s = new Seat();
				s.setStatus(SeatStatus.EXCLUDED);
				s.setTripId(ids[1]);
				s.setUserId(ids[0]);
				sd.save(s);
			}
			Log.info(
					"El usuario [%s] ha sido rechazado del viaje satisfactoriamente",
					apuntado.getUsuario());
			return s;
		} catch (Exception e) {
			Log.info("Algo ha pasado rechazando al usuario [%s] ",
					apuntado.getUsuario());
			return null;
		}
	}

	public void apuntarseViaje(ListaApuntados apuntado) {
		// Añadido a la BD
		try {
			Application a = new Application();
			a.setTripId(apuntado.getViaje().getId());
			a.setUserId(apuntado.getUsuario().getId());
			PersistenceFactory.newApplicationDao().save(a);
			Log.info("El usuario [%s] se ha apuntado correctamente al viaje",
					apuntado.getUsuario());
		} catch (Exception e) {
			Log.info("Algo ha pasado apuntando al usuario [%s] al viaje",
					apuntado.getUsuario());
		}

	}

	public int desapuntarseViaje(ListaApuntados apuntado) {
		// Borrado de la BD
		try {
			TripDao td = PersistenceFactory.newTripDao();
			SeatDao sd = PersistenceFactory.newSeatDao();
			Long[] ids = { apuntado.getUsuario().getId(),
					apuntado.getViaje().getId() };
			int borrado = PersistenceFactory.newApplicationDao().delete(ids);
			if (apuntado.getRelacionViaje().equals(PeticionEstado.ACCEPTED)) {
				apuntado.getViaje().setAvailablePax(
						apuntado.getViaje().getAvailablePax() + 1);
				td.update(apuntado.getViaje());
			}
			if (apuntado.getAsiento() != null) {
				sd.delete(ids);
			}
			Log.info(
					"El usuario [%s] se ha desapuntado correctamente al viaje",
					apuntado.getUsuario());
			return borrado;
		} catch (Exception e) {
			Log.info("Algo ha pasado desapuntando al usuario [%s] al viaje",
					apuntado.getUsuario());
			return 0;
		}

	}

	public List<ListaApuntados> listaApuntadosUsuario(User usuario) {
		List<ListaApuntados> lista = new ArrayList<ListaApuntados>();
		List<uo.sdi.model.Application> reservas = PersistenceFactory
				.newApplicationDao().findByUserId(usuario.getId());
		List<Seat> asientos = PersistenceFactory.newSeatDao().findAll();
		List<Trip> viajes = PersistenceFactory.newTripDao().findAll();
		for (Trip t : viajes) {
			for (uo.sdi.model.Application ap : reservas) {
				ListaApuntados a = new ListaApuntados();
				if (ap.getTripId().equals(t.getId())
						&& usuario.getId().equals(ap.getUserId())) {
					a.setUsuario(usuario);
					a.setViaje(t);
					for (Seat s : asientos) {
						if (s.getTripId().equals(t.getId())
								&& usuario.getId().equals(s.getUserId())) {
							a.setAsiento(s);
						}
					}
					a.setRelacionViaje();
					lista.add(a);
				}
			}
		}
		return lista;
	}

	public List<ListaApuntados> listaApuntadosPromotor(List<Trip> viajes) {
		List<ListaApuntados> lista = new ArrayList<ListaApuntados>();
		List<uo.sdi.model.Application> reservas = PersistenceFactory
				.newApplicationDao().findAll();
		List<Seat> asientos = PersistenceFactory.newSeatDao().findAll();
		for (Trip t : viajes) {
			for (uo.sdi.model.Application ap : reservas) {
				ListaApuntados a = new ListaApuntados();
				if (ap.getTripId().equals(t.getId())) {
					a.setUsuario(PersistenceFactory.newUserDao().findById(
							ap.getUserId()));
					a.setViaje(t);
					for (Seat s : asientos) {
						if (s.getTripId().equals(t.getId())
								&& a.getUsuario().getId().equals(s.getUserId())) {
							a.setAsiento(s);
						}
					}
					a.setRelacionViaje();
					lista.add(a);
				}
			}
		}
		return lista;
	}

	@Override
	public List<Application> getApplication() {
		return PersistenceFactory.newApplicationDao().findAll();
	}

}
