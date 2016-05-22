package uo.sdi.client;

import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import alb.util.console.Console;
import uo.sdi.infraestructure.Factory;
import uo.sdi.model.ListaApuntados;
import uo.sdi.model.ListaApuntados.PeticionEstado;
import uo.sdi.model.SeatStatus;
import uo.sdi.model.Trip;
import uo.sdi.model.User;
import uo.sdi.util.Jndi;

public class Main {

	private static final String JMS_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private static final String SDI3QUEUE = "jms/queue/Sdi3Queue";
	private static final String SDI3TOPIC = "jms/topic/Sdi3Topic";
	private static Sdi3ServiceRest client;
	private User cliente;
	private Trip viaje;
	private List<Trip> viajes;
	private Connection con;
	private Session session;
	private MessageProducer sender;

	public static void main(String[] args) throws JMSException {
		Main main = new Main();
		main.run();
	}

	private Main(){
		client = new ResteasyClientBuilder().build()
				.register(new Authenticator("sdi", "password"))
				.target("http://localhost:8280/sdi3-40.Web/rest/")
				.proxy(Sdi3ServiceRest.class);
	}
	
	private void run() throws JMSException {
		initialize();
		String login = Console.readString("Introduzca su login:");
		String password = Console.readString("Introduzca su password:");
		this.cliente = client.buscarUsuario(login, password);
		viajes = client.listarViajesPromotorParticipado(cliente.getId());
		while(true){
			mostrarViajes();
			Long id = Console.readLong("Seleccione uno de los id de la lista");
			viaje = client.buscarViaje(id);
			if(idCorrecto(id)){
				viaje = client.buscarViaje(id);
				break;
			}
			else{
				System.out.println("Error, por favor introduzca uno de los "
						+ "id de la lista");
			}
		}
	
		TextMessage msg = session.createTextMessage();
		msg.setText("");
		System.out.println("Bienvenido al chat del viaje con id " 
				+ viaje.getId() + " y Origen-Destino: " + viaje.getDeparture()
				.getCity() + "-"+ viaje.getDestination().getCity() + ": ");
		while(!msg.getText().equals("exit")){
			msg = createMessage(cliente.getId(), viaje.getId());
			sender.send(msg);
		}
		System.out.println("Fin del chat.");
		close();
	}
	

	private void mostrarViajes() {
		for(Trip t : viajes){
			System.out.println("ID: " + t.getId() + " Origen-Destino: " 
					+ t.getDeparture().getCity() + "-"+ t.getDestination().getCity());
		}	
	}
	
	private boolean idCorrecto(Long id){
		for(Trip t : viajes){
			if(t.getId().equals(id)){
				return true;
			}
		}
		return false;
	}

	private void close(){
		try{
			sender.close();
			session.close();
			con.close();
		}
		catch(JMSException e){
			System.err.println("Error al intentar cerrar las conexiones");
		}
	}

	private void showMessage(TextMessage msg){
		try{
			System.out.println(msg.getText());
		}
		catch(JMSException e){
			System.err.println("Error al intentar mostrar el mensaje");
		}
	}

	private void initialize() throws JMSException {
		ConnectionFactory factory = (ConnectionFactory) Jndi
				.find(JMS_CONNECTION_FACTORY);
		Destination queue = (Destination) Jndi.find(SDI3QUEUE);
		con = factory.createConnection("sdi", "password");
		session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
		sender = session.createProducer(queue);
		con.start();
	}
	
	private TextMessage createMessage(Long idUser, Long idTrip) throws JMSException {
		TextMessage msg = session.createTextMessage();
		msg.setLongProperty("usuario", idUser);
		msg.setLongProperty("viaje", idTrip);
		String t = Console.readString();
		msg.setText(t);
		return msg;
		}
}
