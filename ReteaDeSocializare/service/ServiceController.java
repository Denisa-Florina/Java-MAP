package src.lab7.service;

import src.lab7.domain.FriendshipRequest;
import src.lab7.domain.Message;
import src.lab7.domain.Utilizator;
import src.lab7.domain.validators.ValidationException;
import src.lab7.events.ChangeEventType;
import src.lab7.events.EntityChangeEvent;
import src.lab7.repository.Repository;
import src.lab7.utils.Observable;
import src.lab7.utils.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class ServiceController implements Observable<EntityChangeEvent>{
    private final Repository<Long, FriendshipRequest> friendsRequest;
    private final Service serviceRequest;
    private List<Observer<EntityChangeEvent>> observers=new ArrayList<>();
    private Long idNotificare;


    public ServiceController(Repository<Long, FriendshipRequest> friends, Service serviceRequest) {
        this.friendsRequest = friends;
        this.serviceRequest = serviceRequest;

    }

    public void setReply(Message reply) {
        serviceRequest.setReply(reply);
        notifyObservers(new EntityChangeEvent(ChangeEventType.MESSAGE, null));
    }

    public Iterable<FriendshipRequest> getFriendshipRequests(){
        return friendsRequest.findAll();
    }

    public Optional<FriendshipRequest> getFriendshipRequest(long id){
        return friendsRequest.findOne(id);
    }

    public List<Utilizator> getUserFriendsRequest(Long id) {
        List<Utilizator> friends = new ArrayList<>();

        Optional<Utilizator> userOptional = serviceRequest.getUser(id);
        if (userOptional.isEmpty()) {
            return friends;
        }
        getFriendshipRequests().forEach(friendship -> {
            if (friendship.getUserIdR1().equals(id)) {
                serviceRequest.getUser(friendship.getUserIdR2()).ifPresent(friends::add);
            } else if (friendship.getUserIdR2().equals(id)) {
                serviceRequest.getUser(friendship.getUserIdR1()).ifPresent(friends::add);
            }
        });
        return friends;
    }

    public void respond(FriendshipRequest friendshipRequest, String status) {
        if(Objects.equals(status, "accept")){
            serviceRequest.addFriendship(friendshipRequest.getUserIdR1(), friendshipRequest.getUserIdR2()); //adaug prietenia in service
            friendsRequest.delete(friendshipRequest.getId());
            notifyObservers(new EntityChangeEvent(ChangeEventType.ADD, null));
        }
        else if(Objects.equals(status, "deny")){
            friendsRequest.delete(friendshipRequest.getId());
            notifyObservers(new EntityChangeEvent(ChangeEventType.DELETE, null));
        }
    }

    public Long getNewFriendshipID(){
        return StreamSupport.stream(getFriendshipRequests().spliterator(),false)
                .map(FriendshipRequest::getId)
                .max(Long::compare)
                .map(id -> id + 1)
                .orElse(1L);
    }

    public void addFriend(Long id1, Long id2){
        if(serviceRequest.getUser(id1).isEmpty() || serviceRequest.getUser(id2).isEmpty()) {
            throw new ValidationException("User not found");
        }
        Utilizator utilizator1 = serviceRequest.getUser(id1).get();
        Utilizator utilizator2 = serviceRequest.getUser(id2).get();

        boolean friendshipB = StreamSupport.stream(getFriendshipRequests().spliterator(),false)
                .anyMatch(friendship -> (Objects.equals(friendship.getUserIdR1(), id1) && Objects.equals(friendship.getUserIdR2(), id2))
                        || (Objects.equals(friendship.getUserIdR1(), id2) && Objects.equals(friendship.getUserIdR2(), id1)));
        if(friendshipB) {
            throw new ValidationException("The friendship request already exists");
        }
        FriendshipRequest friend = new FriendshipRequest(utilizator1.getId(), utilizator2.getId(), LocalDateTime.now());
        friend.setId(getNewFriendshipID());
        friendsRequest.save(friend);
        EntityChangeEvent event = new EntityChangeEvent(ChangeEventType.ADD,utilizator1);
        notifyObservers(event);
        idNotificare = utilizator2.getId();
        EntityChangeEvent event1 = new EntityChangeEvent(ChangeEventType.NOTIFY,utilizator1);
        notifyObservers(event1);
    }

    public Long getIdNotificare(){
        return idNotificare;
    }

    public Iterable<Message> getMessage(){
        return serviceRequest.getMessages();
    }

    public void addMessage(Long id1, Long id2, String message){
        serviceRequest.addMessage(new Message(id1, id2, message, LocalDateTime.now()));
        EntityChangeEvent event = new EntityChangeEvent(ChangeEventType.MESSAGE,null);
        notifyObservers(event);
    }

    public void removeFriendship(Long id1, Long id2){
        serviceRequest.removeFriendship(id1, id2);
        notifyObservers(new EntityChangeEvent(ChangeEventType.DELETE, serviceRequest.getUser(id1).get()));
    }

    public void removeRequestFriendship(Long id1, Long id2) {
        if(serviceRequest.getUser(id1).isEmpty() || serviceRequest.getUser(id2).isEmpty()){
            throw new ValidationException("User not found");
        }

        Long id = StreamSupport.stream(getFriendshipRequests().spliterator(),false)
                .filter(friendship -> (Objects.equals(friendship.getUserIdR1(), id1) || Objects.equals(friendship.getUserIdR2(), id2))
                        || (Objects.equals(friendship.getUserIdR1(), id2) && Objects.equals(friendship.getUserIdR2(), id1)))
                .map(FriendshipRequest::getId)
                .findFirst()
                .orElseThrow(() -> new ValidationException("The friendship request doesn't exist!"));

        friendsRequest.delete(id);
        notifyObservers(new EntityChangeEvent(ChangeEventType.DELETE, serviceRequest.getUser(id1).get()));
    }

    @Override
    public void addObserver(Observer<EntityChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<EntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(EntityChangeEvent t) {
        observers.forEach(x->x.update(t));
    }

}
