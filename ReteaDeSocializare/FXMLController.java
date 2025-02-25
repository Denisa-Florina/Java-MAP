package src.lab7;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import src.lab7.domain.Friendship;
import src.lab7.domain.FriendshipRequest;
import src.lab7.domain.Message;
import src.lab7.domain.Utilizator;
import src.lab7.domain.validators.ValidationException;
import src.lab7.events.ChangeEventType;
import src.lab7.events.EntityChangeEvent;
import src.lab7.service.Service;
import src.lab7.service.ServiceLogin;
import src.lab7.service.ServiceController;
import src.lab7.utils.Observer;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FXMLController implements Observer<EntityChangeEvent> {

    private ServiceLogin serviceLogin;
    private Service service;
    private ServiceController serviceController;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private ListView<String> friendsListView;
    @FXML private TextField friendFirstNameField;
    @FXML private TextField friendLastNameField;
    @FXML private TextField friendFromField;
    @FXML private Button deleteButton;
    @FXML private ListView<String> friendRequestListView;
    @FXML private TextField requestFirstNameField;
    @FXML private TextField requestLastNameField;
    @FXML private Button denyButton;
    @FXML private Button acceptButton;
    @FXML private ListView<String> userListView;
    @FXML private TextField userFirstNameField;
    @FXML private TextField userLastNameField;
    @FXML private Button addButton;


    @FXML private TextArea textArea;
    @FXML private TextField messageField;
    @FXML private Button sendButton;
    @FXML private MenuButton friendsMenuButton;

    private Utilizator utilizatorSelectat;
    private Utilizator utilizatorReply;
    private Message messageSelectat;

    @FXML private TextArea textArea2;
    @FXML private TextField replyField;
    @FXML private Button replyButton;
    @FXML private MenuButton friendsMenu;
    @FXML private MenuButton messagesMenu;

    @FXML private TextField PageField;
    int page;
    int maxPage;
    int limitPage;

    @FXML
    private void handleBack(){
        if(page > 1){
            page--;
            loadFriendsPage();
            PageField.setText(String.valueOf(page));
        }
        else{
            showErrorAlert("This is the last page");
        }
    }

    @FXML
    private void handleNext(){
        if(page < maxPage) {
            page++;
            loadFriendsPage();
            PageField.setText(String.valueOf(page));
        }
        else{
            showErrorAlert("This is the last page");
        }
    }

    private void loadFriendsPage(){
        List<Utilizator> friends = service.getUserFriendsPaginated(serviceLogin.getUtilizator().get().getId(), page, limitPage);
        ObservableList<String> friendsNames = StreamSupport.stream(friends.spliterator(), false)
                .map(friend ->  friend.getFirstName() + " " + friend.getLastName())
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        friendsListView.setItems(friendsNames);
    }

    private void handleReplyButtonClick() {
        String replyMessage = replyField.getText();

        if (replyMessage != null && !replyMessage.trim().isEmpty()) {
            textArea.appendText("Reply: " + replyMessage + "\n");
            replyField.clear();
        } else {
            System.out.println("Reply cannot be empty!");
        }
    }

    public void loadMessageList(){
        textArea.clear();
        Iterable<Message> messages = service.getMessages();
        if(utilizatorSelectat != null) {
                StreamSupport.stream(messages.spliterator(), false)
                        .filter(message ->
                                (message.getId1().equals(serviceLogin.getUtilizator().get().getId()) &&
                                message.getId2().equals(utilizatorSelectat.getId())) ||
                                        (message.getId1().equals(utilizatorSelectat.getId()) &&
                                                message.getId2().equals(serviceLogin.getUtilizator().get().getId()))
                        )
                        .forEach(message -> {
                            if (message.getId1().equals(serviceLogin.getUtilizator().get().getId()) && message.getId2().equals(utilizatorSelectat.getId())) {
                                //daca eu vreau sa trimit un mesaj catre persoana selectata
                                textArea.appendText(utilizatorSelectat.getFirstName() + ": " + message.getMessage() + "\n");
                                if (message.getReply() != null) {
                                    textArea.appendText("               You reply: " + message.getReply() + "\n");
                                }
                            } else if (message.getId1().equals(utilizatorSelectat.getId()) && message.getId2().equals(serviceLogin.getUtilizator().get().getId())) {
                                //persoana selectata vrea sa-mi trimita mie un mesaj
                                textArea.appendText("You: " + message.getMessage() + "\n");
                                if (message.getReply() != null) {
                                    textArea.appendText("               " + utilizatorSelectat.getFirstName() + " reply: " + message.getReply() + "\n");
                                }
                            }
                        });
        }
    }

    public void handleSendButton() {
        String message = messageField.getText();
        if (message != null && !message.trim().isEmpty()) {
            serviceController.addMessage(serviceLogin.getUtilizator().get().getId(),utilizatorSelectat.getId(),message);
            messageField.clear();
        }
    }

    public void setFriend2Menu() {
        List<Utilizator> utilizatori = serviceLogin.getUserFriends();
        ObservableList<MenuItem> friendMenuItems = utilizatori
                .stream()
                .map(friend ->{
                    MenuItem menuItem = new MenuItem(friend.getFirstName() + " " + friend.getLastName());
                    menuItem.setOnAction(event -> handleFriendSelection2(friend));
                    return menuItem;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        friendsMenu.getItems().setAll(friendMenuItems);
    }

    public void setFriendMenu() {
        List<Utilizator> utilizatori = serviceLogin.getUserFriends();
        ObservableList<MenuItem> friendMenuItems = utilizatori
                .stream()
                .map(friend ->{
                    MenuItem menuItem = new MenuItem(friend.getFirstName() + " " + friend.getLastName());
                    menuItem.setOnAction(event -> handleFriendSelection(friend));
                    return menuItem;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        friendsMenuButton.getItems().setAll(friendMenuItems);
    }

    public void setMessageMenu() {
        Iterable<Message> messages = service.getMessages();
        ObservableList<MenuItem> MessageMenuItems = StreamSupport.stream(messages.spliterator(), false)
                .filter(message -> message.getId2().equals(utilizatorReply.getId()) && message.getId1().equals(serviceLogin.getUtilizator().get().getId()))
                .map(message ->{
                    MenuItem menuItem = new MenuItem(message.getMessage());
                    menuItem.setOnAction(event -> handleMessage(message));
                    return menuItem;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        messagesMenu.getItems().setAll(MessageMenuItems);
    }

    private void handleMessage(Message message) {
        textArea2.setText(message.getMessage());
        messageSelectat = message;
    }

    private void handleFriendSelection(Utilizator friend) {
        messageField.clear();
        messageField.setPromptText("Send to " + friend.getFirstName() + " " + friend.getLastName());
        utilizatorSelectat = friend;
        loadMessageList();
    }

    private void handleFriendSelection2(Utilizator friend) {
        replyField.clear();
        replyField.setPromptText("Reply to " + friend.getFirstName() + " " + friend.getLastName());
        utilizatorReply = friend;
        setMessageMenu();
    }

    public void setServiceLogin(ServiceLogin serviceLogin, Service service, ServiceController serviceController) {
        this.serviceLogin = serviceLogin;
        this.service = service;
        this.serviceController = serviceController;
        serviceController.addObserver(this);
    }

    public void initialize() {
        try {
            page = 1;
            limitPage = 3;
            maxPage = (serviceLogin.getUserFriends().size() + limitPage - 1) / limitPage ;
            PageField.setText(String.valueOf(page));
            loadFriendsPage();
            loadRequestsList();
            loadUserList();
            setFriendMenu();
            setFriend2Menu();

            firstNameField.setText(serviceLogin.getUtilizator().get().getFirstName());
            lastNameField.setText(serviceLogin.getUtilizator().get().getLastName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadRequestsList() {
        if(serviceController != null) {
            Iterable<FriendshipRequest> friends = serviceController.getFriendshipRequests();
            ObservableList<String> friendsNames = FXCollections.observableArrayList();

            for (FriendshipRequest friend : friends) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                if (Objects.equals(friend.getUserIdR2(), serviceLogin.getUtilizator().get().getId())) {
                    friendsNames.add(String.valueOf(service.getUser(friend.getUserIdR1()).get().getFirstName() + " " + service.getUser(friend.getUserIdR1()).get().getLastName()) + " " + friend.getDateTimeR().format(formatter));
                }
                if (Objects.equals(friend.getUserIdR1(), serviceLogin.getUtilizator().get().getId())) {
                    friendsNames.add(String.valueOf(service.getUser(friend.getUserIdR2()).get().getFirstName() + " " + service.getUser(friend.getUserIdR2()).get().getLastName()) + "   PENDING...");
                }
            }
            friendRequestListView.setItems(friendsNames);
        }
        else{
            System.out.println("Something went wrong, please reconnect!");
        }
    }

    public void loadFriendsList() {
        if(serviceLogin != null ) {
            List<Utilizator> friends = serviceLogin.getUserFriends();
            ObservableList<String> friendsNames = StreamSupport.stream(friends.spliterator(), false)
                    .map(friend -> friend.getFirstName() + " " + friend.getLastName())
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            friendsListView.setItems(friendsNames);
        }
        else{
            System.out.println("Something went wrong, please reconnect!");
        }
    }

    public void loadUserList() {
        if(service != null) {
            Iterable<Utilizator> friends = service.getUsers();
            ObservableList<String> friendsNames = StreamSupport.stream(friends.spliterator(), false)
                    .map(friend -> friend.getFirstName() + " " + friend.getLastName())
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            userListView.setItems(friendsNames);
        }
        else{
            System.out.println("Something went wrong, please reconnect!");
        }
    }

    @FXML
    private void handleAddButtonClick() {
        String firstName = userFirstNameField.getText();
        String lastName = userLastNameField.getText();

        Optional<Long> userId = StreamSupport.stream(service.getUsers().spliterator(), false)
                .filter(user -> user.getFirstName().equals(firstName) && user.getLastName().equals(lastName))
                .map(Utilizator::getId)
                .findFirst();

        if (userId.isPresent()) {
            try {
                boolean friendshipExists = StreamSupport.stream(service.getFriendships().spliterator(), false)
                        .anyMatch(friendship -> (friendship.getUserId1().equals(serviceLogin.getUtilizator().get().getId()) && friendship.getUserId2().equals(userId.get())) ||
                                (friendship.getUserId1().equals(userId.get()) && friendship.getUserId2().equals(serviceLogin.getUtilizator().get().getId())));

                if (friendshipExists) {
                    showErrorAlert("You are already friends or have a pending friendship request.");
                    return;
                }
                if(serviceLogin.getUtilizator().get().getId().equals(userId.get())) {
                    showErrorAlert("You cannot be friend with yourself!");
                    return;
                }
                serviceController.addFriend(serviceLogin.getUtilizator().get().getId(), userId.get());
                loadUserList();
            }
            catch (Exception e) {
                showErrorAlert(e.getMessage());
            }
        }else{
            showErrorAlert("Something went wrong, please reconnect!");
        }
    }

    @FXML
    private void handleDeleteAction(){
        String firstName = friendFirstNameField.getText();
        String lastName = friendLastNameField.getText();

        Optional<Long> userId = StreamSupport.stream(service.getUsers().spliterator(), false)
                .filter(user -> user.getFirstName().equals(firstName) && user.getLastName().equals(lastName))
                .map(Utilizator::getId)
                .findFirst();

        if (userId.isPresent()) {
            try {
                serviceController.removeFriendship(serviceLogin.getUtilizator().get().getId(), userId.get());
                loadFriendsList();
                loadUserList();
            }
            catch (Exception e) {
                showErrorAlert(e.getMessage());
            }
        }
        else{
            showErrorAlert("Something went wrong, please reconnect!");
        }
    }

    @FXML
    private void handledenyButtonAction(){
        String firstName = requestFirstNameField.getText();
        String lastName = requestLastNameField.getText();

        Optional<Long> userId = StreamSupport.stream(service.getUsers().spliterator(), false)
                .filter(user -> user.getFirstName().equals(firstName) && user.getLastName().equals(lastName))
                .map(Utilizator::getId)
                .findFirst();

        Optional<Long> utilizator = StreamSupport.stream(serviceController.getFriendshipRequests().spliterator(),false)
                .filter(friendship -> (Objects.equals(friendship.getUserIdR1(), userId.get()) && Objects.equals(friendship.getUserIdR2(), serviceLogin.getUtilizator().get().getId()))
                        || (Objects.equals(friendship.getUserIdR1(), serviceLogin.getUtilizator().get().getId()) && Objects.equals(friendship.getUserIdR2(), userId.get())))
                .map(FriendshipRequest::getId)
                .findFirst();

        if (utilizator.isPresent()) {
            try {
                serviceController.respond(serviceController.getFriendshipRequest(utilizator.get()).get(), "deny");

            }
            catch (Exception e) {
                showErrorAlert(e.getMessage());
            }
        }
        else{
            showErrorAlert("Something went wrong, please reconnect!");
        }
    }

    @FXML
    private void handleaceptButtonAction(){
        String firstName = requestFirstNameField.getText();
        String lastName = requestLastNameField.getText();

        Optional<Long> userId = StreamSupport.stream(service.getUsers().spliterator(), false)
                .filter(user -> user.getFirstName().equals(firstName) && user.getLastName().equals(lastName))
                .map(Utilizator::getId)
                .findFirst();

        Optional<Long> utilizator = StreamSupport.stream(serviceController.getFriendshipRequests().spliterator(),false)
                .filter(friendship -> (Objects.equals(friendship.getUserIdR1(), userId.get()) && Objects.equals(friendship.getUserIdR2(), serviceLogin.getUtilizator().get().getId()))
                        || (Objects.equals(friendship.getUserIdR1(), serviceLogin.getUtilizator().get().getId()) && Objects.equals(friendship.getUserIdR2(), userId.get())))
                .map(FriendshipRequest::getId)
                .findFirst();

        if (utilizator.isPresent()) {
            try {
                serviceController.respond(serviceController.getFriendshipRequest(utilizator.get()).get(), "accept");
            }
            catch (Exception e) {
                showErrorAlert(e.getMessage());
            }
        }
        else{
            showErrorAlert("Something went wrong, please reconnect!");
        }
    }

    @FXML
    private void handleListViewClick() {
        String selectedFriend = friendsListView.getSelectionModel().getSelectedItem();

        if (selectedFriend != null) {
            String[] splited = selectedFriend.split(" ");
            String firstName = splited[0];
            String lastName = splited[1];
            friendFirstNameField.setText(firstName);
            friendLastNameField.setText(lastName);

            Optional<Long> userId = Optional.ofNullable(StreamSupport.stream(service.getUsers().spliterator(), false)
                    .filter(user -> user.getFirstName().equals(firstName) && user.getLastName().equals(lastName))
                    .map(Utilizator::getId)
                    .findFirst()
                    .orElseThrow(() -> new ValidationException("The ID cannot be found.")));

            Optional<LocalDateTime> utilizator = StreamSupport.stream(service.getFriendships().spliterator(),false)
                    .filter(friendship -> (Objects.equals(friendship.getUserId1(), userId.get()) && Objects.equals(friendship.getUserId2(), serviceLogin.getUtilizator().get().getId()))
                            || (Objects.equals(friendship.getUserId1(), serviceLogin.getUtilizator().get().getId()) && Objects.equals(friendship.getUserId2(), userId.get())))
                    .map(Friendship::getDateTime)
                    .findFirst();

            friendFromField.setText(String.valueOf(utilizator.get().getYear()));
        }
    }

    @FXML
    private void handleReplyButton(){
        String messaje = replyField.getText();
        messageSelectat.setReply(messaje);
        serviceController.setReply(messageSelectat);
        loadMessageList();
        textArea2.clear();
        replyField.clear();
    }

    @FXML
    private void handleRequestViewClick() {
        String selectedFriend = friendRequestListView.getSelectionModel().getSelectedItem();

        if (selectedFriend != null) {
            String[] splited = selectedFriend.split(" ");
            String firstName = splited[0];
            String lastName = splited[1];
            requestFirstNameField.setText(firstName);
            requestLastNameField.setText(lastName);

        }
    }

    @FXML
    private void handleUserViewClick(){
        String selectedFriend = String.valueOf(userListView.getSelectionModel().getSelectedItem());

        if (selectedFriend != null) {
            String[] splited = selectedFriend.split(" ");
            String firstName = splited[0];
            String lastName = splited[1];
            userFirstNameField.setText(firstName);
            userLastNameField.setText(lastName);

        }
    }

    @Override
    public void update(EntityChangeEvent entityChangeEvent) {
        if(entityChangeEvent.getType()== ChangeEventType.MESSAGE){
            loadMessageList();
            setFriendMenu();
            setFriend2Menu();
        }
        else if((entityChangeEvent.getType() == ChangeEventType.ADD )|| (entityChangeEvent.getType() == ChangeEventType.DELETE)){
            maxPage = (serviceLogin.getUserFriends().size() + limitPage - 1) / limitPage;
            page = 1;
            PageField.setText(String.valueOf(page));
            loadFriendsPage();
            loadRequestsList();
            loadUserList();
            setFriendMenu();
            setFriend2Menu();
        } else if (entityChangeEvent.getType() == ChangeEventType.NOTIFY) {
            if(Objects.equals(serviceLogin.getUtilizator().get().getId(), serviceController.getIdNotificare())){
                showNotify();
            }
        }

    }

    public void showNotify() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Yupyyyyy");
        alert.setHeaderText("Notification");
        alert.setContentText("You have a new friend request");

        alert.showAndWait();
    }

    public void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid credentials.");
        alert.setContentText(message);

        alert.showAndWait();
    }
}
