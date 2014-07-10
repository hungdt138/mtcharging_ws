package com.crm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WSSecurity {
    Properties props = new java.util.Properties();
    public ArrayList<User> users = new ArrayList<User>();

    private static WSSecurity securiry = new WSSecurity();

    public static WSSecurity getSecuriry() {
	return securiry;
    }

    public static void setSecuriry(WSSecurity securiry) {
	WSSecurity.securiry = securiry;
    }

    public WSSecurity() {
	try {
	    int index = WSConfiguration.getConfiguration().getUserFilePath()
		    .lastIndexOf("/");

	    if (index > 0) {
		String directory = WSConfiguration.getConfiguration()
			.getUserFilePath().substring(0, index);
		File dir = new File(directory);
		if (!dir.exists()) {
		    dir.mkdirs();
		}
	    }

	    File f_con = new File(WSConfiguration.getConfiguration()
		    .getUserFilePath());
	    if (!f_con.exists()) {
		this.createUserXMLFile();
	    }

	    this.loadUsers();
	} catch (Exception e) {
	    WSConfiguration.debugMonitor("security: " + e);
	}
    }

    public void loadUsers() {
	try {
	    // if (WebServiceImpl._logger.isInfoEnabled())
	    // WebServiceImpl._logger.info("Loading Users List");
	    DocumentBuilderFactory factory = DocumentBuilderFactory
		    .newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    File file = new File(WSConfiguration.getConfiguration()
		    .getUserFilePath());
	    // File file = new File("D:/users.xml");
	    WSConfiguration.debugMonitor("Load users from "
		    + file.getAbsolutePath());
	    // System.out.println("User file path: " +
	    // WSConfiguration.getConfiguration().getUserFilePath());
	    FileInputStream fileStream = new FileInputStream(file);
	    Document doc = builder.parse(fileStream);

	    // System.out.println(WSConfiguration.getConfiguration().getUserFilePath());
	    Element root = doc.getDocumentElement();
	    NodeList children = root.getChildNodes();

	    for (int i = 0; i < children.getLength(); i++) {
		Node child = children.item(i);
		if (child instanceof Element) {
		    Element childElement = (Element) child;
		    // Text textNode = (Text) childElement.getFirstChild();

		    User user = new User();
		    user.setData(childElement);
		    users.add(user);
		    WSConfiguration.debugMonitor("loadUsers: "
			    + user.getUserName() + ":" + user.getPassword()
			    + ":" + user.Permissions.size());
		}
	    }
	    WSConfiguration.debugMonitor("Load users success.");
	} catch (ParserConfigurationException pce) {
	    WSConfiguration.debugMonitor("loadUsers: " + pce.toString());
	} catch (SAXException se) {
	    WSConfiguration.debugMonitor("loadUsers: " + se);
	} catch (IOException ioe) {
	    WSConfiguration.debugMonitor("loadUsers: " + ioe);
	} catch (Exception e) {
	    WSConfiguration.debugMonitor("loadUsers: " + e);
	}

    }

    public User authenticate(String username, String password) {
	for (int i = 0; i < this.users.size(); i++) {
	    User user = (User) this.users.get(i);
	    if (user.getUserName().equalsIgnoreCase(username)
		    && user.getPassword().equalsIgnoreCase(password)) {
		return user;
	    }
	}
	return null;

    }

    public void createUserXMLFile() {
	try {

	    File file = new File(WSConfiguration.getConfiguration()
		    .getUserFilePath());

	    DocumentBuilderFactory factory = DocumentBuilderFactory
		    .newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.newDocument();
	    Element rootElement = doc.createElement("users");
	    doc.appendChild(rootElement);

	    Element userElement = doc.createElement("user");
	    Element userNameElement = doc.createElement("username");
	    userNameElement.setTextContent("client1");// .setNodeValue("client1");
	    Element passwordElement = doc.createElement("password");
	    passwordElement.setTextContent("client1"); // .setNodeValue("client1");
	    Element permissionsElement = doc.createElement("permissions");

	    Element DeductBalanceElement = doc.createElement("permission");
	    DeductBalanceElement.setTextContent("DeductBalance");
	    Element ChangeExpiredDateElement = doc.createElement("permission");
	    ChangeExpiredDateElement.setTextContent("ChangeExpiredDate");
	    Element RetrieveSubscriberElement = doc.createElement("permission");
	    RetrieveSubscriberElement.setTextContent("RetrieveSubscriber");
	    Element GroupQuitElement = doc.createElement("permission");
	    GroupQuitElement.setTextContent("GroupQuit");
	    Element GroupRemoveElement = doc.createElement("permission");
	    GroupRemoveElement.setTextContent("GroupRemove");
	    Element GroupDeleteElement = doc.createElement("permission");
	    GroupDeleteElement.setTextContent("GroupDelete");
	    Element GroupListMemberElement = doc.createElement("permission");
	    GroupListMemberElement.setTextContent("GroupListMember");
	    Element AddBalanceElement = doc.createElement("permission");
	    AddBalanceElement.setTextContent("AddBalance");
	    Element SetBalanceElement = doc.createElement("permission");
	    SetBalanceElement.setTextContent("SetBalance");
	    Element ExtDebitElement = doc.createElement("permission");
	    ExtDebitElement.setTextContent("ExtDebit");
	    Element VasQueryElement = doc.createElement("permission");
	    VasQueryElement.setTextContent("VasQuery");
	    Element ChangeStateElement = doc.createElement("permission");
	    ChangeStateElement.setTextContent("ChangeState");
	    Element VasOnElement = doc.createElement("permission");
	    VasOnElement.setTextContent("VasOn");
	    Element CallHistoryQueryElement = doc.createElement("permission");
	    CallHistoryQueryElement.setTextContent("CallHistoryQuery");
	    Element VasOffElement = doc.createElement("permission");
	    VasOffElement.setTextContent("VasOff");
	    Element GroupCreateElement = doc.createElement("permission");
	    GroupCreateElement.setTextContent("GroupCreate");
	    Element GroupInviteElement = doc.createElement("permission");
	    GroupInviteElement.setTextContent("GroupInvite");
	    Element ActiveMaxi24Element = doc.createElement("permission");
	    ActiveMaxi24Element.setTextContent("ActiveMaxi24");
	    Element ActiveMaxi24UElement = doc.createElement("permission");
	    ActiveMaxi24UElement.setTextContent("ActiveMaxi24U");
	    Element ExecuteServiceElement = doc.createElement("permission");
	    ExecuteServiceElement.setTextContent("ExecuteService");

	    permissionsElement.appendChild(DeductBalanceElement);
	    permissionsElement.appendChild(ChangeExpiredDateElement);
	    permissionsElement.appendChild(RetrieveSubscriberElement);
	    permissionsElement.appendChild(GroupQuitElement);
	    permissionsElement.appendChild(GroupRemoveElement);
	    permissionsElement.appendChild(GroupDeleteElement);
	    permissionsElement.appendChild(GroupListMemberElement);
	    permissionsElement.appendChild(AddBalanceElement);
	    permissionsElement.appendChild(ExtDebitElement);
	    permissionsElement.appendChild(VasQueryElement);
	    permissionsElement.appendChild(ChangeStateElement);
	    permissionsElement.appendChild(VasOnElement);
	    permissionsElement.appendChild(CallHistoryQueryElement);
	    permissionsElement.appendChild(VasOffElement);
	    permissionsElement.appendChild(GroupCreateElement);
	    permissionsElement.appendChild(GroupInviteElement);
	    permissionsElement.appendChild(ActiveMaxi24Element);
	    permissionsElement.appendChild(ActiveMaxi24UElement);
	    permissionsElement.appendChild(ExecuteServiceElement);
	    permissionsElement.appendChild(SetBalanceElement);

	    userElement.appendChild(userNameElement);
	    userElement.appendChild(passwordElement);
	    userElement.appendChild(permissionsElement);

	    rootElement.appendChild(userElement);

	    Transformer t = TransformerFactory.newInstance().newTransformer();
	    // set output properties to get a DOCTYPE node
	    // t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
	    // "http://www.w3.org/TR/2000/CR-SVG-20000802/DTD/svg-20000802.dtd");
	    t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
		    "-//W3C//DTD SVG 20000802//EN");
	    // apply the "do nothing" transformation and send the output to a
	    // file
	    t.transform(new DOMSource(doc), new StreamResult(
		    new FileOutputStream(file)));

	} catch (Exception ex) {
	    Exception e = new Exception("Can not create Users XML file:"
		    + ex.getMessage(), ex);
	    WSConfiguration.debugMonitor(e);
	}

    }

    public static void main(String[] args) {
	WSSecurity s = new WSSecurity();
	s.authenticate("client1", "client1");
    }
}
