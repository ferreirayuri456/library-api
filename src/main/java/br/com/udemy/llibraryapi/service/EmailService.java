package br.com.udemy.llibraryapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private JavaMailSender sender;

	@Value("${application.mail.default.shipper}")
	private String shipper;

	public void sendMails(String message, List<String> mailsList) {
		var mails = mailsList.toArray(new String[mailsList.size()]);
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(shipper);
		mailMessage.setSubject("Livro com empréstimo atrasado");
		mailMessage.setText(message);
		mailMessage.setTo(mails);

		sender.send(mailMessage);
	}

}
