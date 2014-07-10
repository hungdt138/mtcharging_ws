package com.crm.provisioning.impl.charging;

public class ChargingConstants
{
	/**
	 * SEPARATE_CHARS: end of each request
	 */
	public static final String	SEPARATE_CHARS								= "\r\n";			// CRLF
	public static final String	SEPARATOR_CMD								= " ";
	public static final String	SEPARATOR_FIELD								= "&";
	public static final String	SEPARATOR_REQUEST_CMD_FIELD					= "?";
	public static final String	SEPARATOR_RESPONSE_CMD_FIELD				= ":";

	public static final String	REQUEST										= "REQ";
	public static final String	RESPONSE									= "RESP";
	public static final String	INFO										= "INFO";

	public static final String	CMD_LOGON									= "Logon";
	public static final String	CMD_CREATE_SESSION							= "CreateSession";
	public static final String	CMD_HEART_BEAT								= "HeartBeat";
	public static final String	CMD_DEBIT									= "Debit";
	public static final String	CMD_DETROY_SESSION							= "DestroySession";

	public static final String	FIELD_SESSION_ID							= "SessionId=";
	public static final String	FIELD_MSG									= "Msg=";
	public static final String	FIELD_USER									= "UserName=";
	public static final String	FIELD_PASSWORD								= "Password=";
	public static final String	FIELD_ERROR_CODE							= "ErrCode=";
	public static final String	FIELD_ERROR_DETAIL							= "ErrDetail=";
	public static final String	FIELD_TRANSACTION_ID						= "TransId=";
	public static final String	FIELD_TRANSACTION_DATETIME					= "TransDateTime=";
	public static final String	FIELD_CP_ID									= "CpId=";
	public static final String	FIELD_CP_NAME								= "CpName=";
	public static final String	FIELD_NUMBER_A								= "ANumber=";
	public static final String	FIELD_NUMBER_B								= "BNumber=";
	public static final String	FIELD_SUBMIT_TIME							= "SubmitTime=";
	public static final String	FIELD_SENT_TIME								= "SentTime=";
	public static final String	FIELD_SERVICE_STATE							= "ServiceState=";
	public static final String	FIELD_CONTENT_CODE							= "ContCode=";
	public static final String	FIELD_CONTENT_TYPE							= "ContType=";
	public static final String	FIELD_DESCRIPTION							= "Description=";

	public static final String	VALUE_SERVICE_STATE_SUCCESS					= "D";
	public static final String	VALUE_SERVICE_STATE_FAILED					= "U";
	public static final String	VALUE_CONTENT_CODE_VOICE_MO					= "100";
	public static final String	VALUE_CONTENT_CODE_SMS_MO					= "102";
	public static final String	VALUE_CONTENT_CODE_SMS_MT					= "103";
	public static final String	VALUE_CONTENT_CODE_CALL_FORWARD_OR_DIVERT	= "104";
	public static final String	VALUE_CONTENT_CODE_MMS_MO					= "120";
	public static final String	VALUE_CONTENT_CODE_MMS_MT					= "121";
	public static final String	VALUE_CONTENT_CODE_MMS_FORWARDED			= "125";
	public static final String	VALUE_CONTENT_CODE_DATA						= "122";
	public static final String	VALUE_CONTENT_TYPE_DEFAULT					= "1";

	public static String getFieldValue(String name, String source)
	{
		String value = "";

		int fieldIndex = source.indexOf(name);

		if (fieldIndex >= 0)
		{
			value = source.substring(fieldIndex + name.length());

			int endIndex = value.indexOf(SEPARATOR_FIELD);

			if (endIndex >= 0)
			{
				value = value.substring(0, endIndex);
			}
		}

		return value;
	}
}
