//package jmaster.io.project3.service;
//
//import java.nio.charset.StandardCharsets;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//import org.thymeleaf.context.Context;
//import org.thymeleaf.spring5.SpringTemplateEngine;
//
//@Service
//public class MailService {
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//    
//    @Autowired
//    private SpringTemplateEngine templateEngine;
//    
//	public void sendEmail(String to,
//			String subject, String body) {
//		try {
//		    MimeMessage message = javaMailSender.createMimeMessage();
//		    MimeMessageHelper helper = new MimeMessageHelper(message, 
//		    		StandardCharsets.UTF_8.name());
//		    
//		    //load template email with content
//		    Context context = new Context();
//		    context.setVariable("name", body);
//		    String html = templateEngine.process("hi.html", context);
//		    
//		    ///send email
//		    helper.setTo(to);//email gui toi
//		    helper.setText(html, true);//nd mail
//		    helper.setSubject(subject);
//		    helper.setFrom("your email");
//		    
//		    javaMailSender.send(message);
//
//		} catch (MessagingException e) {
//			e.printStackTrace();
////		    logger.error("Email sent with error: " + e.getMessage());
//		}
//	}
//}
