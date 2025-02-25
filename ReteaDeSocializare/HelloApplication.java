package src.lab7;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import src.lab7.connection.Connectionn;
import src.lab7.domain.validators.UtilizatorValidator;
import src.lab7.domain.validators.ValidationException;
import src.lab7.domain.validators.ValidationFriendship;
import src.lab7.repository.database.FriendshipDatabaseRepository;
import src.lab7.repository.database.MessageDataBaseRepository;
import src.lab7.repository.database.RequestDataBaseRepository;
import src.lab7.repository.database.UtilizatorDatabaseRepository;
import src.lab7.service.Service;
import src.lab7.service.ServiceCommunity;
import src.lab7.service.ServiceLogin;
import src.lab7.service.ServiceController;

import java.io.IOException;
import java.sql.Connection;


public class HelloApplication extends Application {

    UtilizatorDatabaseRepository utilizatorDatabaseRepository;
    FriendshipDatabaseRepository friendshipDatabaseRepository;
    MessageDataBaseRepository messageDataBaseRepository;
    RequestDataBaseRepository requestDataBaseRepository;
    Service service;
    ServiceCommunity serviceCommunity;
    ServiceLogin serviceLogin;
    ServiceController serviceController;
    Connection connectionn;

    @Override
    public void start(Stage stage) throws IOException {
        connectionn = new Connectionn().getConnection();
        utilizatorDatabaseRepository = new UtilizatorDatabaseRepository(new UtilizatorValidator(), connectionn);
        friendshipDatabaseRepository = new FriendshipDatabaseRepository(new ValidationFriendship(), connectionn);
        messageDataBaseRepository = new MessageDataBaseRepository(connectionn);
        requestDataBaseRepository = new RequestDataBaseRepository(connectionn);
        service = new Service(utilizatorDatabaseRepository, friendshipDatabaseRepository, messageDataBaseRepository);
        serviceCommunity = new ServiceCommunity(service);
        serviceController = new ServiceController(requestDataBaseRepository, service);


        GridPane gr = new GridPane();
        gr.setPadding(new Insets(20, 20, 20, 10));
        gr.setHgap(10);
        gr.setVgap(10);
        gr.setAlignment(Pos.CENTER);


        Label welcomeLabel = new Label("Welcome to Social Network!");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: rgba(14,58,74,0.85); -fx-padding: 10px;");
        gr.add(welcomeLabel, 0, 0, 2, 1);


        Label firstNameLabel = new Label("First Name: ");
        Label lastNameLabel = new Label("Last Name: ");
        Label passwordLabel = new Label("Password: ");

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        PasswordField passwordField = new PasswordField();

        firstNameField.setPromptText("Enter your first name");
        lastNameField.setPromptText("Enter your last name");
        passwordField.setPromptText("Enter your password");

        gr.add(firstNameLabel, 0, 1);
        gr.add(firstNameField, 1, 1);
        gr.add(lastNameLabel, 0, 2);
        gr.add(lastNameField, 1, 2);
        gr.add(passwordLabel, 0, 3);
        gr.add(passwordField, 1, 3);

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");
        Button exitBtn = new Button("Exit");
        loginBtn.setStyle("-fx-background-color: #541b75; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");
        registerBtn.setStyle("-fx-background-color: #873483; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");
        exitBtn.setStyle("-fx-background-color: #bf105f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");


        Region spacer = new Region();
        spacer.setMinHeight(40);
        gr.add(spacer, 0, 4, 3, 2);


        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(loginBtn,registerBtn,exitBtn);
        gr.add(hbox,0,5,2,1);

        loginBtn.setOnAction(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String password = passwordField.getText();

            serviceLogin = new ServiceLogin(service, serviceController);
            try {
                serviceLogin.Login(firstName, lastName, password);
            }
            catch (ValidationException de){
                showErrorAlert(de.getMessage());
                return;
            }

            //stage.close();
            HelloUser hu = new HelloUser();
            hu.set(serviceLogin,service, serviceController);
            try {
                hu.start(new Stage());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }


        });
        registerBtn.setOnAction(e -> {
            serviceLogin = new ServiceLogin(service, serviceController);
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String password = passwordField.getText();
            serviceLogin.Register(firstName,lastName,password);
            serviceLogin = null;

        });
        exitBtn.setOnAction(e -> {
            stage.close();
        });

        Scene scene = new Scene(gr,400,250);
        stage.setTitle("SocialNetwork");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText("Login failed");
        alert.setContentText(message);

        alert.showAndWait();
    }
}