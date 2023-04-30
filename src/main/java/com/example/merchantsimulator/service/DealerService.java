package com.example.merchantsimulator.service;

import com.example.merchantsimulator.dto.CustomDto;
import com.example.merchantsimulator.dto.InfoDto;
import com.example.merchantsimulator.entity.*;
import com.example.merchantsimulator.enums.Events;
import com.example.merchantsimulator.form.DealerForm;
import com.example.merchantsimulator.repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Data
public class DealerService {
    private final DealerRepository dealerRepository;
    private final CartRepository cartRepository;
    private final EventRepository eventRepository;
    private final TownRepository townRepository;
    private final ProductRepository productRepository;
    private final CartAndProductsRepository cartAndProductsRepository;
    private final ConditionRepository conditionRepository;
    private final static int DEFAULTCARTLOADCAPACITY = 1000;

    public String createDealer(@RequestBody DealerForm form){
        List<Dealer> dealers = dealerRepository.findAll();
        if(dealers.size() == 0) {
            Cart cart = Cart.builder()
                    .speed(3)
                    .loadCapacity(1000)
                    .currentEventId(0L)
                    .build();
            cartRepository.save(cart);
            System.out.println(form.getName());
            Dealer dealer = Dealer.builder()
                    .name(form.getName())
                    .cartId(cartRepository.findTopByOrderByIdDesc().get().getId())
                    .townId(0L)
                    .townsLeagues(0)
                    .money(100000)
                    .build();
            dealerRepository.save(dealer);
            System.out.println("Cart info: " + cart);
            System.out.println("Dealer info: " + dealer);
            return dealer.toString();
        } else {
            return "The dealer already exists. His name is " + dealers.get(0).getName();
        }
    }

    public String choiceTown(String townName){
        Dealer dealer = dealerRepository.findById(1L).get();
        if(dealer.getTownId() == 0) {
            Town town = townRepository.findByName(townName).get();
            dealer.setTownId(town.getId());
            dealerRepository.setTownIdById(dealer.getTownId(), dealer.getId());
            dealer.setTownsLeagues(town.getLeagues());
            dealerRepository.setTownsLeaguesById(dealer.getTownsLeagues(), dealer.getId());
            return town.getName();
        }
        return "You have already had selected town. It is " + townRepository.findById(dealer.getTownId()).get().getName();
    }

    public CustomDto letsHitTheRoad(){
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        Optional<Dealer> dealer = dealerRepository.findById(1L);
        if(dealer.isPresent()) {
            if (dealer.get().getTownsLeagues() > 0) {
                Optional<Cart> cart = cartRepository.findById(dealer.get().getCartId());
                List<Event> eventList = eventRepository.findAll();
                Long randomEvent = (long) rnd.nextInt(eventList.size()) + 1;
                Event chosenEvent = eventList.stream().filter(e -> e.getId().equals(randomEvent)).findFirst().get();
                Events events = Stream.of(Events.values()).filter(e -> e.getName().equals(chosenEvent.getName())).findFirst().get();
                String eventInfo = events.info(this);
                cart.get().setCurrentEventId(chosenEvent.getId());
                cartRepository.setCurrentEventIdById(cart.get().getCurrentEventId(), cart.get().getId());
                sb.append(eventInfo);
                int difference = events.getFiniteRange() - events.getInitRange();
                int formula;
                if (difference > 0) {
                    formula = rnd.nextInt(difference + 1) + events.getInitRange();
                } else if (difference < 0) {
                    formula = -(rnd.nextInt(Math.abs(difference) + 1)) + events.getInitRange();
                } else {
                    formula = events.getInitRange();
                }
                int leagues = cart.get().getSpeed() + formula;
                dealer.get().setTownsLeagues(dealer.get().getTownsLeagues() - leagues);
                dealerRepository.setTownsLeaguesById(dealer.get().getTownsLeagues(), dealer.get().getId());
                sb.append(". You have gone ").append(leagues).append(" leagues");
                if(dealerRepository.findById(dealer.get().getId()).get().getTownsLeagues() <= 0){
                    List<CartAndProducts> cartAndProducts = cartAndProductsRepository.findByCartId(cart.get().getId());
                    double earnings = cartAndProducts.stream().map(e -> (e.getWeightOfProduct() *
                            productRepository.findById(e.getProductId()).get().getPrice()) *
                            conditionRepository.findById(e.getProductConditionId()).get().getCoefficient())
                            .collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).sum();
                    dealer.get().setMoney(dealer.get().getMoney() + earnings);
                    dealerRepository.setMoneyById(dealer.get().getMoney(), dealer.get().getId());
                    sb.append(". Dealer has arrived in the town. He earned ").append(earnings).append("$");
                    Town town = townRepository.findById(dealer.get().getTownId()).get();
                    dealer.get().setTownId(0L);
                    cartAndProductsRepository.deleteAllByCartId(cart.get().getId());
                    cart.get().setLoadCapacity(DEFAULTCARTLOADCAPACITY);
                    cart.get().setCurrentEventId(0L);
                    cartRepository.setCurrentEventIdById(cart.get().getCurrentEventId(), cart.get().getId());
                    dealer.get().setTownsLeagues(0);
                    dealerRepository.setTownsLeaguesById(dealer.get().getTownsLeagues(), dealer.get().getId());
                    dealerRepository.setTownIdById(dealer.get().getTownId(), dealer.get().getId());
                    return CustomDto.from("../static/start.jpg", town.getName(), String.valueOf(sb));
                }
                return CustomDto.from(events.getImageName(), events.getName(), String.valueOf(sb));
            } else {
                return CustomDto.from("","Error", "You have already arrived in the town");
            }
        } else {
            return CustomDto.from("","Error", "Create dealer");
        }
    }

    public Optional<InfoDto> getInfo(){
        Optional<Dealer> dealer = dealerRepository.findById(1L);
        if(dealer.isPresent()) {
            Cart cart = cartRepository.findById(dealer.get().getId()).get();
            if(cart.getCurrentEventId() != 0) {
                Event event = eventRepository.findById(cart.getCurrentEventId()).get();
                String description = "Current event is " + event.getName();
                return Optional.of(InfoDto.from(event.getImageName(), event.getName(), description));
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
