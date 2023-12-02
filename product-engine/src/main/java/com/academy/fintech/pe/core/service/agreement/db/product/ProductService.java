package com.academy.fintech.pe.core.service.agreement.db.product;

import com.academy.fintech.pe.core.service.agreement.db.product.entity.Product;
import com.academy.fintech.pe.public_interface.agreement.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Gets product by code from database
     *
     * @param code product code
     * @return product
     * @throws NotFoundException if product not found
     */
    public Product getProduct(String code) {
        return productRepository.findById(code).orElseThrow(() -> new NotFoundException("Product not found"));
    }
}
