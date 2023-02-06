package com.zerobase.cms.order.application;

import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.product.AddProductCartForm;
import com.zerobase.cms.order.domain.product.AddProductForm;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.redis.Cart;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.order.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CartApplicationTest {

	@Autowired
	private CartApplication cartApplication;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductRepository productRepository;

	@Test
	void ADD_MODIFY_TEST() {
		Product p = add_product();
		Product result = productRepository.findById(p.getId()).get();

		assertEquals(result.getName(),"나이키 조던");
		assertEquals(result.getSellerId(),p.getSellerId());
		assertEquals(result.getDescription(),"신발");
		assertEquals(result.getProductItems().size(),3);
		assertEquals(result.getProductItems().get(0).getName(),"나이키 조던");
		assertEquals(result.getProductItems().get(1).getPrice(),20000);
		assertEquals(result.getProductItems().get(2).getCount(),10);

		Long customerId=10L;
		cartApplication.clearCart(customerId);

		Cart cart = cartApplication.addCart(customerId,makeAddForm(result));
		assertEquals(cart.getMessages().size(),0);


		Cart cart1 = cartApplication.getCart(customerId);
		assertEquals(cart1.getMessages().size(),1);
		assertEquals(cart1.getMessages().get(0),"나이키 조던 상품의 변동 사항 : 나이키 조던 가격이 변경되었습니다.");
		assertEquals(cart1.getProducts().get(0).getName(),"나이키 조던");
		assertEquals(cart1.getProducts().get(0).getItems().get(0).getName(),"나이키 조던");
	}

	AddProductCartForm makeAddForm(Product p) {
		AddProductCartForm.ProductItem productItem =
			AddProductCartForm.ProductItem.builder()
				.id(p.getProductItems().get(0).getId())
				.name(p.getProductItems().get(0).getName())
				.count(5)
				.price(10000)
				.build();
		return AddProductCartForm.builder()
			.id(p.getId())
			.sellerId(p.getSellerId())
			.name(p.getName())
			.description(p.getDescription())
			.items(List.of(productItem))
			.build();
	}

	Product add_product() {
		Long sellerId = 1L;
		AddProductForm form = makeProductForm("나이키 조", "신발", 3);
		return productService.addProduct(sellerId, form);
	}

	private static AddProductForm makeProductForm(String name, String description, int itemCount) {
		List<AddProductItemForm> itemForms = new ArrayList<>();
		for (int i = 0; i < itemCount; i++) {
			itemForms.add(makeProductItemForm(null,name+i));
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
			.price(20000)
			.count(10)
			.build();
	}

}