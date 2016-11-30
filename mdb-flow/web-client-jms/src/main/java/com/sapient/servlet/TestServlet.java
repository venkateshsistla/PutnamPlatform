package com.sapient.servlet;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.sapient.shared.Employee;

@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// created ConnectionFactory object for creating connection
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");
		// Establish the connection
		Connection connection;
		try {
			connection = factory.createConnection();
		} catch (JMSException e) {

			throw new RuntimeException(e);
		}
		try {
			connection.start();
		} catch (JMSException e) {
			throw new RuntimeException(e);

		}
		Session session;
		try {
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		Queue queue;
		try {
			queue = session.createQueue("JMSQueue");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);

		}
		// Added as a producer
		MessageProducer producer;
		try {
			producer = session.createProducer(queue);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);

		}
		try {
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		} catch (JMSException e) {
			throw new RuntimeException(e);

		}
		// Create and send the message
		TextMessage msg;
		try {
			msg = session.createTextMessage();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);

		}
		try {
			msg.setText("TestMessage");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);

		}
		try {
			producer.send(msg);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);

		}
		////////////////////////////////////////////////////////////////////////////
		ObjectMessage objMsg;
		try {
			objMsg = session.createObjectMessage();
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Employee employee = new Employee();
		employee.setName(request.getParameter("name"));
		employee.setDesignation(request.getParameter("designation"));
		System.out.println(employee);
		ObjectMessage objMsg1 = null;
		try {
			objMsg1 = session.createObjectMessage();
		} catch (JMSException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			objMsg1.setObject(employee);
		} catch (JMSException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			producer.send(objMsg1);
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			session.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);

		}
		try {
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);

		}
		response.setContentType("text/html;charset=UTF-8");
		response.sendRedirect("welcome.jsp");
	}

}
