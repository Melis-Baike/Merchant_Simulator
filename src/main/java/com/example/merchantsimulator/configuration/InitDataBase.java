package com.example.merchantsimulator.configuration;

import com.example.merchantsimulator.entity.Condition;
import com.example.merchantsimulator.entity.Event;
import com.example.merchantsimulator.entity.Product;
import com.example.merchantsimulator.entity.Town;
import com.example.merchantsimulator.enums.Conditions;
import com.example.merchantsimulator.enums.Events;
import com.example.merchantsimulator.enums.Products;
import com.example.merchantsimulator.enums.Towns;
import com.example.merchantsimulator.repository.ConditionRepository;
import com.example.merchantsimulator.repository.EventRepository;
import com.example.merchantsimulator.repository.ProductRepository;
import com.example.merchantsimulator.repository.TownRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
public class InitDataBase {
    private final ProductRepository productRepository;
    private final EventRepository eventRepository;
    private final TownRepository townRepository;
    private final ConditionRepository conditionRepository;
    @Bean
    CommandLineRunner init(){
        return (args -> {
            if(productRepository.findAll().size() == 0) {
                List<Product> products = Stream.of(Products.values())
                        .map(e ->
                                Product.builder()
                                        .name(e.getName())
                                        .price(e.getPrice())
                                        .build())
                        .collect(Collectors.toList());
                productRepository.saveAll(products);
                List<Event> events = Stream.of(Events.values())
                        .map(e ->
                                Event.builder()
                                        .name(e.getName())
                                        .imageName(e.getImageName())
                                        .build())
                        .collect(Collectors.toList());
                eventRepository.saveAll(events);
                List<Town> towns = Stream.of(Towns.values())
                        .map(e ->
                                Town.builder()
                                        .name(e.getName())
                                        .leagues(e.getLeagues())
                                        .build())
                        .collect(Collectors.toList());
                townRepository.saveAll(towns);
                List<Condition> conditions = Stream.of(Conditions.values())
                        .map(e ->
                                Condition.builder()
                                        .name(e.getName())
                                        .coefficient(e.getCoefficient())
                                        .build())
                        .collect(Collectors.toList());
                conditionRepository.saveAll(conditions);
            }
        });
    }
}
