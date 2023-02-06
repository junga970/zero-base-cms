package com.zerobase.cms.user.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.service.customer.SignUpCustomerService;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SignUpCustomerServiceTest {

	@Autowired
	private SignUpCustomerService service;

	@Test
	void signUp() {
		SignUpForm form = SignUpForm.builder()
			.name("name")
			.birth(LocalDate.now())
			.email("abc@gmail.com")
			.phone("01000000000")
			.build();
		Customer  c = service.signUp(form);
		assertNotNull(c.getId());
		assertNotNull(c.getCreatedAt());
	}
}