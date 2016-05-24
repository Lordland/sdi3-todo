package uo.sdi.client;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import uo.sdi.model.ListaApuntados;
import uo.sdi.model.Trip;
import uo.sdi.model.User; 

@Path("/Sdi3ServicesRest")
public interface Sdi3RestService {

	@GET
	@Path("/buscarUsuario/{login}/{password}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	User buscarUsuario(@PathParam("login") String login,@PathParam("password") String password);
	
	@GET
	@Path("/listarViajes")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	List<Trip> listarViajes();
	
	@POST
	@Path("/actualizarViaje")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	void actualizarViaje(Trip viaje);
	
	@POST
	@Path("/actualizarViajeId/{id}")
	void actualizarViajeId(@PathParam("id") Long id);
	
	@GET
	@Path("/buscarViaje/{id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	Trip buscarViaje(@PathParam("id") Long id);
	
	@POST
	@Path("/listarApuntados")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	List<ListaApuntados> listarApuntadosAUnViaje(Trip t);
	
	@POST
	@Path("/cancelar")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	void cancelarUsuario(ListaApuntados apuntado);
	 
	@POST
	@Path("/aceptar")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	void aceptarUsuario(ListaApuntados apuntado);
	
	
}
