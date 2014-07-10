/**
 * 
 */
package com.nms.iwebservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueSession;

import org.apache.log4j.Logger;

import com.crm.kernel.message.Constants;
import com.crm.kernel.queue.QueueFactory;
import com.crm.kernel.sql.Database;
import com.crm.provisioning.message.CommandMessage;
import com.crm.provisioning.util.ResponseConstants;
import com.crm.util.GeneratorSeq;
import com.crm.util.NonceGenerator;
import com.crm.util.User;
import com.crm.util.WSConfiguration;
import com.crm.util.WSSecurity;
import com.fss.util.AppException;
import com.nms.cp.MerchantEntry;
import com.nms.cp.MerchantEntryImpl;

/**
 * @author Hung
 * 
 */
public abstract class ChargingWebserviceBase implements ChargingServices {
    protected static Logger log = Logger
	    .getLogger(ChargingWebserviceBase.class);

    protected ConcurrentHashMap<String, Long> tpsMap = new ConcurrentHashMap<String, Long>();
    
    protected static ConcurrentHashMap<Long, MerchantEntry> cMap = new ConcurrentHashMap<Long, MerchantEntry>();

    protected Calendar starttime = Calendar.getInstance();
    
    public static MerchantEntry getAgent(ServiceRequest request) {
	return cMap.get(request.getcpId());
    }


    public ChargingWebserviceBase() {
	try {
	    WSConfiguration.getConfiguration();
	} catch (Exception e) {
	    log.error(e, e);
	}
    }

    protected void debugMonitor(Object e) {
	if (e instanceof Exception) {
	    log.error(e, (Exception) e);
	} else {
	    log.debug(e);
	}
    }

    protected void infoMonitor(Object e) {
	if (e instanceof Exception) {
	    log.error(e, (Exception) e);
	} else {
	    log.info(e);
	}
    }

    protected String getSessionId(boolean hexa) {
	int sequence = GeneratorSeq.getNextSeq();

	if (hexa) {
	    String hex = Integer.toHexString(sequence).toUpperCase();

	    while (hex.length() < 8) {
		hex = "0" + hex;
	    }

	    hex = "0x" + hex;

	    return hex;
	} else {
	    return String.valueOf(sequence);
	}
    }

    public CommandMessage sendOrder(CommandMessage request, String correlationId, long timeout) throws Exception
	{
		if (timeout <= 0)
		{
			timeout = 30000;
		}

		long startTime = System.currentTimeMillis();

		QueueSession session = null;
		MessageProducer producer = null;
		Message message = null;
		Message callBack = null;
		
		CommandMessage response = null;

		if (request instanceof CommandMessage)
		{
			response = (CommandMessage) request;
		}
		else
		{
			response = new CommandMessage();
		}

		try
		{
			Queue sendQueue = QueueFactory.getQueue(QueueFactory.ORDER_REQUEST_QUEUE);
			session = QueueFactory.getSession();
			message = QueueFactory.createObjectMessage(session, request);
			producer = session.createProducer(sendQueue);

			try
			{
				long beforeSend = System.currentTimeMillis();
				producer.send(message);
				long afterSend = System.currentTimeMillis();
				log.info("Send:" + (afterSend - beforeSend) + ((afterSend - beforeSend) > 20 ? "|high" : "|low") + "|SessionId:" + correlationId);
			}
			catch (Exception ex)
			{
				log.info("Send message error");
				throw ex;
			}
			finally
			{
				QueueFactory.closeQueue(producer);
				QueueFactory.closeQueue(session);
			}
			if (!correlationId.equals(""))
			{
				startTime = System.currentTimeMillis();
				long beforeReceive = System.currentTimeMillis();

				QueueFactory.callbackListerner.put(message.getJMSCorrelationID(), message);
				synchronized (message)
				{
					try
					{
						message.wait(timeout);
					}
					catch (Exception e)
					{

					}
					finally
					{
						callBack = (Message) QueueFactory.callbackOrder.remove(message.getJMSCorrelationID());
						QueueFactory.callbackListerner.remove(message.getJMSCorrelationID());
					}
				}

				long afterReceive = System.currentTimeMillis();

				log.info("Receive:" + (afterReceive - beforeReceive) + ((afterReceive - beforeReceive) > 300 ? "|high " : "|low") +
						"|SessionId:'" + correlationId);
			}
			if (callBack == null)
			{
				throw new AppException("time-out");
			}
			Object content = QueueFactory.getContentMessage(callBack);

			if (content != null)
			{
				if (content instanceof CommandMessage)
				{
					response = (CommandMessage) content;
				}
				else
				{
					response.setStatus(Constants.ORDER_STATUS_DENIED);
					response.setCause(Constants.ERROR);
				}
			}
			else
			{
				response.setStatus(Constants.ORDER_STATUS_DENIED);
				response.setCause(Constants.ERROR_TIMEOUT);
			}

			log.info("Cost time:" + (System.currentTimeMillis() - startTime) + "|sessionId:" + correlationId);
		}
		catch (Exception e)
		{
			log.info(e);
			response.setStatus(Constants.ORDER_STATUS_DENIED);
			response.setCause(Constants.ERROR_RESOURCE_BUSY);
			throw e;
		}
		finally
		{
		}

		if ((response != null) && (response.getStatus() == Constants.ORDER_STATUS_DENIED))
		{
			log.error("sessionId = " + correlationId + ", timeout = " + timeout + " : " + response);
		}

		return response;
	}

    protected CommandMessage sendOrder(CommandMessage message,
	    ServiceRequest request) {
	CommandMessage result = null;

	String sessionId = message.getCorrelationID();

	try {
	    message.setUserId(0);
	    // message.setUserName(request.getUsername());
	    message.setChannel(Constants.CHANNEL_WEB);

	    // String requestContent = getRequest(sessionId, request);

	    message.setRequestValue(ResponseConstants.SESSION_ID, sessionId);
	    // message.setRequestValue(ResponseConstants.VALUE, requestContent);

	   
	    
	    result = sendOrder(message, sessionId, 30000);
	    // result = new CommandMessage();
	    // result.setStatus(Constants.ORDER_STATUS_APPROVED);
	    // result.setCause(Constants.SUCCESS);
	    // Thread.sleep(1200);

	} catch (Exception e) {
	    log.error(e);
	}

	if (result != null) {
	    if (log.isDebugEnabled()) {
		// log.debug("response: " + result.toString());
	    }
	} else {
	    log.error("request with sessionId = " + sessionId
		    + " has response is null");
	}

	return result;
    }

    protected boolean authenticate(String username, String password,
	    String action) {
	WSSecurity security = WSSecurity.getSecuriry();

	User user = security.authenticate(username, password);

	if (user == null) {
	    return false;
	}
	if (!user.hasPermission(action)) {
	    return false;
	}

	return true;
    }
    
    public static synchronized void authenticate(
	    ServiceRequest request, String service, ServiceResponse response) throws Exception {
	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

	MerchantEntry agent = null;

	
	try {

	    agent = getAgent(request);

	    if (agent == null) {
		agent = MerchantEntryImpl.getMerchant(request.getAgentId(),
			request.getcpId());

		cMap.put(agent.getMerchantId(), agent);
	    }

	    if (!agent.getPermisstion().contains(service.toLowerCase())) {
		throw new AppException(ErrorCode.SVC_NOT_HAS_PERMISSION);
	    }

	    if (agent.getConnectionCounter() < agent.getMaxConnection()) {
		agent.increement();
	    } else {
		throw new AppException(ErrorCode.SVC_CONNECTION_LIMIT);
	    }

	    if (agent.getTpsCounter() > agent.getMaxTps()) {
		throw new AppException(ErrorCode.SVC_TPS_LIMIT);
	    }

	    String password = NonceGenerator.getInstance().getNonce(
		    request.getcpId() + agent.getPassword()
			    + request.getrequestDate());

	    if (request.getcpId() != agent.getMerchantId()
		    || !request.getPassword().equals(password)) {
		throw new AppException(
			ErrorCode.SVC_ACCESS_AUTHENTICATION_ERROR);
	    }

	} catch (Exception e) {
	    response.setResult(e.toString());
	    response.setResultDescription(ErrorCode.getErrorDetail(e.toString()));
	} finally {
	    if(response.getResult() == null)
	    {
		response.setResult(ErrorCode.SVC_SUCCESS);
	    }
	    if (agent != null) {
		agent.decreement();
		cMap.replace(request.getcpId(), agent);
	    }
	}

    }

    protected String getCommand(String service) {
	return WSConfiguration.getConfiguration().getCommand(service);
    }

    protected String getCommand(String service, String keyword) {
	return WSConfiguration.getConfiguration().getCommand(service, keyword);
    }

    protected String getShortCode(String service) {
	return WSConfiguration.getConfiguration().getShortCode(service);
    }

    protected Properties getProperties(String service) {
	return WSConfiguration.getConfiguration().getProperties(service);
    }
    
    protected String getTimeout() {
	return WSConfiguration.getConfiguration().getTimeout();
    }
}
