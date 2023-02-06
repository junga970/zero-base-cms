
package com.zerobase.cms.user.client;

import com.zerobase.cms.user.client.mailgun.SendMailForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailSendServiceTest {

	@Autowired
	private MailgunClient mailgunClient;

	@Test
	public void EmailTest() {
		mailgunClient.sendEmail(SendMailForm.builder()
			.from("zerobase-test@gmail.com")
			.to("wjddk2940@gmail.com")
			.text("mail test zerobase")
			.subject("test")
			.build());
	}
}