/**
 * 
 */
package com.nms.iwebservice;

import com.crm.kernel.message.Constants;

/**
 * @author Hung
 * 
 */
public class ErrorCode {

    public static final String SVC_SUCCESS = "SVC0000";

    // system error
    public static final String SVC_ERROR = "SVC0001";

    // Invalid input value
    public static final String SVC_INVALID_INPUT_VALUE = "SVC0002";

    // There are too many address
    public static final String SVC_TOO_MANY_ADDRESS = "SVC0003";

    // not has permission

    public static final String SVC_NOT_HAS_PERMISSION = "SVC0004";

    // IP address not allowed
    public static final String SVC_IP_REJECT = "SVC0005";

    // The function of sending a message to a group is not supported
    public static final String SVC_GROUP = "SVC0006";

    // Transaction duplicated
    public static final String SVC_TRANSACTION_DUPLICATED = "SVC0007";

    // The concurrent connection limit
    public static final String SVC_CONNECTION_LIMIT = "SVC0008";

    // TPS limit
    public static final String SVC_TPS_LIMIT = "SVC0009";

    // invalid shortcode

    public static final String SVC_INVALID_SHORTCODE = "SVC0010";

    // Connection timeout
    public static final String SVC_TIMEOUT = "SVC0101";

    //
    public static final String SVC_REGISTERED = "SVC0102";

    //
    public static final String SVC_UNREGISTERED = "SVC0103";

    public static final String SVC_SUBSCRIPTION_LIMIT = "SVC0104";

    // The transaction does not exist
    public static final String SVC_TRANSACTION_NOT_EXIST = "SVC0200";

    // Subscriber does not exist
    public static final String SVC_SUBSCRIBER_NOT_EXITS = "SVC0201";

    // Subscriber not enought money
    public static final String SVC_SUBSCRIBER_NOT_ENOUGHT_MONEY = "SVC0202";

    // Invalid syntax
    public static final String SVC_INVALID_SYNTAX = "SVC0203";

    // Denied subscriber type
    public static final String SVC_DENIED_SUBSCRIBER_TYPE = "SVC0204";

    // Subscriber postpaid lock
    public static final String SVC_SUBSCRIBER_NOT_ACTIVE = "SVC0205";

    // Subscription over gracedate
    public static final String SVC_SUBSCRIPTION_OVER_GRACEDATE = "SVC0206";

    // Subscription reject
    public static final String SVC_SUBSCRIPTION_CANCEL = "SVC0207";

    public static final String SVC_SUBSCRIPTION_OVER = "SVC0208";

    // The lenght is exceeded
    public static final String SVC_LENGTH_EXCEEDED = "SVC0280";

    // The group-send function is not supported
    public static final String SVC_GROUP_SEND = "SVC0900";

    // Access authentication or authorization error
    public static final String SVC_ACCESS_AUTHENTICATION_ERROR = "SVC0901";

    // The message sending rate exceeds the limit
    public static final String SVC_MESSAGE_EXCEEDS = "SVC0904";

    // Parameter error
    public static final String SVC_PARAMETER_ERROR = "SVC0905";

    public static String getErrorDetail(String errorcode) {

	if (errorcode.equals(SVC_SUCCESS)) {
	    return "Success";
	} else if (errorcode.equals(SVC_ERROR)) {
	    return "Error";
	} else if (errorcode.equals(SVC_INVALID_INPUT_VALUE)) {
	    return "Invalid input value";
	} else if (errorcode.equals(SVC_LENGTH_EXCEEDED)) {
	    return "The lenght is exceeded";
	} else if (errorcode.equals(SVC_ACCESS_AUTHENTICATION_ERROR)) {
	    return "Access authentication or authorization error";
	} else if (errorcode.equals(SVC_PARAMETER_ERROR)) {
	    return "Parameter error";
	} else if (errorcode.equals(SVC_TIMEOUT)) {
	    return "Connection timeout";
	} else if (errorcode.equals(SVC_TOO_MANY_ADDRESS)) {
	    return "There are too many address";
	} else if (errorcode.equals(SVC_TPS_LIMIT)) {
	    return "TPS limit";
	} else if (errorcode.equals(SVC_GROUP)) {
	    return "The function of sending a message to a group is not supported";
	} else if (errorcode.equals(SVC_GROUP_SEND)) {
	    return "The group-send function is not supported";
	} else if (errorcode.equals(SVC_MESSAGE_EXCEEDS)) {
	    return "The message sending rate exceeds the limit";
	} else if (errorcode.equals(SVC_IP_REJECT)) {
	    return "IP address not allowed";
	} else if (errorcode.equals(SVC_CONNECTION_LIMIT)) {
	    return "The concurrent connection limit";
	} else if (errorcode.equals(SVC_TRANSACTION_NOT_EXIST)) {
	    return "The transaction does not exist";
	} else if (errorcode.equals(SVC_SUBSCRIBER_NOT_EXITS)) {
	    return "Subscriber does not exist";
	} else if (errorcode.equals(SVC_SUBSCRIBER_NOT_ENOUGHT_MONEY)) {
	    return "Subscriber not enought money";
	} else if (errorcode.equals(SVC_TRANSACTION_DUPLICATED)) {
	    return "Transaction duplicated";
	} else if (errorcode.equals(SVC_INVALID_SYNTAX)) {
	    return "Invalid syntax";
	} else if (errorcode.equals(SVC_DENIED_SUBSCRIBER_TYPE)) {
	    return "Denied subscriber type";
	} else if (errorcode.equals(SVC_SUBSCRIBER_NOT_ACTIVE)) {
	    return "Subscriber is not active or suspend";
	} else if (errorcode.equals(SVC_SUBSCRIPTION_OVER)) {
	    return "Subscription error";
	} else if (errorcode.equals(SVC_NOT_HAS_PERMISSION)) {
	    return "User not has permisstion";
	} else if (errorcode.equals(SVC_INVALID_SHORTCODE)) {
	    return "Invalid shortcode";
	} else if (errorcode.equals(SVC_SUBSCRIPTION_LIMIT)) {
	    return "Subscription limit";
	} else {
	    return "";
	}

    }

    public static String getErrorByCause(String cause) {
	if (Constants.ERROR_NOT_ENOUGH_MONEY.equals(cause)) {
	    return SVC_SUBSCRIBER_NOT_ENOUGHT_MONEY;
	} else if (Constants.ERROR_DUPLICATED.equals(cause)) {
	    return SVC_TRANSACTION_DUPLICATED;
	} else if (Constants.ERROR_TIMEOUT.equals(cause)) {
	    return SVC_TIMEOUT;
	} else if (Constants.SUCCESS.equals(cause)) {
	    return SVC_SUCCESS;
	} else if (Constants.ERROR.equals(cause)) {
	    return SVC_ERROR;
	} else if (Constants.ERROR_REGISTERED.equals(cause)) {
	    return SVC_REGISTERED;
	} else if (Constants.ERROR_UNREGISTERED.equals(cause)) {
	    return SVC_UNREGISTERED;
	} else if (Constants.ERROR_INVALID_SYNTAX.equals(cause)) {
	    return SVC_INVALID_SYNTAX;
	} else if (Constants.ERROR_DENIED_SUBSCRIBER_TYPE.equals(cause)) {
	    return SVC_DENIED_SUBSCRIBER_TYPE;
	} else if (Constants.ERROR_SUBSRIBER_NOT_ACTIVE.equals(cause)) {
	    return SVC_SUBSCRIBER_NOT_ACTIVE;
	} else if (Constants.ERROR_SUBSCRIPTION.equals(cause)) {
	    return SVC_SUBSCRIPTION_OVER_GRACEDATE;
	} else if (Constants.ERROR_SUBSCRIBER_CHANGE.equals(cause)) {
	    return SVC_SUBSCRIPTION_CANCEL;
	} else if (Constants.ERROR_INVALID_REQUEST.equals(cause)) {
	    return SVC_TRANSACTION_NOT_EXIST;
	} else if (Constants.ERROR_SUBSCRIPTION_OVER.equals(cause)) {
	    return SVC_SUBSCRIPTION_OVER;
	} else {
	    return SVC_ERROR;
	}
    }

}
