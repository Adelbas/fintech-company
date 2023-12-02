package com.academy.fintech.pe.core.service.agreement.db.product;

import com.academy.fintech.pe.core.service.agreement.db.product.entity.Product;
import com.academy.fintech.pe.public_interface.agreement.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getProduct_whenProductExists() {
        final String productCode = "CL_1.0";
        final Product expectedProduct = mock(Product.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        expectedProduct.setCode(productCode);
        when(productRepository.findById(productCode)).thenReturn(Optional.of(expectedProduct));

        Product actualProduct = productService.getProduct(productCode);

        assertThat(actualProduct).isEqualTo(expectedProduct);
        verify(productRepository, only()).findById(productCode);
    }

    @Test
    void getProduct_whenProductNotExists() {
        final String productCode = "CL_1.0";
        when(productRepository.findById(productCode)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> productService.getProduct(productCode));
        verify(productRepository, only()).findById(productCode);
    }
}