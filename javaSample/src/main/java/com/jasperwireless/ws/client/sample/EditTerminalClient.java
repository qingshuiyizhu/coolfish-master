package com.jasperwireless.ws.client.sample;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.wss.ProcessingContext;
import com.sun.xml.wss.XWSSProcessor;
import com.sun.xml.wss.XWSSProcessorFactory;
import com.sun.xml.wss.XWSSecurityException;
import com.sun.xml.wss.impl.callback.PasswordCallback;
import com.sun.xml.wss.impl.callback.UsernameCallback;

//停复机接口 
/*
changeType = 3 

targetValue 

DEACTIVATED_NAME  停机
ACTIVATED_NAME    复机 





 */

//限速接口
/*
changeType = 4
targetValue 
450WLW016523_NJ_DATA_4G_SMS              正常
450WLW016523_NJ_DATA_4G_SMS_Ltd_2        下行0.5Mb/s 上行0.5Mb/s
450WLW016523_NJ_DATA_4G_SMS_Ltd_3        下行1Mb/s 上行1Mb/s
450WLW016523_NJ_DATA_4G_SMS_Ltd_4        下行2Mb/s 上行2Mb/s
450WLW016523_NJ_DATA_4G_SMS_Ltd_5        下行7.25Mb/s 上行5.75Mb/s
*/

public class EditTerminalClient implements ApiClientConstant {
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
    public EditTerminalClient(String url, String licenseKey)
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
    private SOAPMessage createEditTerminalRequest(String iccid,String targetParam,String actionType) throws SOAPException {
        SOAPMessage message = messageFactory.createMessage();
        message.getMimeHeaders().addHeader("SOAPAction",
                "http://api.jasperwireless.com/ws/service/terminal/EditTerminal");
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        Name terminalRequestName = envelope.createName("EditTerminalRequest", PREFIX, NAMESPACE_URI);
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
        //Name iccids = envelope.createName("iccids", PREFIX, NAMESPACE_URI);
        //SOAPElement iccidsElement = terminalRequestElement.addChildElement(iccids);
        
        Name iccidName = envelope.createName("iccid", PREFIX, NAMESPACE_URI);
        SOAPElement iccidElement = terminalRequestElement.addChildElement(iccidName);
        iccidElement.setValue(iccid);
        
        Name targetValue = envelope.createName("targetValue",PREFIX, NAMESPACE_URI);
        SOAPElement targetValueElement  = terminalRequestElement.addChildElement(targetValue);
        targetValueElement.setValue(targetParam);
        
        Name changeType = envelope.createName("changeType",PREFIX, NAMESPACE_URI);
        SOAPElement changeTypeElement  = terminalRequestElement.addChildElement(changeType);
        changeTypeElement.setValue(actionType);
         
        return message;
    }

    public void callWebService(String username, String password, String iccid,String targetValue,String changeType ) throws SOAPException, IOException, XWSSecurityException, Exception {
        SOAPMessage request = createEditTerminalRequest(iccid,targetValue,changeType);
        request = secureMessage(request, username, password);
        System.out.println("Request: ");
        request.writeTo(System.out);
        System.out.println("");
        SOAPConnection connection = connectionFactory.createConnection();
        SOAPMessage response = connection.call(request, url);
        System.out.println("Response: ");
        response.writeTo(System.out);
        if (!response.getSOAPBody().hasFault()) {
            writeTerminalResponse(response);
        } else {
            SOAPFault fault = response.getSOAPBody().getFault();
            System.err.println("Received SOAP Fault");
            System.err.println("SOAP Fault Code :" + fault.getFaultCode());
            System.err.println("SOAP Fault String :" + fault.getFaultString());
        }
    }
    
    private void writeTerminalResponse(SOAPMessage message) throws SOAPException {
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        Name terminalResponseName = envelope.createName("EditTerminalResponse", PREFIX, NAMESPACE_URI);
        SOAPBodyElement terminalResponseElement = (SOAPBodyElement) message
                .getSOAPBody().getChildElements(terminalResponseName).next();
        String terminalValue = terminalResponseElement.getTextContent();
        Name terminal = envelope.createName("correlationId", PREFIX, NAMESPACE_URI);
        SOAPBodyElement terminalElement = (SOAPBodyElement) terminalResponseElement.getChildElements(terminal).next();
   

        System.out.println("correlationId = " + terminalElement.getTextContent());
        System.out.println("Terminal Response [" + terminalValue + "]");

    }
    
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
        EditTerminalClient terminalClient = new EditTerminalClient(url, key);
        terminalClient.callWebService(username, password,"89860617060023800964","DEACTIVATED_NAME","3");
         
    }
    
}
