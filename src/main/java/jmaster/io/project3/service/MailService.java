package jmaster.io.project3.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

//
//import java.nio.charset.StandardCharsets;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//x
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.context.Context;
//import org.thymeleaf.spring5.SpringTemplateEngine;
//
@Service
public class MailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendEmailBill(String to) {
		String subject= "<h1>Thong bao don hang</h1>";
		String body="Bạn vừa có 1 đơn hàng mới. Cám ơn bạn đã mua !";
		sendEmail(to, subject, body);
		
	}
	
	public void sendBirthday(String to) {
		String subject= "<h1>Happy birthday</h1>";
		String body="Chuc minh sinh nhat!";
		sendEmail(to, subject, body);
		
	}
	
	public void sendEmail(String to, String subject, String body) {
		MimeMessage message=javaMailSender.createMimeMessage();
		MimeMessageHelper helper= new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
		try {
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body);
			helper.setFrom("phuongphamdinh@gmail.com");
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public void sendEmailAttach(String to, String subject, String body) throws AddressException, MessagingException, IOException {
		
		String from="phuongphamdinh@gmail.com";
		MimeMessage message=javaMailSender.createMimeMessage();
		message.setFrom(new InternetAddress(from)); 
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); 
		message.setSubject(subject); 

		BodyPart messageBodyPart = new MimeBodyPart(); 
		messageBodyPart.setText(body);
		
		MimeBodyPart attachmentPart = new MimeBodyPart();
		attachmentPart.attachFile(new File("D:/Du lieu/Untitled-1.jpg"));
		
		Multipart multipart= new MimeMultipart() ;
			multipart.addBodyPart(messageBodyPart);
			multipart.addBodyPart(attachmentPart);
			message.setContent(multipart);
			Transport.send(message)	;
		
	}

}
