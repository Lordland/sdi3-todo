package uo.sdi.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import alb.util.console.Console;
import uo.sdi.business.ApplicationService;
import uo.sdi.business.RatingService;
import uo.sdi.business.TripService;
import uo.sdi.business.UserService;
import uo.sdi.business.impl.RemoteEJBServicesLocator;
import uo.sdi.model.ListaApuntados;
import uo.sdi.model.Rating;
import uo.sdi.model.Trip;
import uo.sdi.model.User;
import uo.sdi.model.UserStatus;

public class Main {
	private static String mensaje = "Opciones: \n\t1.Listar usuarios y viajes "
			+ "promovidos y apuntados\n\t2.Excluir usuario\n\t3.Listar "
			+ "comentarios y puntuaciones\n\t4.Eliminar comentarios y "
			+ "puntuaciones\n\t0.Salir";
	
	private static void run() throws Exception {
		while(true){
			System.out.println(mensaje);
			int opcion = Console.readInt("Diga su opcion: "); 
			if(opcion == 1){
				mostrarDatos();
			}
			else if(opcion == 2){
				cancelarUsuario();
			}
			else if(opcion == 3){//TODO no estan ordenados de mas reciente a menos
				listar();
			}
			else if(opcion == 4){
				borrarRatings();
			}
			else if(opcion == 0){
				return;
			}
			else{
				System.out.println("Error, vuelva a mirar las opciones "
						+ "disponibles");
			}
		}
	}
	
	
	private static void listar(){
		TripService serviceT = new RemoteEJBServicesLocator().getTripService();
		List<Trip> trips = serviceT.listarViajes();
		List<Trip> tr = new ArrayList<Trip>();
		Date d = new Date();
		d.setTime(d.getTime() + 30 * 1000 * 60 * 60 * 24);
		for(Trip t : trips){
			if(t.getDepartureDate().compareTo(d) < 0){
				tr.add(t);
			}
		}
		for(Trip t : tr){
			System.out.println("Viaje: " + t.getId() + " salida: " + 
					t.getDeparture() +	" destino: " + t.getDestination());
			RatingService service = new RemoteEJBServicesLocator().getRatingService();
			List<Rating> ratings = service.listarComentarios(t);
			for(Rating r : ratings){
				User u1 = new RemoteEJBServicesLocator().getUserService()
						.findById(r.getSeatFromUserId());
				User u2 = new RemoteEJBServicesLocator().getUserService()
						.findById(r.getSeatAboutUserId());
				System.out.println("\t"+t.getDestination() + " " + 
						u1.getName() + " " + u2.getName() + " " + r.getValue()
						+ "\n\t" + r.getComment());
			}
		}
	}
	
	
	
	private static List<Rating> listaRatings(){
		RatingService service = new RemoteEJBServicesLocator().getRatingService();
		List<Rating> ratings = service.listarRatings();
		for(Rating r : ratings){
			System.out.println(r.getId() + " " + r.getSeatFromTripId() + 
					" " + r.getSeatFromUserId());
		}
		return ratings;
	}
	
	private static void borrarRatings(){
		while(true){
			System.out.println("Lista de ratings: ");
			List<Rating> r = listaRatings();
			long id = Console.readInt("Seleccione el id del rating "
					+ "que desee borrar: ");
			for(Rating rat : r){
				if(rat.getId().equals(id)){
					new RemoteEJBServicesLocator().getRatingService()
					.eliminarComentarios(rat.getId());
					System.out.println("Rating borrado");
					return;
				}
			}
			System.out.println("Error, por favor introduzca un id de la lista");
		}
	}
	
	private static void cancelarUsuario(){
		while(true){
			System.out.println("Usuarios disponibles: ");
			mostrarUsuarios();
			long idUsuario = Console.readInt("Seleccione el id del usuario: ");
			if(existeUsuario(idUsuario)){
				UserService userS = new RemoteEJBServicesLocator()
				.getUserService();
				User u = userS.findById(idUsuario);
				u.setStatus(UserStatus.CANCELLED);
				ApplicationService service = new RemoteEJBServicesLocator()
				.getApplicationService();
				List<ListaApuntados> user = service.listaApuntadosUsuario(u);
				for(ListaApuntados l : user){
					service.cancelarUsuario(l);
					System.out.println("El usuario se ha cancelado con exito");
				}
				userS.updateUser(u);
				return;
			}
			else{
				System.out.println("El usuario no existe, "
						+ "elija uno de la lista");
			}
		}
	}
	
	private static boolean existeUsuario(long id){
		List<User> usuarios = getUsuarios();
		for(User u : usuarios){
			if(u.getId() == id){
				return true;
			}
		}
		return false;
	}
	
	private static int listaViajePromotor(long id) {
		List<Trip> viajes = new RemoteEJBServicesLocator()
		.getTripService().listarViajes();
		List<Trip> aux = new ArrayList<Trip>();
		for (Trip t : viajes) {
			if (t.getPromoterId().equals(id)) {
				aux.add(t);
			}
		}
		return aux.size();
	}
	
	private static void mostrarUsuarios(){
		List<User> u = getUsuarios();
		for(User us : u){
			System.out.println("Id: "+ us.getId() + " nombre: "
					+ us.getName() + " apellido" +us.getSurname());
		}
	}
	
	private static int listaViajeUsuario(User u) {
		ApplicationService service = new RemoteEJBServicesLocator()
		.getApplicationService();
		List<ListaApuntados> user = service.listaApuntadosUsuario(u);
		return user.size();
	}
	
	private static List<User> getUsuarios(){
		UserService service = new RemoteEJBServicesLocator()
		.getUserService();
		return service.getUsers();
	}
	
	private static void mostrarDatos(){
		List<User> user = getUsuarios();
		for (User u : user) {
			int listaP = listaViajePromotor(u.getId());
			int listaA = listaViajeUsuario(u);
			System.out.println("Usuario: " + u.getName() + " " + u.getSurname() 
					+ " "+ u.getEmail() );
			System.out.println("\tPromovio: " + listaP + " viajes");
			System.out.println("\tSe apunto a: " + listaA + " viajes");
		}
	}

	
	public static void main(String [ ] args) throws Exception{
		run();
	}
}
