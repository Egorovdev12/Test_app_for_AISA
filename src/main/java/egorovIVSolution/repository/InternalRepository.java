package egorovIVSolution.repository;

import egorovIVSolution.model.CoffeeProduct;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InternalRepository implements CoffeeMachineRepository {

    // Набор констант для парсинга
    private static final String MAIN_KEY = "productList";
    private static final String SUB_KEY1 = "name";
    private static final String SUB_KEY2 = "cost";
    private static final String RESOURCES_FILE_NAME = "coffeeResources.txt";
    private static final String SETTINGS_FILE_NAME = "settings.json";
    private static int CAPACITY = 4;

    // Хранилище текущих товаров
    private List<CoffeeProduct> productList;

    // Абстрактные ресурсы, необходимые для приготовления напитка в условных единицах
    private int waterResource;
    private int milkResource;
    private int coffeeResource;
    private int chocoResource;

    public InternalRepository() {
        this.productList = new CopyOnWriteArrayList<>();
        parseCurrentMenuFromSettings();
        downloadResourceData();
    }

    // Метод возвращает текущий список товаров
    @Override
    public List<CoffeeProduct> priceList() {
        return productList;
    }

    // Если ресурсов для приготовления напитка достаточно - вернет CoffeeProduct, если нет - ошибку
    @Override
    public CoffeeProduct buyProduct(String productName) {
      return defineDrink(productName);
    }

    // Метод возвращает массив состояний ингридиентов порядке, описанном в классе
    @Override
    public int[] resourceInfo() {
        int[] arr = new int[CAPACITY];
        arr[0] = waterResource;
        arr[1] = milkResource;
        arr[2] = coffeeResource;
        arr[3] = chocoResource;
        return arr;
    }

    // Метод решает что будем готовить и готовит, если нижний метод, решает что можно
    private CoffeeProduct defineDrink(String productName) {
        boolean readyToMake = false;
        int nWater, nMilk, nCoffee, nChoco;
        if (productName.equals("Espresso")) {
            // Эспрессо 3уе кофе, 1уе воды
            nWater = 1; nMilk = 0; nCoffee = 3; nChoco = 0;
            readyToMake = canMake(nWater, nMilk, nCoffee, nChoco);
        }
        else if (productName.equals("Americano")) {
            // Американо 3уе кофе, 5уе воды
            nWater = 5; nMilk = 0; nCoffee = 3; nChoco = 0;
            readyToMake = canMake(nWater, nMilk, nCoffee, nChoco);
        }
        else if (productName.equals("Cappuccino")) {
            // Капучино 3уе кофе, 3уе воды, 2уе молока
            nWater = 3; nMilk = 2; nCoffee = 3; nChoco = 0;
            readyToMake = canMake(nWater, nMilk, nCoffee, nChoco);
        }
        else if (productName.equals("Latte")) {
            // Латте 3уе кофе, 5уе молока, 1уе воды
            nWater = 1; nMilk = 5; nCoffee = 3; nChoco = 0;
            readyToMake = canMake(nWater, nMilk, nCoffee, nChoco);
        }
        else if (productName.equals("Choco")) {
            // Горячий шоколад 1уе воды, 1уе шоколада, 3уе молока
            nWater = 1; nMilk = 3; nCoffee = 0; nChoco = 1;
            readyToMake = canMake(nWater, nMilk, nCoffee, nChoco);
        }
        else {
            return new CoffeeProduct("error", 404);
        }

        if (readyToMake) {
            if (productName.equals("Choco")) {
                productName = "Hot chocolate";
            }
            makeDrink(nWater, nMilk, nCoffee, nChoco);

            for (int i = 0; i < productList.size(); i++) {
               if (productList.get(i).getName().equals(productName)) {
                   return productList.get(i);
               }
            }
        }
        else {
            return null;
        }
        return null;
    }

    // Решает, хватает ли нам ингридиентов для готовки напитка
    public boolean canMake(int water, int milk, int coffee, int choco) {
        return (waterResource >= water) && (milkResource >= milk) && (coffeeResource >= coffee) && (chocoResource >= choco);
    }

    // Готовит напиток (по факту просто вычитает ресуерсы)
    public void makeDrink(int water, int milk, int coffee, int choco) {
        waterResource = waterResource - water;
        milkResource = milkResource - milk;
        coffeeResource = coffeeResource - coffee;
        chocoResource = chocoResource - choco;
    }

    // Метод загружает данные в программу из текстового файла
    private void parseCurrentMenuFromSettings() {
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(SETTINGS_FILE_NAME));
            JSONArray jsonArray = (JSONArray) jsonObject.get(MAIN_KEY);
            List<CoffeeProduct> cpl = new LinkedList<>();

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jObj = (JSONObject) jsonArray.get(i);
                cpl.add(new CoffeeProduct(jObj.get(SUB_KEY1).toString(), Integer.parseInt(jObj.get(SUB_KEY2).toString())));
            }
            productList.addAll(cpl);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Метод, эмулирующий работу кофейного автомата по контролю ресурсов на уровне hardware
    private void downloadResourceData() {
        File resourceFile = new File(RESOURCES_FILE_NAME);
        try (FileReader fileReader = new FileReader(resourceFile);
             BufferedReader reader = new BufferedReader(fileReader)) {

            int[] resArray = new int[CAPACITY];
            String line;

            for (int i = 0; i < CAPACITY; i++) {
                if ((line = reader.readLine()) != null) {
                    resArray[i] = Integer.parseInt(line);
                }
            }
            waterResource = resArray[0];
            milkResource = resArray[1];
            coffeeResource = resArray[2];
            chocoResource = resArray[3];
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}