package uo.sdi.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import uo.sdi.model.User;

@Path("/UserServices")
public interface UserServicesRest {

	@GET
	@Path("{login}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	User buscarUsuario(@PathParam("login") String login);
	
}
