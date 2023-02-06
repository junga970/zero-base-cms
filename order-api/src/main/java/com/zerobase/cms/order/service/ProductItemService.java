package com.zerobase.cms.order.service;

import static com.zerobase.cms.order.exception.ErrorCode.ALREADY_EXIST_ITEM_NAME;
import static com.zerobase.cms.order.exception.ErrorCode.ITEM_NOT_FOUND;
import static com.zerobase.cms.order.exception.ErrorCode.PRODUCT_NOT_FOUND;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.product.UpdateProductItemForm;
import com.zerobase.cms.order.domain.repository.ProductItemRepository;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.order.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductItemService {

	private final ProductRepository productRepository;
	private final ProductItemRepository productItemRepository;

	@Transactional
	public Product addProductItem(Long sellerId, AddProductItemForm form) {
		Product product = productRepository.findBySellerIdAndId(sellerId, form.getProductId())
			.orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));
		if (product.getProductItems().stream()
			.allMatch(item -> item.getName().equals(form.getName()))){
			throw new CustomException(ALREADY_EXIST_ITEM_NAME);
		}
		ProductItem productItem = ProductItem.of(sellerId, form);
		product.getProductItems().add(productItem);

		return product;
	}

	@Transactional
	public ProductItem updateProductItem(Long sellerId, UpdateProductItemForm form) {
		ProductItem productItem = productItemRepository.findById(form.getId())
			.filter(pi -> pi.getSellerId().equals(sellerId)).orElseThrow(
				() -> new CustomException(ITEM_NOT_FOUND));

		productItem.setName(form.getName());
		productItem.setCount(form.getCount());
		productItem.setPrice(form.getPrice());

		return productItem;
	}

	@Transactional
	public void deleteProductItem(Long sellerId, Long productItemId) {
		ProductItem productItem = productItemRepository.findById(productItemId)
			.filter(pi->pi.getSellerId().equals(sellerId)).orElseThrow(() -> new CustomException(
				ITEM_NOT_FOUND));
		productItemRepository.delete(productItem);
	}
}