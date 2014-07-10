/**
 * 
 */
package com.nms.iwebservice;

import java.text.ParseException;

/**
 * @author hungdt
 *
 */
public class SubscriptionResp extends ServiceResponse
{
	private double 	amount;
	
	public double getAmount()
	{
		return amount;
	}
	
	public void setAmount(double amount)
	{
		this.amount = amount;
	}
}
