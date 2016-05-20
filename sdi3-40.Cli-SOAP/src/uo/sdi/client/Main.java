package uo.sdi.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.sdi.ws.ApplicationService;
import com.sdi.ws.EJBApplicationServiceService;
import com.sdi.ws.EJBRatingServiceService;
import com.sdi.ws.EJBTripServiceService;
import com.sdi.ws.EJBUserServiceService;
import com.sdi.ws.ListaApuntados;
import com.sdi.ws.Rating;
import com.sdi.ws.RatingService;
import com.sdi.ws.Trip;
import com.sdi.ws.TripService;
import com.sdi.ws.User;
import com.sdi.ws.UserService;
import com.sdi.ws.UserStatus;

import alb.util.console.Console;

public class Main {
	private static String mensaje = "Opciones: \n\t1.Listar usuarios y viajes "
			+ "promovidos y apuntados\n\t2.Excluir usuario\n\t3.Listar "
			+ "comentarios y puntuaciones\n\t4.Eliminar comentarios y "
			+ "puntuaciones\n\t0.Salir";
	
	private static void run() throws Exception {
		while(true){
			System.out.println(mensaje);
			Integer opcion = Console.readInt("Diga su opcion: "); 
			if(opcion == null){
				System.out.println("Por favor elija una de las opciones");
			}
			else if(opcion == 1){
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
	
	
	private static void listar() throws DatatypeConfigurationException{
		TripService serviceT = new EJBTripServiceService().getTripServicePort();
		List<Trip> trips = serviceT.listarViajes();
		List<Trip> tr = new ArrayList<Trip>();
		Date d = new Date();
		d.setTime(d.getTime() + 30 * 1000 * 60 * 60 * 24);
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(d);
		XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		for(Trip t : trips){
			if(t.getDepartureDate().compare(date2) < 0){
				tr.add(t);
			}
		}
		for(Trip t : tr){
			System.out.println("Viaje: " + t.getId() + " salida: " + 
					t.getDeparture().getCity() +	" destino: " 
					+ t.getDestination().getCity());
			RatingService service = new EJBRatingServiceService().getRatingServicePort();
			List<Rating> ratings = service.listarComentarios(t);
			for(Rating r : ratings){
				User u1 = new EJBUserServiceService().getUserServicePort()
						.findById(r.getSeatFromUserId());
				User u2 = new EJBUserServiceService().getUserServicePort()
						.findById(r.getSeatAboutUserId());
				System.out.println("\t"+t.getDestination() + " " + 
						u1.getName() + " " + u2.getName() + " " + r.getValue()
						+ "\n\t" + r.getComment());
			}
		}
	}
	
	
	
	private static List<Rating> listaRatings(){
		RatingService service = new EJBRatingServiceService().getRatingServicePort();
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
			Long id = Console.readLong("Seleccione el id del rating "
					+ "que desee borrar o 0 para salir");
			for(Rating rat : r){
				if(id != null && rat.getId().equals(id)){
					new EJBRatingServiceService().getRatingServicePort()
					.eliminarComentarios(rat.getId());
					System.out.println("Rating borrado");
					return;
				}
			}
			if(id == 0L){
				return;
			}
			System.out.println("Error, por favor introduzca un id de la lista");
		}
	}
	
	private static void cancelarUsuario(){
		while(true){
			System.out.println("Usuarios disponibles: ");
			mostrarUsuarios();
			Long idUsuario = Console.readLong("Seleccione el id del usuario o 0 para salir: ");
			if(idUsuario!=null && existeUsuario(idUsuario)){
				UserService userS = new EJBUserServiceService()
				.getUserServicePort();
				User u = userS.findById(idUsuario);
				u.setStatus(UserStatus.CANCELLED);
				ApplicationService service = new EJBApplicationServiceService()
				.getApplicationServicePort();
				List<ListaApuntados> user = service.listaApuntadosUsuario(u);
				for(ListaApuntados l : user){
					service.cancelarUsuario(l);
					System.out.println("El usuario se ha cancelado con exito");
				}
				userS.updateUser(u);
				return;
			}
			if(idUsuario == 0L){
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
		List<Trip> viajes = new EJBTripServiceService()
		.getTripServicePort().listarViajes();
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
		ApplicationService service = new EJBApplicationServiceService()
		.getApplicationServicePort();
		List<ListaApuntados> user = service.listaApuntadosUsuario(u);
		return user.size();
	}
	
	private static List<User> getUsuarios(){
		UserService service =  new EJBUserServiceService()
		.getUserServicePort();
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
