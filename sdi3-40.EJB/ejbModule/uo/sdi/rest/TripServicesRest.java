package uo.sdi.rest;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import uo.sdi.model.Trip;
 
@Path("/TripServices")
public interface TripServicesRest {

	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	List<Trip> listarViajes();
	
	@DELETE
	@Path("{id}")
	void deleteViaje(@PathParam("id") Long id) throws EntityNotFoundException;
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	void saveViaje(Trip viaje) throws EntityExistsException;
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	void actualizarViaje(Trip viaje) throws EntityNotFoundException;
	
}
