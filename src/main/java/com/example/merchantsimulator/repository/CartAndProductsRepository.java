package com.example.merchantsimulator.repository;

import com.example.merchantsimulator.entity.CartAndProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartAndProductsRepository extends JpaRepository<CartAndProducts, Long> {
    List<CartAndProducts> findByCartId(Long cartId);
    Optional<CartAndProducts> findByCartIdAndProductId(Long cartId, Long productId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE CartAndProducts cap SET cap.weightOfProduct=:#{#weightOfProduct} WHERE cap.id=:#{#id}")
    void setWeightOfProductById(@Param("weightOfProduct") int weightOfProduct, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE CartAndProducts cap WHERE cap.cartId=:#{#cartId}")
    void deleteAllByCartId(@Param("cartId") Long cartId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE CartAndProducts cap SET cap.productConditionId=:#{#productConditionId} WHERE cap.id=:#{#id}")
    void setProductConditionIdById(@Param("productConditionId") Long productConditionId, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE CartAndProducts cap SET cap.productId=:#{#productId} WHERE cap.id=:#{#id}")
    void setProductIdById(@Param("productId") Long productId, @Param("id") Long id);
}
