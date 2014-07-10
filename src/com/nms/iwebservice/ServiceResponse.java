package com.nms.iwebservice;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.crm.util.StringUtil;

public class ServiceResponse {

    private String result;
    private String resultDescription;

    public String getResult() {
	return result;
    }

    public void setResult(String result) {
	this.result = result;
    }

    public String getResultDescription() {
	return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
	this.resultDescription = resultDescription;
    }

    public String toString() {
	Class<? extends ServiceResponse> type = this.getClass();
	Method[] methods = type.getMethods();
	String returnString = "";
	for (int i = 0; i < methods.length; i++) {
	    if (!methods[i].getName().startsWith("get")) {
		continue;
	    }
	    String member = "";
	    try {
		member = methods[i].getName().substring(3) + "=";
		Object value = methods[i].invoke(this, new Object[] {});
		if (value instanceof Date || value instanceof Calendar) {
		    member += (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"))
			    .format(value);
		} else {
		    member += value.toString();
		}
		member += " | ";
	    } catch (Exception e) {
		member = "";
	    }
	    returnString += member;
	}
	return returnString.trim();
    }

}
