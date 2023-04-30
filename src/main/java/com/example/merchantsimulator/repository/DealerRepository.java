package com.example.merchantsimulator.repository;

import com.example.merchantsimulator.entity.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE Dealer d SET d.townId = :#{#townId} WHERE d.id = :#{#id}")
    void setTownIdById(@Param("townId") Long townId, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Dealer d SET d.townsLeagues=:#{#townsLeagues} WHERE d.id=:#{#id}")
    void setTownsLeaguesById(@Param("townsLeagues") int townsLeagues, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Dealer d SET d.money=:#{#money} WHERE d.id=:#{#id}")
    void setMoneyById(@Param("money") double money, @Param("id") Long id);
}
