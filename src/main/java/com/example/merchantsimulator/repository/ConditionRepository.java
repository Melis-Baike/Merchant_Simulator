package com.example.merchantsimulator.repository;

import com.example.merchantsimulator.entity.Condition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
