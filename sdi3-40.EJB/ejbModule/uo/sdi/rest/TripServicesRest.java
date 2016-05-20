package uo.sdi.rest;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import uo.sdi.model.Trip;
 
@Path("/TripServices")
public interface TripServicesRest {

	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	List<Trip> listarViajes();
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	void actualizarViaje(Trip viaje) throws EntityNotFoundException;
	
	@POST
	@Path("{id}")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	void actualizarViajeId(@PathParam("id") Long id) throws EntityNotFoundException;
	
	
}
