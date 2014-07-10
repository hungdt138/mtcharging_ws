/**
 * 
 */
package com.nms.iwebservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.xfire.transport.http.XFireServletController;

import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.crm.util.NonceGenerator;
import com.fss.util.AppException;

import com.nms.cp.MerchantEntry;
import com.nms.cp.MerchantEntryImpl;

/**
 * @author Hung
 * 
 */
public class Authentication {

    private static long DEFAULT_ID = 0;

    private static Logger logger = Logger.getLogger(Authentication.class);

    protected static ConcurrentHashMap<Long, MerchantEntry> cMap = new ConcurrentHashMap<Long, MerchantEntry>();

    protected static void debugMonitor(Object e) {
	logger.debug(e);
    }

    protected static void infoMonitor(Object e) {
	logger.info(e);
    }

    public static MerchantEntry getAgent(ServiceRequest request) {
	return cMap.get(request.getcpId());
    }

    // not use
    public static void checkIP(String s, String startIP, String endIP)
	    throws AppException {

	if (startIP.equals("0") || endIP.equals("0")) {

	} else {
	    String s3 = s.substring(0, s.lastIndexOf("."));
	    String startIP3 = startIP.substring(0, startIP.lastIndexOf("."));
	    String endIP3 = endIP.substring(0, endIP.lastIndexOf("."));

	    String s4 = s.substring(s.lastIndexOf(".") + 1);
	    String startIP4 = startIP.substring(startIP.lastIndexOf(".") + 1);
	    String endIP4 = endIP.substring(endIP.lastIndexOf(".") + 1);

	    int is = Integer.parseInt(s4);
	    int istartIP = Integer.parseInt(startIP4);
	    int iendIP = Integer.parseInt(endIP4);

	    if (s3.equals(startIP3) && s3.equals(endIP3)) {
		if (is <= iendIP) {
		    if (is >= istartIP) {

		    }
		}

	    } else {
		throw new AppException(ErrorCode.SVC_IP_REJECT);
	    }
	}

    }

   
}
