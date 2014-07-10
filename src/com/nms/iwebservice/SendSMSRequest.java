/**
 * 
 */
package com.nms.iwebservice;

/**
 * @author Hung
 * 
 */
public class SendSMSRequest extends ServiceRequest {
    String shortCode = "";
    String message = "";

    public String getShortCode() {
	return shortCode;
    }

    public void setShortCode(String shortCode) {
	this.shortCode = shortCode;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

}
