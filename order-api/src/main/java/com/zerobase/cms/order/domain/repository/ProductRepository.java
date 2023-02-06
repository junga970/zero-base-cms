package com.zerobase.cms.order.domain.repository;

import com.zerobase.cms.order.domain.model.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@EntityGraph(attributePaths = {"productItems"},type = EntityGraphType.LOAD)
	Optional<Product> findWithProductItemsById(Long productId);

	@EntityGraph(attributePaths = {"productItems"},type = EntityGraphType.LOAD)
	Optional<Product> findBySellerIdAndId(Long sellerId, Long id);
}