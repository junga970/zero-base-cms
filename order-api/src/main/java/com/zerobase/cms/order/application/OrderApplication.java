package com.zerobase.cms.order.application;

import static com.zerobase.cms.order.exception.ErrorCode.ORDER_FAIL_CHECK_CART;
import static com.zerobase.cms.order.exception.ErrorCode.ORDER_FAIL_NO_MONEY;

import com.zerobase.cms.order.client.UserClient;
import com.zerobase.cms.order.client.mailgun.SendMailForm;
import com.zerobase.cms.order.client.user.ChangeBalanceForm;
import com.zerobase.cms.order.client.user.CustomerDto;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.service.ProductItemService;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderApplication {

	private final CartApplication cartApplication;
	private final UserClient userClient;
	private final ProductItemService productItemService;
	private final MailgunClient mailgunClient;

	@Transactional
	public void sendOrder(String token, Cart cart) {
		Cart orderCart = cartApplication.refreshCart(cart);

		if (orderCart.getMessages().size() > 0) {
			throw new CustomException(ORDER_FAIL_CHECK_CART);
		}
		CustomerDto customerDto = userClient.getCustomerInfo(token).getBody();

		int totalPrice = getTotalPrice(cart);
		if (customerDto.getBalance() < totalPrice) {
			throw new CustomException(ORDER_FAIL_NO_MONEY);
		}
		// 롤백 계획에 대해서 생각해야 함.
		userClient.changeBalance(token,
			ChangeBalanceForm.builder()
				.from("USER")
				.message("Order")
				.money(customerDto.getBalance()-totalPrice)
				.build());

		for (Cart.Product product : orderCart.getProducts()) {
			for (Cart.ProductItem cartItem : product.getItems()) {
				ProductItem productItem = productItemService.getProductItem(cartItem.getId());
				productItem.setCount(productItem.getCount()-cartItem.getCount());
			}
		}
	}

	public Integer getTotalPrice(Cart cart) {
		return cart.getProducts().stream().flatMapToInt(
				product -> product.getItems().stream().flatMapToInt(
					productItem -> IntStream.of(productItem.getPrice() * productItem.getCount())))
			.sum();
	}
}