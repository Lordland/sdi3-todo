package uo.sdi.rest.impl;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import uo.sdi.business.ApplicationService;
import uo.sdi.infraestructure.Factory;
import uo.sdi.model.ListaApuntados;
import uo.sdi.model.Trip;
import uo.sdi.rest.ApplicationServicesRest;

public class ApplicationServicesRestImpl implements ApplicationServicesRest{
	
	ApplicationService service = Factory.services.getApplicationService();

	@Override
	public List<ListaApuntados> listarApuntadosAUnViaje(Trip t) {
		return service.peticionesParaUnViaje(t);
	}

	@Override
	public void cancelarUsuario(ListaApuntados apuntado)
			throws EntityNotFoundException {
		service.cancelarUsuario(apuntado);
		
	}

	@Override
	public void aceptarUsuario(ListaApuntados apuntado)
			throws EntityNotFoundException {
		service.aceptarUsuario(apuntado);
		
	}

}
