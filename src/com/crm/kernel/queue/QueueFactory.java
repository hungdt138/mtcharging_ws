package com.crm.kernel.queue;

import java.io.FileInputStream;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.crm.provisioning.cache.MQConnection;
import com.crm.provisioning.cache.MQConnectionPool;
import com.crm.provisioning.message.CommandMessage;
import com.crm.util.AppProperties;
import com.crm.util.WSConfiguration;

import com.fss.util.AppException;

public class QueueFactory {
    public static String QUEUE_FACTORY = "jms/CCS";

    public static String ORDER_REQUEST_QUEUE = "test/OrderRoute";
    public static String ORDER_RESPONSE_QUEUE = "test/OrderResponse";

    public static boolean queueServerEnable = true;
    public static Context context = null;
    public static QueueConnectionFactory connectionFactory = null;
    public static QueueConnection queueConnection = null;
    public static MQConnectionPool queueConnectionPool = null;
    public static int queueConnectionPoolSize = 10;
    public static int queuePoolWaitTime = 1000;
    public static boolean queuePersistent = false;
    public static int queueDeliverTimeout = 0;
    public static HashMap<String, Queue> appQueues = new HashMap<String, Queue>();

    public static ConcurrentHashMap<String, Object> callbackListerner = new ConcurrentHashMap<String, Object>();
    public static ConcurrentHashMap<String, Object> callbackOrder = new ConcurrentHashMap<String, Object>();

    private static Object mutex = "mutex";
    public static Logger log = Logger.getLogger(QueueFactory.class);

    public static MQConnection getConnection() throws Exception {
	if (!queueServerEnable) {
	    return null;
	}

	if (context == null)
	    initContext();

	if (queueConnectionPool == null) {

	    synchronized (mutex) {
		if (queueConnectionPool == null) {
		    queueConnectionPool = new MQConnectionPool(
			    queueConnectionPoolSize);
		}
		if (queueConnectionPool.isClosed()) {
		    queueConnectionPool.open();
		}
	    }
	}

	return queueConnectionPool.getConnection();
    }

    public static void closeConnection(MQConnection connection) {
	if ((connection == null) || (queueConnectionPool == null)) {
	    return;
	}

	try {
	    queueConnectionPool.returnConnection(connection);
	} catch (Exception e) {
	    if (connection != null) {
		connection.markError();
	    }

	    log.error(e);
	    e.printStackTrace();
	}
    }

    public static QueueSession getSession() throws Exception {
	MQConnection connection = null;

	try {
	    connection = getConnection();

	    return connection.getQueueConnection().createQueueSession(false,
		    Session.AUTO_ACKNOWLEDGE);
	} catch (Exception e) {
	    if (connection != null) {
		connection.markError();
	    }

	    throw e;
	} finally {
	    closeConnection(connection);
	}
    }

    public static MessageProducer createProducer(QueueSession session,
	    Queue queue, long timeout) throws JMSException {
	MessageProducer producer = session.createProducer(queue);

	if (!queuePersistent) {
	    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	}
	if (timeout > 0) {
	    producer.setTimeToLive(timeout);
	}

	return producer;
    }

    public static MessageProducer createProducer(QueueSession session,
	    Queue queue) throws Exception {
	return createProducer(session, queue, 0);
    }

    public static Message sendMessage(QueueSession session, Object object,
	    Queue queue, String correlationId, String[] propsName,
	    Object[] propsValue, long timeout) throws Exception {
	Message message = null;
	MessageProducer producer = null;

	try {
	    if (object instanceof Message) {
		message = (Message) object;
	    } else if (object instanceof Serializable) {
		message = createObjectMessage(session, (Serializable) object);
	    } else {
		throw new Exception("invalid-message-object");
	    }

	    if (!correlationId.equals("")) {
		message.setJMSCorrelationID(correlationId);
	    }

	    if ((propsName != null) && (propsValue != null)) {
		if (propsName.length > 0) {
		    for (int i = 0; i < propsName.length; i++) {
			if (propsValue[i] instanceof String) {
			    message.setStringProperty(propsName[i],
				    (String) propsValue[i]);
			} else if (propsValue[i] instanceof Byte) {
			    message.setByteProperty(propsName[i],
				    ((Byte) propsValue[i]).byteValue());
			} else if (propsValue[i] instanceof Integer) {
			    message.setIntProperty(propsName[i],
				    ((Integer) propsValue[i]).intValue());
			} else if (propsValue[i] instanceof Boolean) {
			    message.setBooleanProperty(propsName[i],
				    ((Boolean) propsValue[i]).booleanValue());
			} else if (propsValue[i] instanceof Short) {
			    message.setShortProperty(propsName[i],
				    ((Short) propsValue[i]).shortValue());
			} else if (propsValue[i] instanceof Long) {
			    message.setLongProperty(propsName[i],
				    ((Long) propsValue[i]).longValue());
			} else if (propsValue[i] instanceof Double) {
			    message.setDoubleProperty(propsName[i],
				    ((Double) propsValue[i]).doubleValue());
			} else if (propsValue[i] instanceof Float) {
			    message.setFloatProperty(propsName[i],
				    ((Float) propsValue[i]).floatValue());
			} else {
			    message.setObjectProperty(propsName[i],
				    propsValue[i]);
			}
		    }
		}
	    }

	    producer = createProducer(session, queue, timeout);
	    producer.send(message);

	    if (session.getTransacted()) {
		session.commit();
	    }
	} catch (Exception e) {
	    throw e;
	} finally {
	    QueueFactory.closeQueue(producer);
	}

	return message;
    }

    public static Message sendMessage(Object object, String queueName)
	    throws Exception {
	return sendMessage(object, queueName, 0);
    }

    public static Message sendMessage(Object object, String queueName,
	    long timeout) throws Exception {
	QueueSession session = null;

	try {
	    session = getSession();

	    return sendMessage(session, object, getQueue(queueName), timeout);
	} finally {
	    closeQueue(session);
	}
    }

    public static Message sendMessage(QueueSession session, Object object,
	    Queue queue) throws Exception {
	return sendMessage(session, object, queue, 0);
    }

    public static Message sendMessage(QueueSession session, Object object,
	    Queue queue, long timeout) throws Exception {
	return sendMessage(session, object, queue, "", null, null, timeout);
    }

    public static Message sendMessage(Object object, Queue queue,
	    String correlationId, String[] propsName, Object[] propsValue,
	    long timeout) throws Exception {
	QueueSession session = null;

	try {
	    session = getSession();

	    return sendMessage(session, object, queue, correlationId,
		    propsName, propsValue, timeout);
	} finally {
	    closeQueue(session);
	}
    }

    public static Queue getQueue(String queueName) throws Exception {
	Queue queue = null;

	try {
	    queue = appQueues.get(queueName);

	    if (queue == null) {
		if (context == null)
		    initContext();
		queue = (Queue) context.lookup(queueName);

		appQueues.put(queueName, queue);
	    }
	} catch (javax.naming.NamingException e) {
	    throw new AppException("queue-not-found", queueName);
	} catch (Exception e) {
	    throw e;
	}

	return queue;
    }

    public static void closeQueue(Object object) {
	if (object == null) {
	    return;
	}

	try {
	    if (object instanceof QueueBrowser) {
		((QueueBrowser) object).close();
	    } else if (object instanceof MessageConsumer) {
		((MessageConsumer) object).close();
	    } else if (object instanceof MessageProducer) {
		((MessageProducer) object).close();
	    } else if (object instanceof QueueReceiver) {
		((QueueReceiver) object).close();
	    } else if (object instanceof QueueSession) {
		((QueueSession) object).close();
	    } else if (object instanceof QueueConnection) {
		QueueConnection connection = (QueueConnection) object;

		connection.stop();
		connection.close();
	    }
	} catch (Exception e) {
	    log.info("safeClose:", e);
	}
    }

    public static Message createObjectMessage(QueueSession queueSession,
	    Object object) throws Exception {
	if (object instanceof Message) {
	    return (Message) object;
	} else if (object instanceof Serializable) {
	    try {
		ObjectMessage message = queueSession.createObjectMessage();

		message.setObject((Serializable) object);

		if (object instanceof CommandMessage) {
		    message.setJMSCorrelationID(((CommandMessage) object)
			    .getCorrelationID());
		}

		return message;
	    } catch (Exception e) {
		throw e;
	    }
	}

	throw new Exception("invalid-message-body");
    }

    public static Message createObjectMessage(Serializable content)
	    throws Exception {
	QueueSession session = null;

	try {
	    session = getSession();

	    return createObjectMessage(session, content);
	} finally {
	    closeQueue(session);
	}
    }

    public static Object getContentMessage(Message message) throws Exception {
	if (message == null) {
	    return null;
	} else if (message instanceof ObjectMessage) {
	    return ((ObjectMessage) message).getObject();
	} else {
	    return message;
	}
    }

    public static void emptyQueue(QueueSession queueSession, String queueName)
	    throws Exception {
	QueueReceiver receiver = null;

	try {
	    receiver = queueSession.createReceiver(getQueue(queueName));

	    while (receiver.receiveNoWait() != null) {

	    }
	} finally {
	    closeQueue(receiver);
	}
    }

    public static QueueConnection createQueueConnection() throws Exception {
	initContext();

	QueueConnection connection = null;

	try {
	    connection = connectionFactory.createQueueConnection();

	    connection.start();
	} catch (Exception e) {
	    closeQueue(connection);

	    throw e;
	}

	return connection;
    }

    public static void initContext() throws Exception {
	if ((context != null) & (connectionFactory != null)) {
	    return;
	}

	synchronized (mutex) {
	    appQueues.clear();

	    try {
		if (context == null) {
		    if (queueConnectionPool != null) {
			try {
			    queueConnectionPool.destroyPool();
			} catch (Exception e) {
			    e.printStackTrace();
			} finally {
			    queueConnectionPool = null;
			}
		    }

		    // get server host
		    AppProperties configProvider = new AppProperties();

		    String fileConfig = WSConfiguration.configPath
			    + "ServerConfig.txt";
		    configProvider.loadFromFile(fileConfig);

		    WSConfiguration.debugMonitor("Loaded success: "
			    + fileConfig);
		    queueConnectionPoolSize = configProvider.getInteger(
			    "connectionFactory", 100);

		    QUEUE_FACTORY = configProvider.getString("queue.factory",
			    "jms/CCS");

		    ORDER_REQUEST_QUEUE = configProvider.getString(
			    "queue.orderRoute", "test/OrderRoute");
		    ORDER_RESPONSE_QUEUE = configProvider.getString(
			    "queue.orderResponse", "test/OrderResponse");

		    // get context properties
		    Properties properties = new Properties();

		    properties.load(new FileInputStream(
			    WSConfiguration.configPath + "jndi.properties"));

		    String host = properties
			    .getProperty("org.omg.CORBA.ORBInitialHost");
		    String port = properties
			    .getProperty("org.omg.CORBA.ORBInitialPort");

		    if ((host == null) || host.equals("")) {
			host = "localhost";
		    }
		    if ((port == null) || port.equals("")) {
			host = "3700";
		    }

		    System.setProperty("org.omg.CORBA.ORBInitialHost", host);
		    System.setProperty("org.omg.CORBA.ORBInitialPort", port);

		    context = new InitialContext(properties);
		}

		// lookup the queue connection factory
		connectionFactory = (QueueConnectionFactory) context
			.lookup(QUEUE_FACTORY);
	    } catch (Exception e) {
		context = null;
		connectionFactory = null;

		throw e;
	    }
	}
    }

    public static void resetContext() throws Exception {
	try {
	    if (queueConnection != null) {
		queueConnection.stop();

		queueConnection.close();
	    }
	} finally {
	    context = null;

	    connectionFactory = null;
	}
    }
}
