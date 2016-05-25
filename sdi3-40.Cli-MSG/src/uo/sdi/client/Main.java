package uo.sdi.client;

import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.NamingException;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import alb.util.console.Console;
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
	private TopicConnection tConn = null;
	private TopicSession tSession = null;
	private Topic topic = null;

	public static void main(String[] args) throws JMSException, NamingException {
		Main main = new Main();
		main.run();
	}

	private Main() {
		client = new ResteasyClientBuilder().build()
				.register(new Authenticator("sdi", "password"))
				.target("http://localhost:8280/sdi3-40.Web/rest/")
				.proxy(Sdi3ServiceRest.class);
	}

	private void run() throws JMSException, NamingException {
		initialize();
		while(cliente == null){
			String login = Console.readString("Introduzca su login:");
			String password = Console.readString("Introduzca su password:");
			this.cliente = client.buscarUsuario(login, password);
			if(cliente == null){
				System.out.println("El usuario o la contraseña son "
						+ "incorrectos, pruebe de nuevo");
			}
		}
		viajes = client.listarViajesPromotorParticipado(cliente.getId());
		while (true) {
			mostrarViajes();
			Long id = Console.readLong("Seleccione uno de los id de la lista");
			viaje = client.buscarViaje(id);
			if (idCorrecto(id)) {
				viaje = client.buscarViaje(id);
				break;
			} else {
				System.out.println("Error, por favor introduzca uno de los "
						+ "id de la lista");
			}
		}

		TextMessage msg = session.createTextMessage();
		msg.setStringProperty("mensaje", "");
		System.out.println("Bienvenido al chat del viaje con id "
				+ viaje.getId() + " y Origen-Destino: "
				+ viaje.getDeparture().getCity() + "-"
				+ viaje.getDestination().getCity() + ": ");
		while (!msg.getStringProperty("mensaje").equals("exit")) {
			msg = createMessage(cliente.getId(), viaje.getId());
			sender.send(msg);
			showMessage();
		}
		System.out.println("Fin del chat.");
		close();
	}

	private void mostrarViajes() {
		for (Trip t : viajes) {
			System.out.println("ID: " + t.getId() + " Origen-Destino: "
					+ t.getDeparture().getCity() + "-"
					+ t.getDestination().getCity());
		}
	}

	private boolean idCorrecto(Long id) {
		for (Trip t : viajes) {
			if (t.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	private void close() {
		try {
			sender.close();
			session.close();
			con.close();
		} catch (JMSException e) {
			System.err.println("Error al intentar cerrar las conexiones");
		}
	}
	
	private boolean enLista(String[] s){
		for(int i=0;i<s.length;i++){
			if(s[i].equals(cliente.getId()+"")){
				return true;
			}
		}
		return false;
	}

	private void showMessage() {
		try {
			TopicSubscriber recv = tSession.createSubscriber(topic);
			Message recibir = recv.receive();
			String idU = recibir.getStringProperty("usuario");
			//String[] s = idU.split(",");
			Long idT = recibir.getLongProperty("viaje");
		    if (recibir.equals(null)) {
		    	System.out.println("Timed out waiting for msg");
		    }
		    if(/*enLista(s)*//*idU.equals(cliente.getId()+"") &&*/ viaje.getId().equals(idT)){
		        System.out.println("TopicSubscriber.recv, msgt="+recibir.getStringProperty("mensaje"));
		    }
		} catch (JMSException e) {
			System.err.println("Error al intentar mostrar el mensaje");
		}
	}

	private void initialize() throws JMSException, NamingException {
		ConnectionFactory factory = (ConnectionFactory) Jndi
				.find(JMS_CONNECTION_FACTORY);
		Destination queue = (Destination) Jndi.find(SDI3QUEUE);
		con = factory.createConnection("sdi", "password");
		session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
		sender = session.createProducer(queue);
		con.start();
		setupPubSub();
	}

	private TextMessage createMessage(Long idUser, Long idTrip)
			throws JMSException {
		TextMessage msg = session.createTextMessage();
		msg.setLongProperty("usuario", idUser);
		msg.setLongProperty("viaje", idTrip);
		String t = Console.readString();
		msg.setStringProperty("mensaje", t);
		return msg;
	}

	public void setupPubSub() throws JMSException, NamingException {
		// ... specify the JNDI properties specific to the vendor
		TopicConnectionFactory tcf = 
				(TopicConnectionFactory) Jndi.find(JMS_CONNECTION_FACTORY);
		tConn = tcf.createTopicConnection("sdi","password");
		topic = (Topic) Jndi.find(SDI3TOPIC);
		tSession = tConn.createTopicSession(false,
				TopicSession.AUTO_ACKNOWLEDGE);
		tConn.start();
	}
}
