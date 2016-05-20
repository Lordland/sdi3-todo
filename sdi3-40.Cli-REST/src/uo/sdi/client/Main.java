package uo.sdi.client;

import java.util.ArrayList;
import java.util.List;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import uo.sdi.model.ListaApuntados;
import uo.sdi.model.Trip;
import uo.sdi.model.TripStatus;
import uo.sdi.model.User;
import alb.util.console.Console;

public class Main {

	private Sdi3RestService client;
	private User cliente;
	private String login;
	private String password;
	
	public static void main(String[] args) {
		Main main = new Main();
		main.run();
	}

	public Main() {
		login = Console.readString("Introduzca su login:");
		password = Console.readString("Introduzca su password:");
		client = new ResteasyClientBuilder().build()
				.register(new Authenticator("sdi", "password"))
				.target("http://localhost:8280/sdi3-40.Web/rest/")
				.proxy(Sdi3RestService.class);
	}
	
	private void run() {
		this.cliente = client.buscarUsuario(login,password);
		while (true) {
			listadoViajesPromovidos(cliente); // A Get
																	// operation
			Long id = Console.readLong("Si desea salir de sesion"
					+ " introduzca 0, de otra forma introduzca "
					+ "el id de uno de los viajes de la lista");
			if (id.equals("0")) {
				return;
			}
			listarApuntados(id);
		}
	}
	
	private void listadoViajesPromovidos(User u){
		List<Trip> viajes = client.listarViajes();
		List<Trip> v = new ArrayList<Trip>();
		System.out.println("El usuario " + u.getName() + 
				" " + u.getSurname() + " ha promovido los siguientes viajes: ");
		for(Trip t : viajes){
			if(t.getPromoterId().equals(u.getId()) 
					&& t.getStatus().equals(TripStatus.OPEN)){
				v.add(t);
				System.out.println("Id:" + t.getId() 
						+ " Origen: " + t.getDeparture().getCity() 
						+ " Destino: " + t.getDestination().getCity());
			}
		}
		if(v.isEmpty()){
			System.out.println("Este usuario no ha promovido ningun viaje");
		}
	}
	
	private void listarApuntados(Long id){
		Trip t = client.buscarViaje(id);//uo.sdi.model.Trip
		List<ListaApuntados> v = client.listarApuntadosAUnViaje(t);
		System.out.println("Elija uno de los id de la lista");
		for(ListaApuntados l : v){
			System.out.println("\tUsuario: Id=" + l.getUsuario().getId() 
					+ " Nombre="+ l.getUsuario().getName() 
					+ " Apellido=" + l.getUsuario().getSurname() 
					+ " Viaje: Origen-Destino=" +l.getViaje().getDeparture().getCity() + "-" 
					+ l.getViaje().getDestination().getCity());
		} 
		id = Console.readLong("Seleccione el id del usuario que "
				+ "quiera aceptar en el viaje");
		while(true){
			if(id.equals(null)){
				System.out.println("Error, elija un id de los de la lista");
			}
			else{
				for(ListaApuntados l : v){
					if(l.getUsuario().getId().equals(id)){
						client.aceptarUsuario(l);
						return;
					}
				}
			}
		}

	}

}
