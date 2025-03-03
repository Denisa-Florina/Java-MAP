package src.trenuri;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import src.trenuri.domain.City;
import src.trenuri.observer.Observer;
import src.trenuri.repository.CityRepo;
import src.trenuri.repository.TrainStationRepo;
import src.trenuri.service.Service;

import java.util.List;
import java.util.UUID;

public class TrenuriController implements Observer {
    @FXML private ComboBox<String> fromComboBox;
    @FXML private ComboBox<String> toComboBox;
    @FXML private CheckBox directRoutesOnlyCheckBox;
    @FXML private Button searchButton;
    @FXML private ListView<String> resultsListView;
    @FXML private Label Nother;
    private Service service;
    private String id;

    public void set(String id, Service service) {
        this.id=id;
        this.service=service;
        init();
    }

    public void init() {
        service.addObserver(this);
        update();
        List<City> cities = service.getAllCities();
        for (City city : cities) {
            fromComboBox.getItems().add(city.getName());
            toComboBox.getItems().add(city.getName());
        }

        searchButton.setOnAction(event -> searchRoutes());
    }

    private void searchRoutes() {
        String fromCity = fromComboBox.getValue();
        String toCity = toComboBox.getValue();
        boolean directRoutesOnly = directRoutesOnlyCheckBox.isSelected();

        if(fromCity.equals(toCity)) {
            resultsListView.getItems().clear();
            resultsListView.getItems().add("You are already there!");
            return;
        }

        if (fromCity == null || toCity == null) {
            resultsListView.getItems().clear();
            resultsListView.getItems().add("Select both cities!");
            return;
        }

        List<String> routes = service.findRoutes(fromCity, toCity, directRoutesOnly);
        service.addCautare(id,fromCity,toCity);
        service.notifyObservers();

        resultsListView.getItems().clear();
        if (routes.isEmpty()) {
            resultsListView.getItems().add("No routes found.");
        } else {
            resultsListView.getItems().addAll(routes);
        }
    }


    @Override
    public void update() {
        if(fromComboBox.getValue() != null && toComboBox.getValue()!=null) {
            String from = fromComboBox.getValue().toString();
            String to = toComboBox.getValue().toString();
            Integer cnt=service.nrPersoane(from,to);
            Nother.setText(cnt.toString() + " other user(s) are looking at the same route");
        }
        else {
            Nother.setText("");
        }
    }
}
