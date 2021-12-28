package egorovIVSolution.service;

import egorovIVSolution.DBConnection.JDBConnection;
import egorovIVSolution.exceptions.NotFoundException;
import egorovIVSolution.exceptions.OutOfResourceException;
import egorovIVSolution.model.CoffeeProduct;
import egorovIVSolution.repository.CoffeeMachineRepository;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class CoffeeMachineService {

    private final CoffeeMachineRepository repository;

    public CoffeeMachineService(CoffeeMachineRepository repository) {
        this.repository = repository;
    }

    public List<CoffeeProduct> priceList() {
        return repository.priceList();
    }

    public CoffeeProduct buyProduct(String productName) {
        CoffeeProduct cp = repository.buyProduct(productName);
        if (cp == null) {
            throw new OutOfResourceException();
        }
        else if (cp.getName().equals("error")) {
            throw new NotFoundException();
        }
        else {
            try {
                PreparedStatement prStatement = JDBConnection.connection.prepareStatement("INSERT INTO logs (time_and_date, drink, income) VALUES (?, ?, ?)");
                java.util.Date utilDate = new java.util.Date();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                prStatement.setDate(1,  sqlDate);
                prStatement.setString(2, cp.getName());
                prStatement.setInt(3, cp.getCost());
                prStatement.execute();
                prStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return cp;
        }
    }

    public int[] resourceInfo() {
        return repository.resourceInfo();
    }
}