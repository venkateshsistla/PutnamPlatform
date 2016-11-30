package com.sapient.mdb;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.jboss.annotation.ejb.ResourceAdapter;

import com.sapient.shared.Employee;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue.queue_in") })
@ResourceAdapter("activemq-ra.rar")
public class MDBBean implements MessageListener {

	@Resource(mappedName = "java:/activemq/ConnectionFactory")
	private ConnectionFactory connectionFactory;
	@Resource(mappedName = "java:/activemq/queue_out")

	private Destination queue;
	private Connection connection;

	public void init() throws JMSException {
		connection = connectionFactory.createConnection();
		connection.start();
	}

	public void destroy() throws JMSException {
		connection.close();
	}

	@Override
	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
				System.out.println("Queue: I received a TextMessage at " + new Date());
				TextMessage msg = (TextMessage) message;
				System.out.println("Message is : " + msg.getText());
			} else if (message instanceof ObjectMessage) {
				System.out.println("Queue: I received an ObjectMessage at " + new Date());
				ObjectMessage msg = (ObjectMessage) message;
				Employee employee = (Employee) msg.getObject();
				System.out.println("Employee Details: ");
				System.out.println(employee.getName());
				System.out.println(employee.getDesignation());
			} else {
				System.out.println("Not a valid message for this Queue MDB");
			}

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
