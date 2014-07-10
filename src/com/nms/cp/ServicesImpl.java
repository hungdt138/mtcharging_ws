/**
 * 
 */
package com.nms.cp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import com.crm.kernel.message.Constants;
import com.crm.kernel.sql.Database;
import com.nms.iwebservice.ErrorCode;
import com.nms.iwebservice.ServiceRequest;
import com.nms.iwebservice.ServiceStatus;

/**
 * @author Hung
 * 
 */
public class ServicesImpl {

    public final static String CONDITION_ACTIVE = " (supplierStatus = "
	    + Constants.SUPPLIER_ACTIVE_STATUS + ") ";

    public final static String CONDITION_BARRING = " (supplierStatus = "
	    + Constants.SUPPLIER_BARRING_STATUS + ") ";

    public final static String CONDITION_TERMINATED = " (supplierStatus = "
	    + Constants.SUPPLIER_CANCEL_STATUS + ") ";

    public final static String CONDITION_UNTERMINATED = " (supplierStatus != "
	    + Constants.SUPPLIER_CANCEL_STATUS + ") ";

    public static ServiceStatus getStatus(ServiceRequest request)
	    throws Exception {

	ServiceStatus response = new ServiceStatus();

	Connection connection = null;

	PreparedStatement stmtSubscription = null;

	ResultSet rsSubscription = null;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	try {

	    connection = Database.getConnection();

	    String sql = "Select A.* From SubscriberProduct A, ProductEntry B "
		    + "Where A.productId = B.productId and isdn = ? and B.code = ? order by A.registerDate desc";

	    stmtSubscription = connection.prepareStatement(sql);

	    stmtSubscription.setString(1, request.getIsdn());
	    stmtSubscription.setString(2, request.getProduct());

	    rsSubscription = stmtSubscription.executeQuery();

	    if (rsSubscription.next()) {
		response.setStatus(rsSubscription.getInt("supplierStatus"));
		response.setRegisterDate(rsSubscription
			.getTimestamp("registerDate"));
		response.setExpirationDate(rsSubscription
			.getTimestamp("expirationDate"));
	    }
	} catch (Exception e) {
	    throw e;
	} finally {
	    Database.closeObject(rsSubscription);
	    Database.closeObject(stmtSubscription);
	    Database.closeObject(connection);
	    response.setResult(ErrorCode.SVC_SUCCESS);
	    response.setResultDescription(ErrorCode
		    .getErrorDetail(ErrorCode.SVC_SUCCESS));
	}

	return response;
    }

    public static boolean getActive(String isdn, String productCode)
	    throws Exception {
	PreparedStatement stmtActive = null;
	ResultSet rsActive = null;
	Connection connection = null;
	try {
	    connection = Database.getConnection();
	    String SQL = "Select * "
		    + "From SubscriberProduct "
		    + "Where isdn = ? and productId = (select productId from productEntry where code = ?) and "
		    + CONDITION_ACTIVE + "Order by registerDate desc";

	    stmtActive = connection.prepareStatement(SQL);
	    stmtActive.setString(1, isdn);
	    stmtActive.setString(2, productCode);

	    rsActive = stmtActive.executeQuery();

	    if (rsActive.next()) {
		return true;
	    }
	} catch (Exception e) {
	    throw e;
	} finally {
	    Database.closeObject(connection);
	    Database.closeObject(rsActive);
	    Database.closeObject(stmtActive);
	}

	return false;
    }

    public static boolean checkRetry(String isdn, String productCode, int retryNum)
	    throws Exception {
	PreparedStatement stmtActive = null;
	ResultSet rsActive = null;
	Connection connection = null;
	int count = 0;
	try {
	    connection = Database.getConnection();
	    String SQL = "Select * "
		    + "From SubscriberProduct "
		    + "Where isdn = ? and productId = (select productId from productEntry where code = ?) and "
		    + CONDITION_ACTIVE + "Order by registerDate desc";

	    stmtActive = connection.prepareStatement(SQL);
	    stmtActive.setString(1, isdn);
	    stmtActive.setString(2, productCode);

	    rsActive = stmtActive.executeQuery();

	    if (rsActive.next()) {
		count = rsActive.getInt("retry");
	    }
	    if(count <= retryNum)
	    {
		return true;
	    }
	} catch (Exception e) {
	    throw e;
	} finally {
	    Database.closeObject(connection);
	    Database.closeObject(rsActive);
	    Database.closeObject(stmtActive);
	}

	return false;
    }
}
