package com.sapient.mdb;

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
import javax.jms.QueueConnection;

import org.jboss.annotation.ejb.ResourceAdapter;

import com.sapient.shared.Employee;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "JMSQueue") })
@ResourceAdapter(value = "activemq-rar.rar")
public class MDBBean implements MessageListener {
	static QueueConnection queueConn = null;

	@Resource(mappedName = "java:/activemq/ConnectionFactory")
	private ConnectionFactory connectionFactory;
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
		if (message instanceof ObjectMessage) {
			ObjectMessage msg = (ObjectMessage) message;
			Employee employee;
			try {
				employee = (Employee) msg.getObject();
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
			System.out.println("Employee Details: ");
			System.out.println(employee.getName());
			System.out.println(employee.getDesignation());
		}

	}

}
