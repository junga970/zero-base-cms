package com.zerobase.cms.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

	ALREADY_EXIST_ITEM_NAME(HttpStatus.BAD_REQUEST, "중복된 아이템명입니다."),
	PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
	ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "아이템을 찾을 수 없습니다."),
	ITEM_COUNT_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "상품 수량이 부족합니다."),
	KART_CHANGE_FAIL(HttpStatus.BAD_REQUEST, "장바구니에 추가할 수 없습니다."),
	ORDER_FAIL_CHECK_CART(HttpStatus.BAD_REQUEST, "주문 불가! 장바구니를 확인해주세요."),
	ORDER_FAIL_NO_MONEY(HttpStatus.BAD_REQUEST, "주문 불가! 잔액이 부족합니다.");

	private final HttpStatus httpStatus;
	private final String detail;
}