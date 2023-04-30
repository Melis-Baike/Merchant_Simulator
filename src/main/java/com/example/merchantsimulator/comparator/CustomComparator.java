package com.example.merchantsimulator.comparator;

import com.example.merchantsimulator.entity.CartAndProducts;
import com.example.merchantsimulator.entity.Product;
import com.example.merchantsimulator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class CustomComparator implements Comparator<CartAndProducts> {
    private final ProductRepository productRepository;

    @Override
    public int compare(CartAndProducts o1, CartAndProducts o2) {
        Product firstProduct = productRepository.findById(o1.getProductId()).get();
        Product secondProduct = productRepository.findById(o2.getProductId()).get();
        int firstValue = o1.getWeightOfProduct() * firstProduct.getPrice();
        int secondValue = o2.getWeightOfProduct() * secondProduct.getPrice();
        return Integer.compare(firstValue, secondValue);
    }
}
