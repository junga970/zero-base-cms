package com.zerobase.cms.order.domain.product;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductForm {
	private Long id;
	private Long productId;
	private String name;
	private String description;
	private List<UpdateProductItemForm> items;
}