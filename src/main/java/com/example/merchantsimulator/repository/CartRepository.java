package com.example.merchantsimulator.repository;

import com.example.merchantsimulator.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findTopByOrderByIdDesc();

    @Modifying
    @Transactional
    @Query(value = "UPDATE Cart c SET c.loadCapacity=:#{#loadCapacity} WHERE c.id=:#{#id}")
    void setLoadCapacityById(@Param("loadCapacity") int loadCapacity, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Cart c SET c.currentEventId=:#{#currentEventId} WHERE c.id=:#{#id}")
    void setCurrentEventIdById(@Param("currentEventId") Long currentEventId, @Param("id") Long id);
}
