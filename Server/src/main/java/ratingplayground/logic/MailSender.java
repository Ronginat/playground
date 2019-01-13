package ratingplayground.logic;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;



public class MailSender {

 public final static String senderEmailID = "ratingplayground@mail.com";
 public final static String senderPassword = "ratingplayground1";
 public final static String emailSMTPserver = "smtp.mail.com";
 public final static String emailServerPort = "465";
 
 private String receiverEmailID = null;
 private String emailSubject = AppConstants.MAIL_SUBJECT;
 private String emailBody = AppConstants.MAIL_BODY;


 public MailSender(String receiverEmailID, String code) {

  // Receiver Email Address
  this.receiverEmailID = receiverEmailID;

  // Body
  this.emailBody = this.emailBody + code;
  
  System.out.println("BODY = " + emailBody);
  System.out.println("receiverEmailID = " + receiverEmailID);
  System.out.println("SUBJECT = " +emailSubject);
  
  
  Properties props = new Properties();
  props.put("mail.smtp.user", senderEmailID);
  props.put("mail.smtp.host", emailSMTPserver);
  props.put("mail.smtp.port", emailServerPort);
  props.put("mail.smtp.starttls.enable", "true");
  props.put("mail.smtp.auth", "true");
  props.put("mail.smtp.socketFactory.port", emailServerPort);
  props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
  props.put("mail.smtp.socketFactory.fallback", "false");
  SecurityManager security = System.getSecurityManager();
  Authenticator auth = new SMTPAuthenticator();
  try {
	   Session session = Session.getInstance(props, auth);
	   MimeMessage msg = new MimeMessage(session);
	   msg.setText(emailBody);
	   msg.setSubject(emailSubject);
	   msg.setFrom(new InternetAddress(senderEmailID));
	   msg.addRecipient(Message.RecipientType.TO,
	    new InternetAddress(receiverEmailID));
	   Transport.send(msg);
   System.out.println("Message sent Successfully!");
  } catch (Exception mex) {
   mex.printStackTrace();
  }


 }
 public class SMTPAuthenticator extends javax.mail.Authenticator {
  public PasswordAuthentication getPasswordAuthentication() {
   return new PasswordAuthentication(senderEmailID, senderPassword);
  }
 }
}