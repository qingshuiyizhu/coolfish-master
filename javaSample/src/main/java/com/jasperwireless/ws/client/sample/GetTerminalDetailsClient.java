/**
 * Copyright 2005 Jasper Systems, Inc. All rights reserved.
 *
 * This software code is the confidential and proprietary information of
 * Jasper Systems, Inc. ("Confidential Information"). Any unauthorized
 * review, use, copy, disclosure or distribution of such Confidential
 * Information is strictly prohibited.
 */
package com.jasperwireless.ws.client.sample;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.soap.*;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import com.sun.xml.wss.ProcessingContext;
import com.sun.xml.wss.XWSSProcessor;
import com.sun.xml.wss.XWSSProcessorFactory;
import com.sun.xml.wss.XWSSecurityException;
import com.sun.xml.wss.impl.callback.PasswordCallback;
import com.sun.xml.wss.impl.callback.UsernameCallback;

/**
 * @author Sunil Sheshadri
 * @version $Id: //depot/jasper_dev/module/ProvisionApp/web/secure/apidoc/java/com/jasperwireless/ws/client/sample/GetTerminalDetailsClient.java#3 $
 */
public class GetTerminalDetailsClient implements ApiClientConstant {
    private SOAPConnectionFactory connectionFactory;
    private MessageFactory messageFactory;
    private URL url;
    private String licenseKey;

    private XWSSProcessorFactory processorFactory;

    /**
     * Constructor which initializes Soap Connection, messagefactory and ProcessorFactory
     *
     * @param url
     * @throws SOAPException
     * @throws MalformedURLException
     * @throws XWSSecurityException
     */
    public GetTerminalDetailsClient(String url, String licenseKey)
            throws SOAPException, MalformedURLException, XWSSecurityException {
        connectionFactory = SOAPConnectionFactory.newInstance();
        messageFactory = MessageFactory.newInstance();
        processorFactory = XWSSProcessorFactory.newInstance();
        this.url = new URL(url);
        this.licenseKey = licenseKey;
    }

    /**
     * This method creates a Terminal Request and sends back the SOAPMessage.
     * ICCID value is passed into this method
     *
     * @return SOAPMessage
     * @throws SOAPException
     */
    private SOAPMessage createTerminalRequest(String iccid) throws SOAPException {
        SOAPMessage message = messageFactory.createMessage();
        message.getMimeHeaders().addHeader("SOAPAction",
                "http://api.jasperwireless.com/ws/service/terminal/GetTerminalDetails");
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        Name terminalRequestName = envelope.createName("GetTerminalDetailsRequest", PREFIX, NAMESPACE_URI);
        SOAPBodyElement terminalRequestElement = message.getSOAPBody()
                .addBodyElement(terminalRequestName);
        Name msgId = envelope.createName("messageId", PREFIX, NAMESPACE_URI);
        SOAPElement msgElement = terminalRequestElement.addChildElement(msgId);
        msgElement.setValue("TCE-100-ABC-34084");
        Name version = envelope.createName("version", PREFIX, NAMESPACE_URI);
        SOAPElement versionElement = terminalRequestElement.addChildElement(version);
        versionElement.setValue("1.0");
        Name license = envelope.createName("licenseKey", PREFIX, NAMESPACE_URI);
        SOAPElement licenseElement = terminalRequestElement.addChildElement(license);
        licenseElement.setValue(licenseKey);
        Name iccids = envelope.createName("iccids", PREFIX, NAMESPACE_URI);
        SOAPElement iccidsElement = terminalRequestElement.addChildElement(iccids);
        Name iccidName = envelope.createName("iccid", PREFIX, NAMESPACE_URI);
        SOAPElement iccidElement = iccidsElement.addChildElement(iccidName);
        iccidElement.setValue(iccid);
        return message;
    }

    public void callWebService(String username, String password, String iccid) throws SOAPException, IOException, XWSSecurityException, Exception {
        SOAPMessage request = createTerminalRequest(iccid);
        request = secureMessage(request, username, password);
        System.out.println("Request: ");
        request.writeTo(System.out);
        System.out.println("");
        SOAPConnection connection = connectionFactory.createConnection();
        SOAPMessage response = connection.call(request, url);
        System.out.println("Response: ---");
        response.writeTo(System.out);
        System.out.println("-------------");
        if (!response.getSOAPBody().hasFault()) {
            writeTerminalResponse(response);
        } else {
            SOAPFault fault = response.getSOAPBody().getFault();
            System.err.println("Received SOAP Fault");
            System.err.println("SOAP Fault Code :" + fault.getFaultCode());
            System.err.println("SOAP Fault String :" + fault.getFaultString());
        }
    }

    /**
     * Gets the terminal response.
     *
     * @param message
     * @throws SOAPException
     */
    private void writeTerminalResponse(SOAPMessage message) throws SOAPException {
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        Name terminalResponseName = envelope.createName("GetTerminalDetailsResponse", PREFIX, NAMESPACE_URI);
        SOAPBodyElement terminalResponseElement = (SOAPBodyElement) message
                .getSOAPBody().getChildElements(terminalResponseName).next();
        String terminalValue = terminalResponseElement.getTextContent();
        Name terminals = envelope.createName("terminals", PREFIX, NAMESPACE_URI);
        Name terminal = envelope.createName("terminal", PREFIX, NAMESPACE_URI);
        SOAPBodyElement terminalsElement = (SOAPBodyElement) terminalResponseElement.getChildElements(terminals).next();
        SOAPBodyElement terminalElement = (SOAPBodyElement) terminalsElement.getChildElements(terminal).next();
        NodeList list = terminalElement.getChildNodes();
        Node n = null;
        for (int i = 0; i < list.getLength(); i ++) {
            n = list.item(i);
            if ("status".equalsIgnoreCase(n.getLocalName()))
                break;
        }

        System.out.println("status of device = " + n.getTextContent());
        System.out.println("Terminal Response [" + terminalValue + "]");

    }

    /**
     * This method is used to add the security. This uses xwss:UsernameToken configuration and expects
     * Username and Password to be passes. SecurityPolicy.xml file should be in classpath.
     *
     * @param message
     * @param username
     * @param password
     * @return
     * @throws IOException
     * @throws XWSSecurityException
     */
    private SOAPMessage secureMessage(SOAPMessage message, final String username, final String password)
            throws IOException, XWSSecurityException {
        CallbackHandler callbackHandler = new CallbackHandler() {
            public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
                for (int i = 0; i < callbacks.length; i++) {
                    if (callbacks[i] instanceof UsernameCallback) {
                        UsernameCallback callback = (UsernameCallback) callbacks[i];
                        callback.setUsername(username);
                    } else if (callbacks[i] instanceof PasswordCallback) {
                        PasswordCallback callback = (PasswordCallback) callbacks[i];
                        callback.setPassword(password);
                    } else {
                        throw new UnsupportedCallbackException(callbacks[i]);
                    }
                }
            }
        };
        InputStream policyStream = null;
        XWSSProcessor processor = null;
        try {
            policyStream = getClass().getResourceAsStream("securityPolicy.xml");
            processor = processorFactory.createProcessorForSecurityConfiguration(policyStream, callbackHandler);
        }
        finally {
            if (policyStream != null) {
                policyStream.close();
            }
        }
        ProcessingContext context = processor.createProcessingContext(message);
        return processor.secureOutboundMessage(context);
    }

    /**
     * Main program. Usage : TerminalClient <username> <password>
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // Apitest URL. See "Get WSDL Files" in the API documentation for Production URL.
    // String url = "https://apitest.jasperwireless.com/ws/service/terminal";
     /*     if (args.length != 4) {
            System.out.println("usage: GetTerminalDetailsClient <license-key> <username> <password> <iccid>");
            System.exit(-1);
        }
        GetTerminalDetailsClient terminalClient = new GetTerminalDetailsClient(url, args[0]);
        terminalClient.callWebService(args[1], args[2], args[3]);
        */
        //设备当前月份的用量
        String key = "04f4d3fe-0706-4683-900a-f89c93304178";
        String url ="https://api.10646.cn/ws/service/terminal";
        String username ="liuchang16523";
        String password = "KUyu8888@";
        GetTerminalDetailsClient terminalClient = new GetTerminalDetailsClient(url, key);
        terminalClient.callWebService(username, password,"89860617060023161011");
        
        
    }
}

