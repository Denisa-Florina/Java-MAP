package src.lab7.service;

import src.lab7.domain.Utilizator;
import src.lab7.domain.validators.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class ServiceLogin{
    private Service service;
    private Optional<Utilizator> utilizator;
    private ServiceController serviceController;

    public ServiceLogin(Service service, ServiceController serviceController) {
        this.service = service;
        this.serviceController = serviceController;
    }

    public Optional<Utilizator> getUtilizator() {
        return utilizator;
    }


    public void Login(String nume, String prenume, String password) {
        Optional<Long> userId = StreamSupport.stream(service.getUsers().spliterator(), false)
                .filter(user -> user.getFirstName().equals(prenume) && user.getLastName().equals(nume) && user.getPassword().equals(password))
                .map(Utilizator::getId)
                .findFirst();

        if (userId.isPresent()) {
            System.out.println("Login successful for user: " + nume + " " + prenume);
            utilizator = service.getUser(userId.get());
            return;
        }
        throw new ValidationException("Invalid credentials. Please check your username and password.");
    }

    public void Register(String nume, String prenume, String password) {
        if(nume == null || prenume == null || password == null) {
            throw new ValidationException("Invalid credentials. Please check your username and password.");
        }
        if(nume.isEmpty() || prenume.isEmpty() || password.isEmpty()) {
            throw new ValidationException("Invalid credentials. Please check your username and password.");
        }
        Optional<Long> userId = StreamSupport.stream(service.getUsers().spliterator(), false)
                .filter(user -> user.getFirstName().equals(prenume) && user.getLastName().equals(nume) && user.getPassword().equals(password))
                .map(Utilizator::getId)
                .findFirst();

        if (userId.isPresent()) {
            throw new ValidationException("Invalid credentials. Your username and password already exists.");
        }else {
            service.addUtilizator(new Utilizator(nume, prenume, password));
            System.out.println("Registration successful for user: " + nume + " " + prenume);
        }
    }

    public List<Utilizator> getUserFriendsRequest() {
        if(utilizator.isPresent()){
            return serviceController.getUserFriendsRequest(utilizator.get().getId());
        } else {
            throw new ValidationException("Login failed. Please check your username and password.");
        }
    }

    public List<Utilizator> getUserFriends(){
        if(utilizator.isPresent()){
            return service.getUserFriends(utilizator.get().getId());
        } else {
            throw new ValidationException("User data is missing. Please check your login.");
        }
    }

}
