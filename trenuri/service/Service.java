package src.trenuri.service;

import src.trenuri.domain.Cautari;
import src.trenuri.domain.City;
import src.trenuri.domain.TrainStation;
import src.trenuri.observer.Observable;
import src.trenuri.observer.ObservableImplementat;
import src.trenuri.observer.Observer;
import src.trenuri.repository.CityRepo;
import src.trenuri.repository.TrainStationRepo;

import java.util.*;

public class Service extends ObservableImplementat {

    private CityRepo cityrepo;
    private TrainStationRepo trainRepo;
    private List<Cautari> cautari=new ArrayList<>();

    public Service(CityRepo cityrepo, TrainStationRepo trainRepo) {
        this.cityrepo = cityrepo;
        this.trainRepo = trainRepo;
    }

    public List<City> getAllCities() {
        return cityrepo.getAllCities();
    }


    public List<String> findRoutes(String fromCity, String toCity, boolean directRoutesOnly) {
        if (directRoutesOnly) {
            return findDirectRoutes(fromCity, toCity);
        } else {
            return getAllPossibleRoutes(fromCity, toCity);
        }
    }

    private List<String> findDirectRoutes(String fromCity, String toCity) {
        List<TrainStation> allStations = trainRepo.getAllTrainStations();
        List<String> directRoutes = new ArrayList<>();

        for (TrainStation station : allStations) {
            City departureCity = this.findById(station.getDepartureCityId());

            if (departureCity != null && departureCity.getName().equals(fromCity)) {
                String trainId = station.getTrainId();

                int stationsCount = calculateStationsCount(trainId, fromCity, toCity, allStations);
                if (stationsCount > 0) {
                    double price = stationsCount * 0.10;
                    directRoutes.add(fromCity + "-Train" + trainId + "->" + toCity + " (Price: " + price + " RON)");
                }
            }
        }

        return directRoutes;
    }

    private int calculateStationsCount(String trainId, String fromCity, String toCity, List<TrainStation> allStations) {
        String currentCity = fromCity;
        int stationsCount = 0;
        while (!currentCity.equals(toCity)) {
            boolean foundNext = false;

            for (TrainStation station : allStations) {
                City departureCity = this.findById(station.getDepartureCityId());
                City destinationCity = this.findById(station.getDestinationCityId());

                if (Objects.equals(station.getTrainId(), trainId) && departureCity != null && destinationCity != null
                        && departureCity.getName().equals(currentCity)) {
                    currentCity = destinationCity.getName();
                    foundNext = true;
                    stationsCount++;
                    break;
                }
            }

            if (!foundNext) {
                return 0;
            }
        }
        return stationsCount;
    }


    public City findById(String id) {
        for (City city : cityrepo.getAllCities()) {
            if (Objects.equals(city.getId(), id)) {
                return city;
            }
        }
        return null;
    }

    public List<String> getAllPossibleRoutes(String fromCity, String toCity) {
        List<String> routes = new ArrayList<>();
        List<String> currentRoute = new ArrayList<>();
        backtrack(fromCity, toCity, currentRoute, routes);
        return routes;
    }

    private void backtrack(String currentCity, String toCity, List<String> currentRoute, List<String> routes) {
        if (currentCity.equals(toCity)) {
            double totalPrice = currentRoute.size() * 0.10;
            routes.add(String.join("->", currentRoute) + " (Total Price: " + totalPrice + ")");
            return;
        }

        for (TrainStation station : trainRepo.getAllTrainStations()) {
            City departureCity = this.findById(station.getDepartureCityId());
            City destinationCity = this.findById(station.getDestinationCityId());

            if (departureCity != null && departureCity.getName().equals(currentCity)) {
                String trainRoute = currentCity + "-Train" + station.getTrainId() + "->" + destinationCity.getName();


                if (currentRoute.contains(trainRoute)) {
                    continue;
                }

                currentRoute.add(trainRoute);
                backtrack(destinationCity.getName(), toCity, currentRoute, routes);
                currentRoute.remove(currentRoute.size() - 1);
            }
        }
    }


    public void addCautare(String idOm, String idDeparture, String idDestination) {
        boolean found = false;

        for (Cautari caut : cautari) {
            if (caut.getId().equals(idOm)) {
                caut.setIdDeparture(idDeparture);
                caut.setIdDestination(idDestination);
                found = true;
                break;
            }
        }

        if (!found) {
            Cautari c = new Cautari(idOm, idDeparture, idDestination);
            cautari.add(c);
        }
    }

    public Integer nrPersoane(String from,String to){
        Integer cnt=0;
        for(Cautari c: cautari)
        {
            if(c.getIdDeparture().equals(from) && c.getIdDestination().equals(to)) {
                cnt++;
            }
        }
        return cnt-1;
    }

}
