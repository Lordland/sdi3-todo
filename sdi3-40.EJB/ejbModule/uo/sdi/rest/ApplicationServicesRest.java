package uo.sdi.rest;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import uo.sdi.model.ListaApuntados;
import uo.sdi.model.Trip;

public interface ApplicationServicesRest {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	List<ListaApuntados> listarApuntadosAUnViaje(Trip t);
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	void cancelarUsuario(ListaApuntados apuntado) throws EntityNotFoundException;
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	void aceptarUsuario(ListaApuntados apuntado) throws EntityNotFoundException;

}
