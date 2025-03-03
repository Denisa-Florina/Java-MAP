package src.trenuri.repository;

import src.trenuri.domain.TrainStation;

import java.sql.*;
import java.util.*;

public class TrainStationRepo {
    private final String url;
    private final String user;
    private final String password;

    public TrainStationRepo(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public List<TrainStation> getAllTrainStations() {
        List<TrainStation> trainStations = new ArrayList<>();
        String query = "SELECT * FROM train_stations";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String trainId = resultSet.getString("train_id");
                String departureCityId = resultSet.getString("departure_city_id");
                String destinationCityId = resultSet.getString("destination_city_id");
                trainStations.add(new TrainStation(trainId, departureCityId, destinationCityId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trainStations;
    }

}
