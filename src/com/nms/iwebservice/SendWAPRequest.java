/**
 * 
 */
package com.nms.iwebservice;

/**
 * @author Hung
 * 
 */
public class SendWAPRequest extends ServiceRequest {
    String shortCode = "";
    String targetURL = "";
    String subject = "";


    public String getShortCode() {
	return shortCode;
    }

    public void setShortCode(String shortCode) {
	this.shortCode = shortCode;
    }

    public String getTargetURL() {
	return targetURL;
    }

    public void setTargetURL(String targetURL) {
	this.targetURL = targetURL;
    }

    public String getSubject() {
	return subject;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

}
