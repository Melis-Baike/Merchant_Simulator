package com.example.merchantsimulator.entity;

import com.example.merchantsimulator.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Comparator;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartAndProducts implements Comparator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cartId;
    private Long productId;
    private Long productConditionId;
    private int weightOfProduct;

    @Override
    public int compare(Object o1, Object o2) {
        CartAndProducts cartAndProducts = (CartAndProducts) o1;
        CartAndProducts secondCartAndProducts = (CartAndProducts) o2;
        return Integer.compare(cartAndProducts.getWeightOfProduct(), secondCartAndProducts.getWeightOfProduct());
    }
}
