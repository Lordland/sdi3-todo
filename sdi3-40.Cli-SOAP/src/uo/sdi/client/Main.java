package uo.sdi.client;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import com.sdi.ws.ApplicationService;
import com.sdi.ws.EJBApplicationServiceService;
import com.sdi.ws.EJBRatingServiceService;
import com.sdi.ws.EJBTripServiceService;
import com.sdi.ws.EJBUserServiceService;
import com.sdi.ws.Rating;
import com.sdi.ws.RatingService;
import com.sdi.ws.Trip;
import com.sdi.ws.TripService;
import com.sdi.ws.User;
import com.sdi.ws.UserService;

import alb.util.console.Console;

public class Main {
	private static String mensaje = "Opciones: \n\t1.Listar usuarios y viajes "
			+ "promovidos y apuntados\n\t2.Excluir usuario\n\t3.Listar "
			+ "comentarios y puntuaciones\n\t4.Eliminar comentarios y "
			+ "puntuaciones\n\t0.Salir";

	private static void run() throws Exception {
		while (true) {
			System.out.println(mensaje);
			Integer opcion = Console.readInt("Diga su opcion: ");
			if (opcion == null) {
				System.out.println("Por favor elija una de las opciones");
			} else if (opcion == 1) {
				mostrarDatos();
			} else if (opcion == 2) {
				cancelarUsuario();
			} else if (opcion == 3) {
				listar();
			} else if (opcion == 4) {
				borrarRatings();
			} else if (opcion == 0) {
				return;
			} else {
				System.out.println("Error, vuelva a mirar las opciones "
						+ "disponibles");
			}
		}
	}

	private static void listar() throws DatatypeConfigurationException {
		TripService serviceT = new EJBTripServiceService().getTripServicePort();
		List<Trip> trips = serviceT.listarViajesUltimoMes();
		for (Trip t : trips) {
			System.out.println("Viaje: " + t.getId() + " salida: "
					+ t.getDeparture().getCity() + " destino: "
					+ t.getDestination().getCity());
			List<Rating> ratings = new EJBRatingServiceService()
					.getRatingServicePort().listarComentarios(t);
			for (Rating r : ratings) {
				User u1 = new EJBUserServiceService().getUserServicePort()
						.findById(r.getSeatFromUserId());
				User u2 = new EJBUserServiceService().getUserServicePort()
						.findById(r.getSeatAboutUserId());
				System.out.println("\t" + t.getDestination() + " "
						+ u1.getName() + " " + u2.getName() + " "
						+ r.getValue() + "\n\t" + r.getComment());
			}
		}
	}

	private static void listaRatings() {
		RatingService service = new EJBRatingServiceService()
				.getRatingServicePort();
		List<Rating> ratings = service.listarRatings();
		for (Rating r : ratings) {
			System.out.println(r.getId() + " " + r.getSeatFromTripId() + " "
					+ r.getSeatFromUserId());
		}
	}

	private static void borrarRatings() {
		while (true) {
			System.out.println("Lista de ratings: ");
			listaRatings();
			Long id = Console.readLong("Seleccione el id del rating "
					+ "que desee borrar o 0 para salir");
			if (id == 0L) {
				return;
			} else {
				System.out
						.println("Error, por favor introduzca un id de la lista");
			}
			new EJBRatingServiceService().getRatingServicePort()
					.eliminarComentarios(id);
			System.out.println("Rating borrado");
			return;
		}
	}

	private static void cancelarUsuario() {
		while (true) {
			System.out.println("Usuarios disponibles: ");
			mostrarUsuarios();
			Long idUsuario = Console
					.readLong("Seleccione el id del usuario o 0 para salir: ");
			if (idUsuario == 0L) {
				return;
			}
			UserService service  = new EJBUserServiceService().getUserServicePort();
			boolean borrado = service.darDeBajaUsuario(idUsuario);
			if (!borrado) {
				System.out.println("El usuario no existe, "
						+ "elija uno de la lista");
			} else {
				System.out.println("Usuario borrado satisfactoriamente");
			}
			return;

		}
	}

	private static void mostrarUsuarios() {
		UserService service = new EJBUserServiceService().getUserServicePort();
		List<User> u = service.getUsers();
		for (User us : u) {
			System.out.println("Id: " + us.getId() + " nombre: " + us.getName()
					+ " apellido" + us.getSurname());
		}
	}

	private static void mostrarDatos() {
		UserService service = new EJBUserServiceService().getUserServicePort();
		TripService serviceT = new EJBTripServiceService().getTripServicePort();
		ApplicationService serviceA = new EJBApplicationServiceService().getApplicationServicePort();
		List<User> user = service.getUsers();
		for (User u : user) {
			int listaP = serviceT.listaViajePromotor(u.getId()).size();
			int listaA = serviceA.listaApuntadosUsuario(u).size();
			System.out.println("Usuario: " + u.getName() + " " + u.getSurname()
					+ " " + u.getEmail());
			System.out.println("\tPromovio: " + listaP + " viajes");
			System.out.println("\tSe apunto a: " + listaA + " viajes");
		}
	}

	public static void main(String[] args) throws Exception {
		run();
	}
}
