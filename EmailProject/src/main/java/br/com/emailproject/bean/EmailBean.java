package br.com.emailproject.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.emailproject.dto.EmailLayout;
import br.com.emailproject.model.Email;
import br.com.emailproject.service.EmailService;

@Named
@RequestScoped
public class EmailBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String DESTINATARIO = "italo.ods@hotmail.com";
	private static final String ASSUNTO = "Alteração de Cadastro";

	@Inject
	private EmailService emailService;

	public String enviarEmail() {
		
//		List<Email> emails = new ArrayList<Email>();
//		emails.add(new EmailLayout().montarEmailAdministrador("italo.ods@hotmail.com", ASSUNTO));
//		emails.add(new EmailLayout().montarEmailAdministrador("isabele.ods@hotmail.com", ASSUNTO));
//		emails.add(new EmailLayout().montarEmailAdministrador("cristiane.ods@hotmail.com", ASSUNTO));
//		
//		emailService.enviar(emails);
		emailService.enviar(montarEmail());
		return null;
	}

	public Email montarEmail() {
		EmailLayout layout = new EmailLayout();
		return layout.montarEmailSecretario(DESTINATARIO, ASSUNTO);
//		return layout.montarEmailAdministrador(DESTINATARIO, ASSUNTO);
	}
}