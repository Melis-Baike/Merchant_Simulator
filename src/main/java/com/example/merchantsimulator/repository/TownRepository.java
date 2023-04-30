package com.example.merchantsimulator.repository;

import com.example.merchantsimulator.entity.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TownRepository extends JpaRepository<Town, Long> {
    Optional<Town> findByName(String name);
}