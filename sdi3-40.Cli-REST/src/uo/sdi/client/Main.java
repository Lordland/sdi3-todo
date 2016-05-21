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
		client = new ResteasyClientBuilder().build()
				.register(new Authenticator("sdi", "password"))
				.target("http://localhost:8280/sdi3-40.Web/rest/")
				.proxy(Sdi3RestService.class);
	}

	private void run() {
		while (true) {
			boolean mensaje = false;
			try {
				login = Console.readString("Introduzca su login:");
				password = Console.readString("Introduzca su password:");
				this.cliente = client.buscarUsuario(login, password);
			} catch (Exception e) {
				mensaje = true;
				System.out
						.println("Ha introducido un usuario, pass o ambos vacíos. Por favor, introduzca algún dato");

			}
			if (cliente != null) {
				listadoViajesPromovidos(cliente); // A Get
													// operation

			} else {
				if (!mensaje) {
					System.out
							.println("El usuario o la contraseña no eran correctos. Por favor, vuelva a intentarlo");
				}
			}
		}
	}

	private void listadoViajesPromovidos(User u) {
		while (true) {
			List<Trip> viajes = client.listarViajes();
			List<Trip> v = new ArrayList<Trip>();
			System.out.println("El usuario " + u.getName() + " "
					+ u.getSurname() + " ha promovido los siguientes viajes: ");
			for (Trip t : viajes) {
				if (t.getPromoterId().equals(u.getId())
						&& t.getStatus().equals(TripStatus.OPEN)) {
					v.add(t);
					System.out.println("Id:" + t.getId() + " Origen: "
							+ t.getDeparture().getCity() + " Destino: "
							+ t.getDestination().getCity());
				}
			}
			if (v.isEmpty()) {
				System.out.println("Este usuario no ha promovido ningun viaje");
			}

			seleccionaViaje(v);
			
		}
	}

	private void seleccionaViaje(List<Trip> v) {
		while (true) {
			Long id = Console.readLong("Si desea salir de sesion"
					+ " introduzca 0, de otra forma introduzca "
					+ "el id de uno de los viajes de la lista");
			if (id.equals(0L)) {
				System.exit(0);
			}
			for (Trip t : v) {
				if (t.getId().equals(id)) {
					listarApuntados(id);
					return;
				}
			}
			System.out
					.println("El viaje no está en la lista. Por favor, inténtelo de nuevo");
		}
		
	}

	private void listarApuntados(Long id) {
		Trip t = client.buscarViaje(id);
		List<ListaApuntados> v = client.listarApuntadosAUnViaje(t);
		for (ListaApuntados l : v) {
			System.out.println("\tUsuario: Id=" + l.getUsuario().getId()
					+ " Nombre=" + l.getUsuario().getName() + " Apellido="
					+ l.getUsuario().getSurname() + " Viaje: Origen-Destino="
					+ l.getViaje().getDeparture().getCity() + "-"
					+ l.getViaje().getDestination().getCity());
		}

		while (true) {
			Long idUs = Console.readLong("Seleccione el id del usuario que "
					+ "quiera aceptar en el viaje. 0 para salir");
			if (idUs.equals(null)) {
				System.out.println("Error, elija un id de los de la lista");
			} else {
				for (ListaApuntados l : v) {
					if (l.getUsuario().getId().equals(idUs)) {
						client.aceptarUsuario(l);
						return;
					}
				}
				System.out
						.println("El id que ha introducido no está en la lista. Por favor, introduzca uno de la lista.");
				if (idUs.equals(0L)) {
					System.out
							.println("Ha decidido salir. Hasta la próxima :)");
					return;
				}

			}
		}

	}

}
