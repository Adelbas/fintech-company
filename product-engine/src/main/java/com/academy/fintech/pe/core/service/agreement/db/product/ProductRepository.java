package com.academy.fintech.pe.core.service.agreement.db.product;

import com.academy.fintech.pe.core.service.agreement.db.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}
