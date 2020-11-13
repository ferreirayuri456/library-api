package br.com.udemy.llibraryapi.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

	private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";

	private LoanService loanService;
	private EmailService emailService;

	@Value("${application.mail.lateLoans.message}")
	private String mensagem;

	@Scheduled(cron = CRON_LATE_LOANS)
	public void sendMail() {
		var allLateLoans = loanService.getAllLateLoans();
		var mailsList = allLateLoans.stream().map(loan -> loan.getCustomerEmail()).collect(Collectors.toList());

		emailService.sendMails(mensagem, mailsList);

	}

}
