package uo.sdi.client;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import alb.util.console.Console;
import uo.sdi.util.Jndi;

public class Main {

	private static final String JMS_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private static final String SDI3QUEUE = "jms/queue/Sdi3Queue";
	private static final String SDI3TOPIC = "jms/topic/Sdi3Topic";

	public static void main(String[] args) throws JMSException {
		new Main().run();
	}

	private Connection con;
	private Session session;
	private MessageProducer sender;

	private void run() throws JMSException {
		initialize();
		//Long id = Console.readLong("Seleccione uno de los id de la lista");
		//metodo para mostrar los viajes de los que es promotor y en los que
		//participa "List<Trip> l = listaViajesParticipados()"
		//luego se hace comprobacion de que el id se encuentre en la lista
		//se acepta el id y se saca el viaje y entonces se puede enviar msgs
		//al chat de ese viaje (aunque ni idea de momento de como se hace,
		//a las 10 o asi santi llegara de nuevo)		
		TextMessage msg = session.createTextMessage();
		msg.setText("");
		while(!msg.equals("exit")){
			msg = createMessage();
			showMessage(msg);
			sender.send(msg);
		}
		close();
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
	
	private TextMessage createMessage() throws JMSException {
		TextMessage msg = session.createTextMessage();
		String t = Console.readString();
		msg.setText(t);
//		msg.setString("command", "new");
//		msg.setString("iduser", "123456");
//		msg.setString("email", "jms@email.es");
//		msg.setString("name", "JMS Name "
//		+ " "
//		+ System.currentTimeMillis());
//		msg.setString("surname", "JMS Surname");
		return msg;
		}
}
