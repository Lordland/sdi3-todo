package uo.sdi.integration;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TemporaryTopic;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ejb.ActivationConfigProperty;

import uo.sdi.infraestructure.Factory;
import uo.sdi.model.Application;
import uo.sdi.model.Trip;

@MessageDriven(activationConfig = { @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/Sdi3Queue") })
public class Sdi3Listener implements MessageListener {

	private TopicConnection connect = null;
	private TopicSession pubSession = null;
	private TopicSession subSession = null;
	private TopicPublisher publisher = null;
	private TopicSubscriber subscriber = null;
	private Topic hotDealsTopic = null;
	private TemporaryTopic buyOrdersTopic = null;

	@Override
	public void onMessage(Message msg) {
		System.out.println("Sdi3Listener: Msg received");
		try {
			process(msg);
		} catch (JMSException | NamingException jex) {
			// here we should log the exception
			jex.printStackTrace();
		}
	}

	private void process(Message msg) throws JMSException, NamingException {
		if (msg != null) {
			sendAsync(msg);
		}

	}

	public void setupPubSub() throws JMSException, NamingException {
		try {
			// Properties env = new Properties( );
			// ... specify the JNDI properties specific to the vendor

			InitialContext jndi = new InitialContext(/* env */);

			TopicConnectionFactory factory = (TopicConnectionFactory) jndi
					.lookup("ConnectionFactory");
			connect = factory.createTopicConnection("sdi", "password");

			pubSession = connect.createTopicSession(false,
					Session.AUTO_ACKNOWLEDGE);
			subSession = connect.createTopicSession(false,
					Session.AUTO_ACKNOWLEDGE);

			hotDealsTopic = (Topic) jndi.lookup("topic/Sdi3Topic");
			publisher = pubSession.createPublisher(hotDealsTopic);

			buyOrdersTopic = subSession.createTemporaryTopic();

			subscriber = subSession.createSubscriber(buyOrdersTopic);
			subscriber.setMessageListener(this);

			connect.start();

		} catch (javax.jms.JMSException jmse) {
			jmse.printStackTrace();
			System.exit(1);
		} catch (javax.naming.NamingException jne) {
			jne.printStackTrace();
			System.exit(1);
		}
	}

	public void sendAsync(Message msg) throws JMSException, NamingException {
		// Setup the pub/sub connection, session
		setupPubSub();
		// Enviar el mensaje a publisher, con el id del viaje y el id de los
		// usuarios promotores/apuntados para que lo filtre el cliente
		Long id = msg.getLongProperty("viaje");
		String s = getIds(msg);
		Message tm = subSession.createMessage();
        tm.setLongProperty("viaje", id);
        tm.setStringProperty("usuarios", s);
        tm.setStringProperty("mensaje", msg.getStringProperty("mensaje"));
        publisher.publish(tm);
	}
	
	
	private String getIds(Message msg) throws JMSException{
		Long id = msg.getLongProperty("viaje");
        List<String> s = new ArrayList<String>();
        List<Application> a = Factory.services.getApplicationService()
        		.getApplication();
        Trip t = Factory.services.getTripService().buscarViaje(id);
        for(Application ap : a){
        	if(ap.getTripId().equals(id)){
        		s.add(""+ap.getUserId());
        	}
        }
        s.add(""+t.getPromoterId());
        String cadena= "";
        for(int i=0;i<s.size();i++){
        	if(i == s.size()-1){
        		cadena = cadena + s.get(i);
        	}
        	else{
        		cadena = cadena + s.get(i) + ",";
        	}
        }
        return cadena;
	}
}
