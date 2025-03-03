package src.trenuri.repository;

import src.trenuri.domain.City;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityRepo {
    private final String url;
    private final String user;
    private final String password;

    public CityRepo(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public List<City> getAllCities() {
        List<City> cities = new ArrayList<>();
        String query = "SELECT * FROM cities";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                cities.add(new City(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }

}
