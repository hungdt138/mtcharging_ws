/**
 * 
 */
package com.nms.iwebservice;


/**
 * @author Hung
 * 
 */
public interface ChargingServices {
    public abstract ServiceStatus getStatus(ServiceRequest req)
	    throws Exception;

    public abstract SendSMSResponse sendSMS(SendSMSRequest req)
	    throws Exception;

    public abstract SendWAPResponse sendWAP(SendWAPRequest req)
	    throws Exception;

    public abstract SubscriptionResp subscription(SubscriptionReq req)
	    throws Exception;

    public abstract SyncSubscriberResp syncSubscriber(SyncSubscriberReq req)
	    throws Exception;

}
