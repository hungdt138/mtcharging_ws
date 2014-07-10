package com.crm.ejb.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.crm.kernel.queue.QueueFactory;
import com.crm.util.WSConfiguration;

/**
 * Message-Driven Bean implementation class for: ResponseHandler
 */
@MessageDriven(
		activationConfig = { 
			@ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue"), 
		}, 
		mappedName = "queue/OrderResponse")
public class ResponseHandler implements MessageListener {

    /**
     * Default constructor. 
     */
    public ResponseHandler() {
        // TODO Auto-generated constructor stub
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
	try
	{
		Object waitingRequest = QueueFactory.callbackListerner.get(message.getJMSCorrelationID());

		if (waitingRequest != null)
		{
			synchronized (waitingRequest)
			{
				QueueFactory.callbackOrder.put(message.getJMSCorrelationID(), message);

				try
				{
					waitingRequest.notifyAll();
				}
				catch (Exception e)
				{
					WSConfiguration.debugMonitor(e);
				}
			}
		}
	}
	catch (JMSException e)
	{
		e.printStackTrace();
	}
        
    }

}
