/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudeer.timeclock.util;

/**
 *
 * @author sudee
 */
import java.util.Properties;    
import javax.mail.*;    
import javax.mail.internet.*;    

public class Mailer{    
    
    private String fromEmail = null;
    private String fromEmailPword=null;
    private String toEmail=null;
    
// public static void main(String[] args) {    
//     //from,password,to,subject,message  
//   try {
//       Mailer.send("Sudeer","Clocked In at 5pm.");
//       System.out.println("Email Sent");
//   }  catch(Exception ex){
//       System.out.println("error "+ex.toString());
//   }
//     //change from, password and to  
// }  

    public Mailer(Properties props) {
        fromEmail = props.getProperty("fromEmail");
        fromEmailPword=props.getProperty("fromEmailPword");
        toEmail=props.getProperty("toEmail");
    }
 
 
          //Get properties object    
    public void send(String sub,String msg){  
          Properties props = new Properties();    
          props.put("mail.smtp.host", "smtp.gmail.com");    
          props.put("mail.smtp.socketFactory.port", "465");    
          props.put("mail.smtp.socketFactory.class",    
                    "javax.net.ssl.SSLSocketFactory");    
          props.put("mail.smtp.auth", "true");    
          props.put("mail.smtp.port", "465");    
          //get Session   
          Session session = Session.getDefaultInstance(props,    
           new javax.mail.Authenticator() {    
           protected PasswordAuthentication getPasswordAuthentication() {    
           return new PasswordAuthentication(fromEmail,fromEmailPword);  
           }    
          });    
          //compose message    
          try {    
           MimeMessage message = new MimeMessage(session);    
           message.addRecipient(Message.RecipientType.TO,new InternetAddress(toEmail));    
           message.setSubject(sub);    
           message.setText(msg);    
           //send message  
           Transport.send(message);    
          // System.out.println("message sent successfully");    
          } catch (MessagingException e) {throw new RuntimeException(e);}    
             
    }  
}  
    
