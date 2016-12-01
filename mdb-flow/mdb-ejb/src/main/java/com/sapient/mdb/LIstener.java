package com.sapient.mdb;

import java.util.Properties;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.sapient.shared.Employee;

public class LIstener implements MessageListener, ExceptionListener {
	static QueueConnection queueConn = null;

	public static void main(String[] args) throws Exception {
		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		env.put(Context.PROVIDER_URL, "tcp://localhost:61616");
		env.put("queue.queueSampleQueue", "JMSQueue");

		InitialContext ctx = new InitialContext(env);

		Queue queue = (Queue) ctx.lookup("queueSampleQueue");

		QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");

		queueConn = connFactory.createQueueConnection();

		QueueSession queueSession = queueConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

		QueueReceiver queueReceiver = queueSession.createReceiver(queue);

		LIstener Receiver = new LIstener();
		queueReceiver.setMessageListener(Receiver);

		queueConn.setExceptionListener(Receiver);

		queueConn.start();
	}

	@Override
	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
			ObjectMessage msg = (ObjectMessage) message;
			Employee employee;
			try {
				employee = (Employee) msg.getObject();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
			System.out.println("Employee Details: ");
			System.out.println(employee.getName());
			System.out.println(employee.getDesignation());
		}
	}

	@Override
	public void onException(JMSException exception) {
		System.err.println("an error occurred: " + exception);
	}
}
