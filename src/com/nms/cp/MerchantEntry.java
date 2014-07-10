/**
 * 
 */
package com.nms.cp;

import java.util.Calendar;

import com.crm.util.StringUtil;

/**
 * @author Hung
 * 
 */
public class MerchantEntry {
    public final static int STATUS_ACTIVE = 0;
    public final static int STATUS_DENIED = 1;

    public final static int AUTHEN_BY_PORTAL = 0;
    public final static int AUTHEN_BY_LOCAL = 1;
    public final static int AUTHEN_BY_SESSION = 2;

    private long agentId = 0;
    private long merchantId = 0;
    private String code = "";

    // private String isdn;
    private String serviceAddress = "";

    private int maxTps = 0;
    private int tpsTime = 0;
    private int maxConnection = 0;
    private int maxAmountPerDay = 0;
    private String startIP = "0";
    private String endIP = "0";
    private String ipauthorize;

    private int timeout = 30000;

    private String screenName = "";
    private String password = "";
    private int authenMode = AUTHEN_BY_PORTAL;
    private int status;
    private String permisstion = "";
    private int subscriptionRetry = 0;

    private int connectionCounter = 0;
    private int tpsCounter = 0;
    private long tpsTimer = Calendar.getInstance().getTimeInMillis();

    public String toString() {
	return StringUtil.toDebugString(this);
    }

    public long getAgentId() {
	return agentId;
    }

    public void setAgentId(long agentId) {
	this.agentId = agentId;
    }

    public long getMerchantId() {
	return merchantId;
    }

    public void setMerchantId(long merchantId) {
	this.merchantId = merchantId;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getServiceAddress() {
	return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
	this.serviceAddress = serviceAddress;
    }

    public int getMaxTps() {
	return maxTps;
    }

    public void setMaxTps(int maxTps) {
	this.maxTps = maxTps;
    }

    public int getTpsTime() {
	return tpsTime;
    }

    public void setTpsTime(int tpsTime) {
	this.tpsTime = tpsTime;
    }

    public int getMaxConnection() {
	return maxConnection;
    }

    public void setMaxConnection(int maxConnection) {
	this.maxConnection = maxConnection;
    }

    public int getMaxAmountPerDay() {
	return maxAmountPerDay;
    }

    public void setMaxAmountPerDay(int maxAmountPerDay) {
	this.maxAmountPerDay = maxAmountPerDay;
    }

    public String getStartIP() {
	return startIP;
    }

    public void setStartIP(String startIP) {
	this.startIP = startIP;
    }

    public String getEndIP() {
	return endIP;
    }

    public void setEndIP(String endIP) {
	this.endIP = endIP;
    }

    public String getIpauthorize() {
	return ipauthorize;
    }

    public void setIpauthorize(String ipauthorize) {
	this.ipauthorize = ipauthorize;
    }

    public int getTimeout() {
	return timeout;
    }

    public void setTimeout(int timeout) {
	this.timeout = timeout;
    }

    public String getScreenName() {
	return screenName;
    }

    public void setScreenName(String screenName) {
	this.screenName = screenName;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public int getAuthenMode() {
	return authenMode;
    }

    public void setAuthenMode(int authenMode) {
	this.authenMode = authenMode;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public int getConnectionCounter() {
	return connectionCounter;
    }

    public void setConnectionCounter(int connectionCounter) {
	this.connectionCounter = connectionCounter;
    }

    public int getTpsCounter() {
	return tpsCounter;
    }

    public void setTpsCounter(int tpsCounter) {
	this.tpsCounter = tpsCounter;
    }

    public long getTpsTimer() {
	return tpsTimer;
    }

    public void setTpsTimer(long tpsTimer) {
	this.tpsTimer = tpsTimer;
    }

    public String getPermisstion() {
	return permisstion.toLowerCase();
    }

    public void setPermisstion(String permisstion) {
	if (permisstion == null || "".equals(permisstion)) {
	    this.permisstion = "0";
	} else {
	    this.permisstion = permisstion;
	}

    }

    public int getSubscriptionRetry() {
	return subscriptionRetry;
    }

    public void setSubscriptionRetry(int subscriptionRetry) {
	this.subscriptionRetry = subscriptionRetry;
    }

    public synchronized void increement() {
	connectionCounter++;

	if ((System.currentTimeMillis() - tpsTimer) > 1000) {
	    tpsCounter = 1;
	} else {
	    tpsCounter++;
	}
    }

    public synchronized void decreement() {
	connectionCounter--;
    }

}
