package egorovIVSolution.model;

public class CoffeeProduct {

    private String name;
    private int cost;

    public CoffeeProduct(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return this.name;
    }

    public int getCost() {
        return this.cost;
    }
}