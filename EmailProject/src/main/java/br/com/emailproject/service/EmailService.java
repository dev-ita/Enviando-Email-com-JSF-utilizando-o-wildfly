package br.com.emailproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import br.com.emailproject.model.Email;
import br.com.emailproject.util.LogUtil;

@Stateless
public class EmailService extends Thread {

	private List<Email> emails;
	public static final String HEADER_CONTEXT = "text/html; charset=utf-8";
	private String username = "italojavamailtest@gmail.com";
	private String password = "kzva btsu plas xxdb";

	public void enviar(Email email) {
		emails = new ArrayList<>();
		emails.add(email);
		send();
	}

	public void enviar(List<Email> emails) {
		this.emails = emails;
		send();
	}

	private EmailService copy() {
		EmailService emailService = new EmailService();
		emailService.emails = emails;
		return emailService;
	}

	private void send() {
		new Thread(this.copy()).start();
	}

	@Override
	public void run() {
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.ssl.trust", "*"); // autenticar com segurança SSL
		props.put("mail.smtp.auth", "true"); // autorização
		props.put("mail.smtp.host", System.getProperty("email-project.mail.smtp.host"));
		props.put("mail.smtp.port", System.getProperty("email-project.mail.smtp.port"));
		props.put("mail.smtp.socketFactory.port", System.getProperty("email-project.mail.smtp.port")); // expecifíca a porta a ser conectada pelo socket
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // classe socket de conexão
																							// ao SMTP

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		session.setDebug(false);

		for (Email email : emails) {
			try {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(System.getProperty("email-project.mail.from")));

				if (email.getDestinatario().contains("/")) {
					List<InternetAddress> emailsLocal = new ArrayList<InternetAddress>();
					for (String e : email.getDestinatario().split("/")) {
						emailsLocal.add(new InternetAddress(e));
					}
					message.addRecipients(RecipientType.TO, emailsLocal.toArray(new InternetAddress[0]));
				} else {
					InternetAddress para = new InternetAddress(email.getDestinatario());
					message.addRecipient(RecipientType.TO, para);
				}

				message.setSubject(email.getAssunto());

				MimeBodyPart textPart = new MimeBodyPart();
				textPart.setHeader("Content-Type", HEADER_CONTEXT);
				textPart.setContent(email.getTexto(), HEADER_CONTEXT);
				Multipart mp = new MimeMultipart();
				mp.addBodyPart(textPart);
				message.setContent(mp);
				
				Transport.send(message);
				
			} catch (MessagingException e) {
				LogUtil.getLogger(EmailService.class).error("Erro ao enviar e-mail: " + e.getMessage());
			}
		}
	}
}