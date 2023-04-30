package com.example.merchantsimulator.enums;

import com.example.merchantsimulator.entity.CartAndProducts;

public enum Conditions {
    EXCELLENT("Excellent", 1.2){
        @Override
        public CartAndProducts changeCondition(CartAndProducts cartAndProducts) {
            cartAndProducts.setProductConditionId(2L);
            return cartAndProducts;
        }
    },
    GOOD("Good", 0.95){
        @Override
        public CartAndProducts changeCondition(CartAndProducts cartAndProducts) {
            cartAndProducts.setProductConditionId(3L);
            return cartAndProducts;
        }
    },
    MEDIUM("Medium", 0.7){
        @Override
        public CartAndProducts changeCondition(CartAndProducts cartAndProducts) {
            cartAndProducts.setProductConditionId(4L);
            return cartAndProducts;
        }
    },
    BAD("Bad", 0.5){
        @Override
        public CartAndProducts changeCondition(CartAndProducts cartAndProducts) {
            cartAndProducts.setProductConditionId(5L);
            return cartAndProducts;
        }
    },
    TERRIBLE("Terrible", 0.3){
        @Override
        public CartAndProducts changeCondition(CartAndProducts cartAndProducts) {
            return null;
        }
    };

    private final String name;
    private final double coefficient;

    Conditions(String name, double coefficient) {
        this.name = name;
        this.coefficient = coefficient;
    }

    public String getName() {
        return name;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public abstract CartAndProducts changeCondition(CartAndProducts cartAndProducts);
}
