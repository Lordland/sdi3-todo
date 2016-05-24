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
import uo.sdi.model.Rating;
import uo.sdi.model.Trip;
import uo.sdi.model.User;

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

	private static void listar() {
		TripService serviceT = new RemoteEJBServicesLocator().getTripService();

		List<Trip> viajes = serviceT.listarViajesUltimoMes();
		for (Trip t : viajes) {
			System.out.println("Viaje: " + t.getId() + " salida: "
					+ t.getDeparture().getCity() + " destino: "
					+ t.getDestination().getCity());
			List<Rating> ratings = new RemoteEJBServicesLocator()
					.getRatingService().listarComentarios(t);
			for (Rating r : ratings) {
				User u1 = new RemoteEJBServicesLocator().getUserService()
						.findById(r.getSeatFromUserId());
				User u2 = new RemoteEJBServicesLocator().getUserService()
						.findById(r.getSeatAboutUserId());
				System.out.println("\tDestino: " + t.getDestination().getCity()
						+ " Comentario de: "+ u1.getName() 
						+ " Hacia: " + u2.getName()+" \n\t\tValoracion: "
						+r.getValue() + "\n\t\tComentario: " 
						+ r.getComment() +"\n");
			}
		}
	}

	private static void listaRatings() {
		RatingService service = new RemoteEJBServicesLocator()
				.getRatingService();
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
			if (id.equals(null)) {
				System.out
				.println("Error, por favor introduzca un id de la lista");
			}else if(id.equals(0)){
				return;
			}else {
				new RemoteEJBServicesLocator().getRatingService().borrarRating(id);
				System.out.println("Rating borrado");
				return;
			}
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
			boolean borrado = new RemoteEJBServicesLocator().getUserService()
					.darDeBajaUsuario(idUsuario);
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
		UserService service = new RemoteEJBServicesLocator().getUserService();
		List<User> u = service.getUsers();
		for (User us : u) {
			System.out.println("Id: " + us.getId() + " nombre: " + us.getName()
					+ " apellido" + us.getSurname());
		}
	}

	private static void mostrarDatos() {
		UserService service = new RemoteEJBServicesLocator().getUserService();
		TripService serviceT = new RemoteEJBServicesLocator().getTripService();
		ApplicationService serviceA = new RemoteEJBServicesLocator()
				.getApplicationService();
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
