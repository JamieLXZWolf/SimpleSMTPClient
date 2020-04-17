import java.io.*;
import java.net.*;
import java.util.*;

/**
 * SMTP envelope for one mail message.
 */
public class Envelope {
    /* SMTP-sender of the message (in this case, contents of 

From-header. */
    public String Sender;
    public String SenderName;
    public String Password;

    /* SMTP-recipient, or contents of To-header. */
    public String Recipient;

    /* Target MX-host */
    public String DestHost;
    public InetAddress DestAddr;

    public String SourceHost;
    public InetAddress SourceAddr;

    /* The actual message */
    public Message Message;

    /* Create the envelope. */
    public Envelope(Message message) {
	/* Get sender and recipient. */
	Sender = message.getFrom();
	Password = message.getPassword();
	Recipient = message.getTo();

	/* Get message. We must escape the message to make sure that 
	   there are no single periods on a line. This would mess up
	   sending the mail. */
	Message = escapeMessage(message);

	/* Get the hostname part of the recipient. It should be the
	   name of the MX-host for the recipient's domain. */
	int destAtSign = Recipient.lastIndexOf('@');
	DestHost = Recipient.substring(destAtSign + 1);

	int sourceAtSign = Sender.lastIndexOf('@');
	SourceHost = "smtp." + Sender.substring(sourceAtSign + 1);
	SenderName = Sender.substring(0, sourceAtSign);

	/* Map the name into an IP-address */
	try {
	    DestAddr = InetAddress.getByName(DestHost);
	} catch (UnknownHostException e) {
	    System.out.println("Unknown host: " + DestHost);
        e.printStackTrace();
	}

	try {
	    SourceAddr = InetAddress.getByName(SourceHost);
    } catch (UnknownHostException e) {
        System.out.println("Unknown host: " + SourceHost);
        e.printStackTrace();
    }
    }

    /* Escape the message by doubling all periods at the beginning of
       a line. */
    private Message escapeMessage(Message message) {
	String escapedBody = "";
	String token;
	StringTokenizer parser = new StringTokenizer(message.Body, "\n", true);

	while(parser.hasMoreTokens()) {
	    token = parser.nextToken();
	    if(token.startsWith(".")) {
		token = "." + token;
	    }
	    escapedBody += token;
	}
	message.Body = escapedBody;
	return message;
    }

    /* For printing the envelope. Only for debug. */
    public String toString() {
	String res = "Sender: " + Sender + '\n';
	res += "Recipient: " + Recipient + '\n';
	res += "Sender-Host" + SourceHost + ", address: " + SourceAddr + '\n';
	res += "MX-host: " + DestHost + ", address: " + DestAddr + '\n';
	res += "Message:" + '\n';
	res += Message.toString();
	
	return res;
    }
}


