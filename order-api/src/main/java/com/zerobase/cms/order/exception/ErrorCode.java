package com.zerobase.cms.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

	ALREADY_EXIST_ITEM_NAME(HttpStatus.BAD_REQUEST, "중복된 아이템명입니다."),
	PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
	ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "아이템을 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String detail;
}