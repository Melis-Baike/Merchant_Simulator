package com.example.merchantsimulator.controller;

import com.example.merchantsimulator.dto.CustomDto;
import com.example.merchantsimulator.dto.InfoDto;
import com.example.merchantsimulator.form.DealerForm;
import com.example.merchantsimulator.service.DealerService;
import com.example.merchantsimulator.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dealers")
public class DealerController {
    private final DealerService dealerService;
    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<String> createDealer(@RequestBody DealerForm form){
        return new ResponseEntity<>(dealerService.createDealer(form), HttpStatus.OK);
    }

    @GetMapping("/towns")
    public ResponseEntity<String> setTown(@RequestParam("townName") String townName){
        return new ResponseEntity<>(dealerService.choiceTown(townName), HttpStatus.OK);
    }

    @PostMapping("/way")
    public CustomDto letsHitTheRoad(){
        return dealerService.letsHitTheRoad();
    }

    @GetMapping("/products/purchase")
    public ResponseEntity<String> buyProduct(@RequestParam("productName") String productName){
        return new ResponseEntity<>(productService.buyProduct(productName), HttpStatus.OK);
    }

    @GetMapping("/info")
    public Optional<InfoDto> getInfo(){
        return dealerService.getInfo();
    }
}
