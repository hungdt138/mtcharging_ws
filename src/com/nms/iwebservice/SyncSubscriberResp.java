/**
 * 
 */
package com.nms.iwebservice;

import java.text.ParseException;

/**
 * @author hungdt
 *
 */
public class SyncSubscriberResp extends ServiceResponse
{
	//sub or unsub
	private String action = "";

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}
	
	
	
}
