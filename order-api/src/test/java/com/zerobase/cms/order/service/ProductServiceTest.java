package com.zerobase.cms.order.service;

import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductRepository productRepository;


	@Test
	@Transactional
	void ADD_PRODUCT_TEST() {
		Long sellerId = 1L;

		AddProductForm form = makeProductForm("상품", "설명", 3);

		Product p = productService.addProduct(sellerId, form);

		Product result = productRepository.findWithProductItemsById(p.getId()).get();

		assertNotNull(result);
		assertEquals(result.getName(),"상품");
		assertEquals(result.getSellerId(),1L);
		assertEquals(result.getDescription(),"설명");
		assertEquals(result.getProductItems().size(),3);
		assertEquals(result.getProductItems().get(0).getName(),"상품");
		assertEquals(result.getProductItems().get(0).getPrice(),10000);
		assertEquals(result.getProductItems().get(0).getCount(),1);
	}

	@Test
	@Transactional
	void DELETE_PRODUCT_TEST() {
		Long sellerId = 1L;

		AddProductForm form = makeProductForm("상품", "설명", 3);

		Product p = productService.addProduct(sellerId, form);

		productService.deleteProduct(sellerId, p.getId());

		Product result = productRepository.findById(p.getId()).orElse(null);

		assertEquals(p.getName(),"상품");
		assertNull(result);
	}

	private static AddProductForm makeProductForm(String name, String description, int itemCount) {
		List<AddProductItemForm> itemForms = new ArrayList<>();
		for (int i = 0; i < itemCount; i++) {
			itemForms.add(makeProductItemForm(null,name + i));
		}
		return AddProductForm.builder()
			.name(name)
			.description(description)
			.items(itemForms)
			.build();
	}

	private static AddProductItemForm makeProductItemForm(Long productId, String name) {
		return AddProductItemForm.builder()
			.productId(productId)
			.name(name)
			.price(10000)
			.count(1)
			.build();
	}
}