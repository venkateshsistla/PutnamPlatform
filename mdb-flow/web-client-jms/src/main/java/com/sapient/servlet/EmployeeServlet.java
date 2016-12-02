package com.sapient.servlet;

import java.io.IOException;
import java.util.Properties;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sapient.shared.Employee;

@WebServlet("/EmployeeServlet")

public class EmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		env.put(Context.PROVIDER_URL, "tcp://localhost:61616");
		env.put("queue.queueSampleQueue", "JMSQueue");

		// get the initial context
		InitialContext ctx = null;
		try {
			ctx = new InitialContext(env);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// lookup the queue object
		Queue queue = null;
		try {
			queue = (Queue) ctx.lookup("queueSampleQueue");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// lookup the queue connection factory
		QueueConnectionFactory connFactory = null;
		try {
			connFactory = (QueueConnectionFactory) ctx.lookup("QueueConnectionFactory");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// create a queue connection
		QueueConnection queueConn = null;
		try {
			queueConn = connFactory.createQueueConnection();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// create a queue session
		QueueSession queueSession = null;
		try {
			queueSession = queueConn.createQueueSession(false, Session.DUPS_OK_ACKNOWLEDGE);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// create a queue sender
		QueueSender queueSender = null;
		try {
			queueSender = queueSession.createSender(queue);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			queueSender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMessage objMsg = null;
		try {
			objMsg = queueSession.createObjectMessage();
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Employee employee = new Employee();
		employee.setName(request.getParameter("name"));
		employee.setDesignation(request.getParameter("designation"));

		try {
			objMsg.setObject(employee);
		} catch (JMSException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			queueSender.send(objMsg);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Pushed into queue");
		try {
			queueConn.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.sendRedirect("welcome.jsp");
	}

}
