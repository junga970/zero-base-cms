package com.zerobase.cms.order.application;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.exception.ErrorCode;
import com.zerobase.cms.order.service.CartService;
import com.zerobase.cms.order.service.ProductSearchService;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartApplication {

	private final ProductSearchService productSearchService;
	private final CartService cartService;


	@Transactional
	public Cart addCart(Long customerId, AddProductCartForm form) {
		Product product = productSearchService.getByProductId(form.getId());
		if (product == null) {
			throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
		}
		Cart cart = cartService.getCart(customerId);

		if (!addAble(cart, product, form)) {
			throw new CustomException(ErrorCode.ITEM_COUNT_NOT_ENOUGH);
		}
		return cartService.addCart(customerId, form);
	}

	private boolean addAble(Cart cart, Product product, AddProductCartForm form) {
		Cart.Product cartProduct = cart.getProducts().stream().filter(p -> p.getId().equals(form.getId()))
			.findFirst().orElse(Cart.Product.builder()
				.id(product.getId())
				.items(Collections.emptyList()).build());
		Map<Long, Integer> cartItemCountMap = cartProduct.getItems().stream()
			.collect(Collectors.toMap(Cart.ProductItem::getId, Cart.ProductItem::getCount));
		Map<Long, Integer> currentItemCountMap = product.getProductItems().stream()
			.collect(Collectors.toMap(ProductItem::getId, ProductItem::getCount));

		return form.getItems().stream().noneMatch(
			formItems -> {
				Integer cartCount = cartItemCountMap.get(formItems.getId());
				if (cartCount==null){
					cartCount=0;
				}
				Integer currentCount = currentItemCountMap.get(formItems.getId());
				return formItems.getCount() + cartCount > currentCount;
			});
	}
}