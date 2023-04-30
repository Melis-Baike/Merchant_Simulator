package com.example.merchantsimulator.enums;

import com.example.merchantsimulator.comparator.CustomComparator;
import com.example.merchantsimulator.entity.*;
import com.example.merchantsimulator.service.DealerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public enum Events {
    USUAL_DAY("Usual day", 0, 0, "../static/usualDay.jpg"){
        @Override
        public String info(DealerService dealerService) {
            return "This day was without any cases";
        }
    },
    RAIN("Rain", -1,-2, "../static/rain.jpg"){
        @Override
        public String info(DealerService dealerService) {
            Random rnd = new Random();
            int chance = rnd.nextInt(5);
            if(chance == 0){
                SPOILAGE.info(dealerService);
            }
            return "It is raining";
        }
    },
    SMOOTH_WAY("Smooth way",2,2,"../static/smoothWay.jpg"){
        @Override
        public String info(DealerService dealerService) {
            return "We are on the smooth road";
        }
    },
    BROKEN_CART("Broken cart",-3,-3, "../static/brokenCart.jpg"){
        @Override
        public String info(DealerService dealerService) {
            return "Cart has broken. Dealer is repairing it";
        }
    },
    RIVER("River",-1,-2, "../static/river.jpg"){
        @Override
        public String info(DealerService dealerService) {
            return "We stumbled upon the river";
        }
    },
    MEETING_LOCAL("Meeting local",3,6, "../static/meetingLocal.jpg"){
        @Override
        public String info(DealerService dealerService) {
            return "A local came across. He showed the shortcut";
        }
    },
    ROBBERS("Robbers",-3,-3, "../static/robbers.jpg"){
        @Override
        public String info(DealerService dealerService) {
            StringBuilder sb = new StringBuilder();
            sb.append("The robbers attacked us!");
            int paymentToRobbers = 3000;
            Dealer dealer = dealerService.getDealerRepository().findById(1L).get();
            Cart cart = dealerService.getCartRepository().findById(dealer.getId()).get();
            List<CartAndProducts> cartAndProducts = dealerService.getCartAndProductsRepository()
                    .findByCartId(cart.getId());
            if(dealer.getMoney() > paymentToRobbers) {
                dealer.setMoney(dealer.getMoney() - paymentToRobbers);
                dealerService.getDealerRepository().setMoneyById(dealer.getMoney(), dealer.getId());
                sb.append(" Dealer gave to robbers ").append(paymentToRobbers).append("$");
            } else {
                CustomComparator customComparator = new CustomComparator(dealerService.getProductRepository());
                List<CartAndProducts> sortedCartAndProducts = cartAndProducts.stream().sorted(customComparator.reversed())
                        .collect(Collectors.toList());
                if(sortedCartAndProducts.size() > 0) {
                    CartAndProducts cartAndProduct = sortedCartAndProducts.get(0);
                    cart.setLoadCapacity(cart.getLoadCapacity() - cartAndProduct.getWeightOfProduct());
                    dealerService.getCartRepository().setLoadCapacityById(cart.getLoadCapacity(), cart.getId());
                    dealerService.getCartAndProductsRepository().delete(cartAndProduct);
                    sb.append(". ").append(sortedCartAndProducts.get(0)).append(" was taken by robbers");
                } else {
                    if(dealer.getMoney() != 0) {
                        sb.append(" Dealer gave the robbers his last money").append(dealer.getMoney())
                                .append("$. He has 0 money");
                        dealer.setMoney(0);
                        dealerService.getDealerRepository().setMoneyById(dealer.getMoney(), dealer.getId());
                    } else {
                        sb.append(" Dealer doesn't have any product and money. Dealer danced them a dance " +
                                "with a tambourine");
                    }
                }
            }
            return String.valueOf(sb);
        }
    },
    INN("Inn",0,0, "../static/inn.jpg"){
        @Override
        public String info(DealerService dealerService) {
            StringBuilder sb = new StringBuilder();
            sb.append("The road led us to an inn");
            Random rnd = new Random();
            int randomForInn = rnd.nextInt(2);
            if(randomForInn == 0){
                Dealer dealer = dealerService.getDealerRepository().findById(1L).get();
                int overnightFee = rnd.nextInt(500) + 500;
                if(dealer.getMoney() > overnightFee) {
                    sb.append(". Dealer decided to stop at inn. Dealer paid ").append(overnightFee)
                            .append("$ for the night");
                    int randomForAction = rnd.nextInt(3);
                    INN.setInitRange(-3);
                    INN.setFiniteRange(-3);
                    dealer.setMoney(dealer.getMoney() - overnightFee);
                    Cart cart = dealerService.getCartRepository().findById(dealer.getId()).get();
                    List<CartAndProducts> cartAndProducts = dealerService.getCartAndProductsRepository()
                            .findByCartId(cart.getId());
                    int randomCartAndProductId = rnd.nextInt(cartAndProducts.size());
                    CartAndProducts cartAndProduct = cartAndProducts.get(randomCartAndProductId);
                    Product product = dealerService.getProductRepository().findById(cartAndProduct.getProductId()).get();
                    Long randomProductIdFromInn = (long) rnd.nextInt(Products.values().length) + 1;
                    Product randomProduct = dealerService.getProductRepository().findById(randomProductIdFromInn).get();
                    Optional<CartAndProducts> optionalCartAndProducts = dealerService.getCartAndProductsRepository()
                            .findByCartIdAndProductId(cart.getId(), randomProduct.getId());
                    int weight = 50;
                    if (randomForAction == 0) {
                        if(optionalCartAndProducts.isPresent()){
                            dealerService.getCartAndProductsRepository().setWeightOfProductById(optionalCartAndProducts
                                    .get().getWeightOfProduct() + weight, optionalCartAndProducts.get().getId());
                        } else {
                            dealerService.getCartAndProductsRepository().setProductIdById(randomProduct.getId(),
                            cartAndProduct.getId());
                        }
                        sb.append(". Dealer exchanged ").append(product.getName()).append(" for ")
                                .append(randomProduct.getName());
                    } else if (randomForAction == 1) {
                        cart.setLoadCapacity(cart.getLoadCapacity() - cartAndProduct.getWeightOfProduct());
                        dealerService.getCartAndProductsRepository().delete(cartAndProduct);
                        int price = cartAndProduct.getWeightOfProduct() * product.getPrice();
                        sb.append(". Dealer sold ").append(product.getName()).append(" for ").append(price);
                        dealer.setMoney(dealer.getMoney() + price);
                    } else {
                        int randomWeight = rnd.nextInt(50) + 50;
                        int priceForPurchasedProduct = randomWeight * product.getPrice();
                        if(optionalCartAndProducts.isPresent()){
                            dealerService.getCartAndProductsRepository().setWeightOfProductById(optionalCartAndProducts
                                    .get().getWeightOfProduct() + weight, optionalCartAndProducts.get().getId());
                        } else {
                            if (dealer.getMoney() > priceForPurchasedProduct) {
                                CartAndProducts boughtProduct = CartAndProducts.builder()
                                        .cartId(cart.getId())
                                        .productId(randomProduct.getId())
                                        .productConditionId(1L)
                                        .weightOfProduct(randomWeight)
                                        .build();
                                dealerService.getCartAndProductsRepository().save(boughtProduct);
                                cart.setLoadCapacity(cart.getLoadCapacity() + randomWeight);
                                sb.append(". Dealer bought ").append(product.getName()).append(" for ")
                                        .append(priceForPurchasedProduct);
                                dealer.setMoney(dealer.getMoney() - priceForPurchasedProduct);
                            } else {
                                sb.append(". Dealer wanted to buy ").append(randomWeight).append(" kg of")
                                        .append(product.getName()).append(" at a price of ").append(product.getPrice())
                                        .append("$ per kg. But dealer doesn't have enough money");
                            }
                        }
                    }
                    dealerService.getDealerRepository().setMoneyById(dealer.getMoney(), dealer.getId());
                    dealerService.getCartRepository().setLoadCapacityById(cart.getLoadCapacity(), cart.getId());
                } else {
                    sb.append(". Dealer doesn't have enough money to stay at inn. Dealer continued on his way");
                }
            } else {
                INN.setInitRange(0);
                INN.setFiniteRange(0);
                sb.append(". Dealer decided not to stop at inn. Dealer continued on his way");
            }
            return String.valueOf(sb);
        }
    },
    SPOILAGE("Spoilage",0,0, "../static/spoilage.jpg"){
        @Override
        public String info(DealerService dealerService) {
            StringBuilder sb = new StringBuilder();
            Dealer dealer = dealerService.getDealerRepository().findById(1L).get();
            Cart cart = dealerService.getCartRepository().findById(dealer.getId()).get();
            List<CartAndProducts> cartAndProducts = dealerService.getCartAndProductsRepository()
                    .findByCartId(cart.getId());
            Random rnd = new Random();
            if(cartAndProducts.size() > 0) {
                int randomCartAndProduct = rnd.nextInt(cartAndProducts.size());
                CartAndProducts cartAndProduct = cartAndProducts.get(randomCartAndProduct);
                Condition condition = dealerService.getConditionRepository().findById(cartAndProduct
                        .getProductConditionId()).get();
                Conditions conditions = Stream.of(Conditions.values())
                        .filter(e -> e.getName().equals(condition.getName())).findFirst().get();
                CartAndProducts changedCartAndProduct = conditions.changeCondition(cartAndProduct);
                if (changedCartAndProduct != null) {
                    dealerService.getCartAndProductsRepository().setProductConditionIdById(changedCartAndProduct
                            .getProductConditionId(), changedCartAndProduct.getId());
                    sb.append("The quality of the ").append(dealerService.getProductRepository()
                                    .findById(changedCartAndProduct.getProductId()).get().getName())
                            .append(" product has dropped to a ").append(dealerService.getConditionRepository()
                                    .findById(changedCartAndProduct.getProductConditionId())
                                    .get().getName()).append(" level");
                } else {
                    cart.setLoadCapacity(cart.getLoadCapacity() - cartAndProduct.getWeightOfProduct());
                    dealerService.getCartAndProductsRepository().delete(cartAndProduct);
                    sb.append(". The product ").append(dealerService.getProductRepository()
                            .findById(changedCartAndProduct.getProductId()).get().getName()).append(" is thrown away");
                }
            } else {
                sb.append("You don't have any products in your cart. Because of this, there is nothing to spoil");
            }
            dealerService.getCartRepository().setLoadCapacityById(cart.getLoadCapacity(), cart.getId());
            return String.valueOf(sb);
        }
    };

    private final String name;
    private int initRange;
    private int finiteRange;

    private final String imageName;


    public String getName() {
        return name;
    }

    public int getInitRange() {
        return initRange;
    }

    public void setInitRange(int initRange) {
        this.initRange = initRange;
    }

    public int getFiniteRange() {
        return finiteRange;
    }

    public void setFiniteRange(int finiteRange) {
        this.finiteRange = finiteRange;
    }

    public String getImageName() {
        return imageName;
    }

    Events(String name, int initRange, int finiteRange, String imageName) {
        this.name = name;
        this.initRange = initRange;
        this.finiteRange = finiteRange;
        this.imageName = imageName;
    }

    public abstract String info(DealerService dealerService);
}
