package com.example.merchantsimulator.service;

import com.example.merchantsimulator.entity.Cart;
import com.example.merchantsimulator.entity.CartAndProducts;
import com.example.merchantsimulator.entity.Dealer;
import com.example.merchantsimulator.entity.Product;
import com.example.merchantsimulator.repository.CartAndProductsRepository;
import com.example.merchantsimulator.repository.CartRepository;
import com.example.merchantsimulator.repository.DealerRepository;
import com.example.merchantsimulator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final DealerRepository dealerRepository;
    private final ProductRepository productRepository;
    private final CartAndProductsRepository cartAndProductsRepository;
    private final CartRepository cartRepository;

    public String buyProduct(String productName) {
        Optional<Dealer> dealer = dealerRepository.findById(1L);
        Optional<Cart> cart = cartRepository.findById(dealer.get().getCartId());
        Product product = productRepository.findByName(productName).get();
        if (cart.isPresent()) {
            if(cart.get().getCurrentEventId() == 0) {
                int weight = 50;
                int price = product.getPrice() * weight;
                if (dealer.get().getMoney() > price && cart.get().getLoadCapacity() > weight) {
                    Optional<CartAndProducts> optionalCartAndProduct = cartAndProductsRepository
                            .findByCartIdAndProductId(cart.get().getId(), product.getId());
                    if (optionalCartAndProduct.isPresent()) {
                        optionalCartAndProduct.get().setWeightOfProduct(optionalCartAndProduct.get().getWeightOfProduct() +
                                weight);
                        cartAndProductsRepository.setWeightOfProductById(optionalCartAndProduct.get().getWeightOfProduct(),
                                optionalCartAndProduct.get().getId());
                    } else {
                        CartAndProducts cartAndProducts = CartAndProducts.builder()
                                .cartId(cart.get().getId())
                                .productId(product.getId())
                                .productConditionId(1L)
                                .weightOfProduct(weight)
                                .build();
                        cartAndProductsRepository.save(cartAndProducts);
                        dealer.get().setMoney(dealer.get().getMoney() - price);
                        cart.get().setLoadCapacity(cart.get().getLoadCapacity() - weight);
                        dealerRepository.setMoneyById(dealer.get().getMoney(), dealer.get().getId());
                        cartRepository.setLoadCapacityById(cart.get().getLoadCapacity(), cart.get().getId());
                    }
                    return "You have successfully bought " + weight + "kg of next product: " + productName;
                } else {
                    return "Dealer doesn't have enough money or cart doesn't have enough load capacity";
                }
            } else {
                return "You are already on your way! You need to be in the bazaar to buy product";
            }
        }
        return "You haven't logged in yet";
    }
}
