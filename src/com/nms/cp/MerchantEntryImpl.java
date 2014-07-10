/**
 * 
 */
package com.nms.cp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import com.crm.kernel.sql.Database;
import com.nms.iwebservice.ServiceRequest;
import com.nms.iwebservice.ServiceStatus;

/**
 * @author Hung
 * 
 */
public class MerchantEntryImpl {

    // get info CP request by agentId and cpId

    public static MerchantEntry getMerchant(long agentId, long cpId)
	    throws Exception {
	Connection connection = null;
	MerchantEntry merchant = null;
	PreparedStatement stmtMerchant = null;

	ResultSet rsMerchant = null;

	try {
	    String sql = "Select * From MerchantAgent A, MerchantEntry B Where A.merchantId = ? and A.agentId = ? and"
		    + " A.merchantId = B.merchantId";
	    connection = Database.getConnection();
	    stmtMerchant = connection.prepareStatement(sql);
	    stmtMerchant.setLong(1, cpId);
	    stmtMerchant.setLong(2, agentId);

	    rsMerchant = stmtMerchant.executeQuery();

	    if (rsMerchant.next()) {
		merchant = new MerchantEntry();
		merchant.setMerchantId(rsMerchant.getLong("merchantId"));
		merchant.setAgentId(rsMerchant.getLong("agentId"));
		merchant.setCode(rsMerchant.getString("code"));
		merchant.setServiceAddress(rsMerchant.getString("serviceAddress"));
		merchant.setScreenName(rsMerchant.getString("screenName"));
		merchant.setPassword(rsMerchant.getString("password_"));
		merchant.setStartIP(rsMerchant.getString("startIP"));
		merchant.setEndIP(rsMerchant.getString("endIP"));
		merchant.setMaxTps(rsMerchant.getInt("maxTPS"));
		merchant.setMaxConnection(rsMerchant.getInt("maxsession"));
		merchant.setPermisstion(rsMerchant.getString("permission"));
		merchant.setSubscriptionRetry(rsMerchant.getInt("subretry"));
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    throw e;
	} finally {
	    Database.closeObject(stmtMerchant);
	    Database.closeObject(connection);
	    Database.closeObject(rsMerchant);
	}

	return merchant;
    }

    
    
    

}
