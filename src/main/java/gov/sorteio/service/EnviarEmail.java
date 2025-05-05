package gov.sorteio.service;

import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EnviarEmail {
    public String enviar(String email, String titulo, String mensagem){
        String username = "nucleo@ntist.com.br";
        String password = "Sds20#21";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.kinghost.net");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("nucleo@ntist.com.br"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(titulo);
            message.setContent(mensagem, "text/html");
            Transport.send(message);
            return "Success";
        } catch (MessagingException e) {
            return null;
        }
    }
}
