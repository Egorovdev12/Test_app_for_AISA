package egorovIVSolution.repository;

import egorovIVSolution.model.CoffeeProduct;

import java.util.List;

public interface CoffeeMachineRepository {
    List<CoffeeProduct> priceList();
    CoffeeProduct buyProduct(String productName);
    int[] resourceInfo();
}