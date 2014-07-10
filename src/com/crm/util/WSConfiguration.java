package com.crm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * @author Nam<br>
 *         Last Modified Date: 06/07/2012
 * 
 */
public class WSConfiguration extends AppProperties {
    /**
	 * 
	 */
    private static final long serialVersionUID = 10616673571566833L;

    private static Logger log = Logger.getLogger(WSConfiguration.class);
    private static WSConfiguration configuration = null;

    private Map<String, Properties> propertiesMap = new HashMap<String, Properties>();

    public static String rootPath = System.getProperty("catalina.base");
    public static String configPath = rootPath + "/config/mtchargingws/";

    public static WSConfiguration getConfiguration() {
	if (configuration == null) {
	    configuration = new WSConfiguration();
	}

	return configuration;
    }

    public static void debugMonitor(Object e) {
	if (e instanceof Exception) {
	    log.error(e, (Exception) e);
	} else {
	    log.info(e);
	}
    }

    public WSConfiguration() {
	super();

	InputStream configStream = null;
	File file = null;

	try {
	    file = new File(configPath + "ServiceConfig.txt");
	    configStream = new FileInputStream(file);

	    load(configStream);

	    debugMonitor("Load configuration success. (" + configPath
		    + "ServiceConfig.txt)");
	} catch (Exception e) {
	    debugMonitor("Load ServiceConfig.txt error");
	    debugMonitor(e);
	} finally {
	    try {
		configStream.close();
	    } catch (IOException ioe) {
	    }
	}
    }

    public String getUserFilePath() {
	return getProperty("root.userFile", "");
    }

    public String getChargingAccount() {
	return getProperty("root.charging.account", "");
    }

    public String getCommand(String service) {
	return getProperty(service + ".command", "");
    }

    public String getCommand(String service, String keyword) {
	return getProperty(service + ".command." + keyword, "");
    }
    
    public String getTimeout() {
	return getProperty("timeout", "");
    }

    public String getShortCode(String service) {
	return getProperty(service + ".shortCode", "");
    }

    public Properties getProperties(String service) {
	Properties props = propertiesMap.get(service);

	if (props == null) {
	    props = loadProperties(service);
	    propertiesMap.put(service, props);
	}

	if (props == null) {
	    props = new Properties();
	}

	return props;
    }

    private Properties loadProperties(String service) {
	Properties props = new Properties();

	String list = getProperty(service + ".properties", "");

	if (!list.equals("")) {
	    ArrayList<String> properties = StringUtil.toList(list,
		    StringPool.COMMA);
	    for (String property : properties) {
		props.setProperty(
			property.trim(),
			getProperty(service + ".properties." + property.trim(),
				""));
	    }
	}

	return props;
    }

    @Override
    public String getProperty(String key, String defaultValue) {
	String value = super.getProperty(key, defaultValue);
	if (value.equals(""))
	    value = defaultValue;
	return value;
    }

    public static void main(String[] params) throws ParseException {
	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	long n = df.parse("20100101000101001").getTime();
	long l = (new Date()).getTime();
	l = l - n;
	System.out.println(l);
	String strhex = Long.toHexString(l).toUpperCase();
	System.out.println(strhex);
    }
}
