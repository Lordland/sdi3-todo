package uo.sdi.client;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import uo.sdi.model.Trip;
import uo.sdi.model.User;

@Path("/Sdi3ServicesRest")
public interface Sdi3ServiceRest {

	@GET
	@Path("/buscarUsuario/{login}/{password}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	User buscarUsuario(@PathParam("login") String login,@PathParam("password")
		String password);
	
	
	@GET
	@Path("/listarViajesPromotorParticipado/{id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	List<Trip> listarViajesPromotorParticipado(@PathParam("id") Long idUsuario);
	
	@GET
	@Path("/buscarViaje/{id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	Trip buscarViaje(@PathParam("id") Long id);
	
}
