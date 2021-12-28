package egorovIVSolution.controller;

import egorovIVSolution.model.CoffeeProduct;
import egorovIVSolution.service.CoffeeMachineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class CoffeeMachineController {

    private final CoffeeMachineService service;

    public CoffeeMachineController(CoffeeMachineService service) {
        this.service = service;
    }

    @GetMapping("/menu")
    public List<CoffeeProduct> priceList() {
        return service.priceList();
    }

    @GetMapping("/buy/{productName}")
    public CoffeeProduct buyProduct(@PathVariable String productName) {
        return service.buyProduct(productName);
    }

    @GetMapping("/resinfo")
    public int[] resourceInfo() {
        return service.resourceInfo();
    }
}