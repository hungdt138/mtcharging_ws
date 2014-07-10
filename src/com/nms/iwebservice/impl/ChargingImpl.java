/**
 * 
 */
package com.nms.iwebservice.impl;

import java.text.SimpleDateFormat;

import sun.management.Sensor;

import com.crm.kernel.message.Constants;
import com.crm.provisioning.message.CommandMessage;
import com.nms.cp.MerchantEntry;
import com.nms.cp.MerchantEntryImpl;
import com.nms.cp.ServicesImpl;
import com.nms.iwebservice.Authentication;
import com.nms.iwebservice.ChargingWebserviceBase;
import com.nms.iwebservice.ErrorCode;
import com.nms.iwebservice.SendSMSRequest;
import com.nms.iwebservice.SendSMSResponse;
import com.nms.iwebservice.SendWAPRequest;
import com.nms.iwebservice.SendWAPResponse;
import com.nms.iwebservice.ServiceRequest;
import com.nms.iwebservice.ServiceResponse;
import com.nms.iwebservice.ServiceStatus;
import com.nms.iwebservice.SubscriptionReq;
import com.nms.iwebservice.SubscriptionResp;
import com.nms.iwebservice.SyncSubscriberReq;
import com.nms.iwebservice.SyncSubscriberResp;

/**
 * @author Hung
 * 
 */
public class ChargingImpl extends ChargingWebserviceBase {

    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public ServiceStatus getStatus(ServiceRequest req) throws Exception {

	String service = "getStatus";
	String sessionId = getSessionId(true);
	log.info("[GetStatus][REQ]: sessionId = " + sessionId + " | "
		+ req.toString());

	ServiceStatus resp = new ServiceStatus();

	try {
	    authenticate(req, service, resp);
	    if (!resp.getResult().equals(ErrorCode.SVC_SUCCESS)) {
		return resp;
	    }

	    resp = ServicesImpl.getStatus(req);

	} catch (Exception e) {
	    throw e;
	} finally {
	    log.info("RESP: sessionId = " + sessionId + " | " + resp.toString());
	}

	return resp;
    }

    @Override
    public SendSMSResponse sendSMS(SendSMSRequest req) throws Exception {

	String service = "sendSMS";
	String sessionId = getSessionId(true);
	log.info("[sendSMS][REQ]: sessionId = " + sessionId + " | "
		+ req.toString());
	SendSMSResponse resp = new SendSMSResponse();
	try {
	    authenticate(req, service, resp);

	    if (!resp.getResult().equals(ErrorCode.SVC_SUCCESS)) {
		return resp;
	    }

	    MerchantEntry merchant = getAgent(req);

	    if (!merchant.getServiceAddress().equals(req.getShortCode())) {
		resp.setResult(ErrorCode.SVC_INVALID_SHORTCODE);
		resp.setResultDescription(ErrorCode
			.getErrorDetail(ErrorCode.SVC_INVALID_SHORTCODE));
		return resp;
	    }

	    // check sub active or not

	    if (!ServicesImpl.getActive(req.getIsdn(), req.getProduct())) {
		resp.setResult(ErrorCode.SVC_SUBSCRIBER_NOT_ACTIVE);
		resp.setResultDescription(ErrorCode
			.getErrorDetail(ErrorCode.SVC_SUBSCRIBER_NOT_ACTIVE));
		return resp;
	    }

	    CommandMessage message = new CommandMessage();

	    message.setCorrelationID(sessionId);
	    message.setChannel(Constants.CHANNEL_WEB);
	    message.setServiceAddress(req.getShortCode());
	    message.setKeyword(Constants.FREFIX_DELIVERY + req.getProduct());
	    message.setIsdn(req.getIsdn());
	    message.setOrderDate(df.parse(req.getrequestDate()));
	    message.setMerchantId(req.getcpId());
	    message.setAgentId(req.getAgentId());
	    message.setResponseValue("smsType", 0);
	    message.setContent(req.getMessage());

	    CommandMessage result = sendOrder(message, req);

	    log.info(result.toString());

	    String error = ErrorCode.getErrorByCause(result.getCause());
	    resp.setResult(error);
	    resp.setResultDescription(ErrorCode.getErrorDetail(error));

	} catch (Exception e) {
	    throw e;
	} finally {
	    log.info("[sendSMS][RESP]: sessionId = " + sessionId + " | "
		    + resp.toString());
	}

	return resp;
    }

    @Override
    public SendWAPResponse sendWAP(SendWAPRequest req) throws Exception {

	String service = "sendWAP";
	String sessionId = getSessionId(true);
	log.info("[sendWAP][REQ]: sessionId = " + sessionId + " | "
		+ req.toString());
	SendWAPResponse resp = new SendWAPResponse();
	try {
	    authenticate(req, service, resp);

	    if (!resp.getResult().equals(ErrorCode.SVC_SUCCESS)) {
		return resp;
	    }

	    MerchantEntry merchant = getAgent(req);

	    if (!merchant.getServiceAddress().equals(req.getShortCode())) {
		resp.setResult(ErrorCode.SVC_INVALID_SHORTCODE);
		resp.setResultDescription(ErrorCode
			.getErrorDetail(ErrorCode.SVC_INVALID_SHORTCODE));
		return resp;
	    }

	    // check sub active or not

	    if (!ServicesImpl.getActive(req.getIsdn(), req.getProduct())) {
		resp.setResult(ErrorCode.SVC_SUBSCRIBER_NOT_ACTIVE);
		resp.setResultDescription(ErrorCode
			.getErrorDetail(ErrorCode.SVC_SUBSCRIBER_NOT_ACTIVE));
		return resp;
	    }

	    CommandMessage message = new CommandMessage();

	    message.setCorrelationID(sessionId);
	    message.setChannel(Constants.CHANNEL_WEB);
	    message.setServiceAddress(req.getShortCode());
	    message.setKeyword(Constants.FREFIX_DELIVERY + req.getProduct());
	    message.setIsdn(req.getIsdn());
	    message.setOrderDate(df.parse(req.getrequestDate()));
	    message.setMerchantId(req.getcpId());
	    message.setAgentId(req.getAgentId());
	    message.setResponseValue("smsType", 1);
	    message.setDeliveryWapTitle(req.getSubject());
	    message.setDeliveryWapHref(req.getTargetURL());

	    CommandMessage result = sendOrder(message, req);

	    log.info(result.toString());

	    String error = ErrorCode.getErrorByCause(result.getCause());
	    resp.setResult(error);
	    resp.setResultDescription(ErrorCode.getErrorDetail(error));

	} catch (Exception e) {
	    e.printStackTrace();
	    throw e;
	} finally {
	    log.info("[sendWAP][RESP]: sessionId = " + sessionId + " | "
		    + resp.toString());
	}

	return resp;
    }

    @Override
    public SubscriptionResp subscription(SubscriptionReq req) throws Exception {

	String service = "subscription";
	String sessionId = getSessionId(true);
	log.info("[subscription][REQ]: sessionId = " + sessionId + " | "
		+ req.toString());
	SubscriptionResp resp = new SubscriptionResp();
	try {
	    authenticate(req, service, resp);

	    if (!resp.getResult().equals(ErrorCode.SVC_SUCCESS)) {
		return resp;
	    }

	    MerchantEntry merchant = getAgent(req);

	    if (!ServicesImpl.checkRetry(req.getIsdn(), req.getProduct(),
		    merchant.getSubscriptionRetry())) {
		resp.setResult(ErrorCode.SVC_SUBSCRIPTION_LIMIT);
		resp.setResultDescription(ErrorCode
			.getErrorDetail(ErrorCode.SVC_SUBSCRIPTION_LIMIT));
		return resp;
	    }

	    // check sub active or not

//	     if (!ServicesImpl.getActive(req.getIsdn(), req.getProduct())) {
//	     resp.setResult(ErrorCode.SVC_SUBSCRIBER_NOT_ACTIVE);
//	     resp.setResultDescription(ErrorCode
//	     .getErrorDetail(ErrorCode.SVC_SUBSCRIBER_NOT_ACTIVE));
//	     return resp;
//	     }

	    CommandMessage message = new CommandMessage();

	    message.setCorrelationID(sessionId);
	    message.setChannel(Constants.CHANNEL_WEB);
	    message.setServiceAddress(merchant.getServiceAddress());
	    message.setKeyword(Constants.PREFIX_SUBSCRIPTION + req.getProduct());
	    message.setIsdn(req.getIsdn());
	    message.setOrderDate(df.parse(req.getrequestDate()));
	    message.setMerchantId(req.getcpId());
	    message.setAgentId(req.getAgentId());

	    CommandMessage result = sendOrder(message, req);

	    log.info(result.toString());

	    String error = ErrorCode.getErrorByCause(result.getCause());
	    resp.setResult(error);
	    resp.setResultDescription(ErrorCode.getErrorDetail(error));
	    resp.setAmount(result.getAmount());

	} catch (Exception e) {
	    throw e;
	} finally {
	    log.info("[subscription][RESP]: sessionId = " + sessionId + " | "
		    + resp.toString());
	}

	return resp;
    }

    @Override
    public SyncSubscriberResp syncSubscriber(SyncSubscriberReq req)
	    throws Exception {

	return null;
    }

//    public static void main(String[] args) {
//	// ServiceStatus status = new ServiceStatus();
//	// ServiceRequest request = new ServiceRequest();
//	// request.setAgentId(54020);
//	// request.setcpId(3022);
//	// request.setDescription("Get Active");
//	// request.setIsdn("84923962323");
//	// request.setPassword("B5CAD3AED615D01DA8612AC6175910F1");
//	// request.setProduct("ACOM_IQ");
//	// request.setrequestDate("20140312141300");
//	// request.setrequestId(123);
//	//
//	// ChargingImpl impl = new ChargingImpl();
//	// try {
//	// status = impl.getStatus(request);
//	// System.out.println("Result: " + status.getResult());
//	// System.out.println("Description: " + status.getResultDescription());
//	// } catch (Exception e) {
//	// // TODO Auto-generated catch block
//	// e.printStackTrace();
//	// }
//
//	 SendSMSRequest req = new SendSMSRequest();
//	 req.setAgentId(54019);
//	 req.setcpId(3020);
//	 req.setDescription("Test");
//	 req.setIsdn("841869023701");
//	 req.setMessage("aaaa");
//	 req.setPassword("850ba783fcdff6199e1b10dcc3a0f65e");
//	 req.setProduct("GAPIT_COOL");
//	 req.setrequestDate("20140321112300");
//	 req.setrequestId(1223214032);
//	 req.setShortCode("8969");
//	
//	 ChargingImpl impl = new ChargingImpl();
//	 SendSMSResponse resp = new SendSMSResponse();
//	 try {
//	 resp = impl.sendSMS(req);
//	 System.out.println("Result: " + resp.getResult());
//	 System.out.println("Description: " + resp.getResultDescription());
//	 } catch (Exception e) {
//	 // TODO Auto-generated catch block
//	 e.printStackTrace();
//	 }
//
//	// SendWAPRequest req = new SendWAPRequest();
//	// req.setAgentId(54020);
//	// req.setcpId(3022);
//	// req.setDescription("Test");
//	// req.setIsdn("84923962323");
//	// req.setSubject("aaaa");
//	// req.setTargetURL("http://203.128.246.91:8088/mtcharging/services/mtcharging_ws?wsdl");
//	// req.setPassword("B5CAD3AED615D01DA8612AC6175910F1");
//	// req.setProduct("ACOM_IQ");
//	// req.setrequestDate("20140312141300");
//	// req.setrequestId(1234);
//	// req.setShortCode("8926");
//	//
//	// ChargingImpl impl = new ChargingImpl();
//	// SendWAPResponse resp = new SendWAPResponse();
//	// try {
//	// resp = impl.sendWAP(req);
//	// System.out.println("Result: " + resp.getResult());
//	// System.out.println("Description: " + resp.getResultDescription());
//	// } catch (Exception e) {
//	// // TODO Auto-generated catch block
//	// e.printStackTrace();
//	// }
//    }

}
