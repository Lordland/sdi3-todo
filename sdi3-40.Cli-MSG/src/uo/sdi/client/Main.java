package uo.sdi.client;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;

import uo.sdi.util.Jndi;

public class Main {

	private static final String JMS_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private static final String NOTANEITOR_QUEUE = "jms/queue/NotaneitorQueue";

	public static void main(String[] args) throws JMSException {
		new Main().run();
	}

	private Connection con;
	private Session session;
	private MessageProducer sender;

	private void run() throws JMSException {
		initialize();
		for (int i = 0; i < 5; i++) {
			MapMessage msg = createMessage();
			showMessage(msg);
			sender.send(msg);
		}
		close();
	}

	private void close() {
		// TODO Auto-generated method stub
		
	}

	private void showMessage(MapMessage msg) {
		// TODO Auto-generated method stub
		
	}

	private void initialize() throws JMSException {
		ConnectionFactory factory = (ConnectionFactory) Jndi
				.find(JMS_CONNECTION_FACTORY);
		Destination queue = (Destination) Jndi.find(NOTANEITOR_QUEUE);
		con = factory.createConnection("sdi", "password");
		session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
		sender = session.createProducer(queue);
		con.start();

	}
	
	private MapMessage createMessage() throws JMSException {
		MapMessage msg = session.createMapMessage();
		msg.setString("command", "new");
		msg.setString("iduser", "123456");
		msg.setString("email", "jms@email.es");
		msg.setString("name", "JMS Name "
		+ " "
		+ System.currentTimeMillis());
		msg.setString("surname", "JMS Surname");
		return msg;
		}
}
