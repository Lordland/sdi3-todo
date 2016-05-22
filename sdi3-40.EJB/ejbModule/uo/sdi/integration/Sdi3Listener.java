package uo.sdi.integration;

import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ejb.ActivationConfigProperty;


@MessageDriven(
		mappedName="jms/Queue", activationConfig =  {
		        @ActivationConfigProperty(propertyName = "destination",
                        propertyValue = "topic/Sdi3Topic")}
			)
public class Sdi3Listener  implements MessageListener{

	private TopicConnection tConn = null;
	private TopicSession tSession = null;
	private Topic topic = null;

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
		if(msg != null){
			sendAsync("recibido DAAAAA");
		}
		
	}
	
	public void setupPubSub() throws JMSException, NamingException {
		// ... specify the JNDI properties specific to the vendor
		InitialContext iniCtx = new InitialContext();
		TopicConnectionFactory tcf = 
				(TopicConnectionFactory)iniCtx.lookup("jms/RemoteConnectionFactory");
		tConn = tcf.createTopicConnection("sdi","password");
		topic = (Topic) iniCtx.lookup("jms/topic/Sdi3Topic");
		tSession = tConn.createTopicSession(false,
				TopicSession.AUTO_ACKNOWLEDGE);
		tConn.start();
	}
	
	public void sendAsync(String text)
	        throws JMSException, NamingException
	    {
	        System.out.println("Begin sendAsync");
	        // Setup the pub/sub connection, session
	        setupPubSub();
	        // Send a text msg
	        TopicPublisher send = tSession.createPublisher(topic);
	        TextMessage tm = tSession.createTextMessage(text);
	        send.publish(tm);
	        System.out.println("sendAsync, sent text=" +  tm.getText());
	        send.close();
	        System.out.println("End sendAsync");
	    }

}


