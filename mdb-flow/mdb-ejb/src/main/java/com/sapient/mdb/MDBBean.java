package com.sapient.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.QueueConnection;

import com.sapient.shared.Employee;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "JMSQueue") })
public class MDBBean implements MessageListener, ExceptionListener {
	static QueueConnection queueConn = null;

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

	@Override
	public void onException(JMSException arg0) {
		System.out.println("error occured");
	}
}
