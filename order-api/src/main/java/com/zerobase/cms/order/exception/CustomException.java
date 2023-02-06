package com.zerobase.cms.order.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException{
	private final ErrorCode errorCode;
	private final int status;

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getDetail());
		this.errorCode = errorCode;
		this.status = errorCode.getHttpStatus().value();
	}

	@AllArgsConstructor
	@Builder
	@NoArgsConstructor
	@Getter
	public static class CustomExceptionResponse{
		private int status;
		private String code;
		private String message;
	}
}