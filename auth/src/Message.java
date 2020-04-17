import java.util.*;
import java.text.*;

/**
 * Mail message.
 */
public class Message {
    /* The headers and the body of the message. */
    public String Headers;
    public String Body;

    /* Sender and recipient. With these, we don't need to extract 
       them from the headers. */
    private String From;
    private String Password;
    private String To;

    /* To make it look nicer */
    private static final String CRLF = "\r\n";

    /* Create the message object by inserting the required headers 
       from RFC 822 (From, To, Date). */
    public Message(String from, String password, String to, String subject, String text)

{
	/* Remove whitespace */
	From = from.trim();
	To = to.trim();
	Password = password.trim();
	Headers = "From: " + From + CRLF;
	Headers += "To: " + To + CRLF;
	Headers += "Subject: " + subject.trim() + CRLF;

	/* A close approximation of the required format. Unfortunately
	   only GMT. */
	SimpleDateFormat format = 
	    new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
	String dateString = format.format(new Date());
	Headers += "Date: " + dateString + CRLF;
	Body = text;
    }

    /* Two functions to access the sender and recipient. */
    public String getFrom() {
	return From;
    }

    public String getPassword() {
        return Password;
    }

    public String getTo() {
	return To;
    }

    /* Check whether the message is valid. In other words, check that
       both sender and recipient contain only one @-sign. */
    public boolean isValid() {
	int fromAt = From.indexOf('@');
	int toAt = To.indexOf('@');

	if(fromAt < 1 || (From.length() - fromAt) <= 1) {
	    System.out.println("Sender address is invalid");
	    return false;
	}
	if(toAt < 1 || (To.length() - toAt) <= 1) {
	    System.out.println("Recipient address is invalid");
	    return false;
	}
	if(fromAt != From.lastIndexOf('@')) {
	    System.out.println("Sender address is invalid");
	    return false;
	}
	if(toAt != To.lastIndexOf('@')) {
	    System.out.println("Recipient address is invalid");
	    return false;
	}	
	return true;
    }
    
    /* For printing the message. */
    public String toString() {
	String res;

	res = Headers + CRLF;
	res += Body;
	return res;
    }
}