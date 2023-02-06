package com.zerobase.cms.user.controller;

import com.zerobase.cms.user.domain.customer.ChangeBalanceForm;
import com.zerobase.cms.user.domain.customer.CustomerDto;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.customer.CustomerService;
import com.zerobase.domain.common.UserVo;
import com.zerobase.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerService customerService;
	private final CustomerBalanceService customerBalanceService;
	private final JwtAuthenticationProvider provider;

	@GetMapping("/getInfo")
	public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X-AUTH-TOKEN") String token) {
		UserVo vo = provider.getUserVo(token);
		Customer c = customerService.findByIdAndEmail(vo.getId(), vo.getEmail()).orElseThrow(
			() -> new CustomException(ErrorCode.USER_NOT_FOUND)
		);
		return ResponseEntity.ok(CustomerDto.from(c));
	}

	@PostMapping("/balance")
	public ResponseEntity<Integer> changeBalance(@RequestHeader(name = "X-AUTH-TOKEN") String token,
		@RequestBody ChangeBalanceForm form) {
		UserVo vo = provider.getUserVo(token);
		return ResponseEntity.ok(customerBalanceService.changeBalance(vo.getId(), form).getCurrentMoney());
	}
}