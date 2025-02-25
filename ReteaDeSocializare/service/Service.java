package src.lab7.service;

import src.lab7.domain.*;
import src.lab7.domain.validators.ValidationException;
import src.lab7.repository.Repository;
import src.lab7.repository.database.FriendshipDatabaseRepository;
import src.lab7.repository.database.MessageDataBaseRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.StreamSupport;

public class Service {

    private final Repository<Long, Utilizator> repository;
    private final FriendshipDatabaseRepository friends;
    private final MessageDataBaseRepository messages;



    public void setReply(Message message) {
        messages.updateReply(message);
    }

    public Long getNewMessageID(){
        return StreamSupport.stream(getMessages().spliterator(),false)
                .map(Message::getId)
                .max(Long::compare)
                .map(id -> id + 1)
                .orElse(1L);
    }

    public void addMessage(Message message) {
        message.setId(getNewMessageID());
        messages.save(message);
    }

    public Iterable<Message> getMessages() {
        return messages.findAll();
    }

    public Service(Repository<Long, Utilizator> repository, FriendshipDatabaseRepository friends, MessageDataBaseRepository messages) {
        this.repository = repository;
        this.friends = friends;
        this.messages = messages;
    }

    public Long getNewID(){
        return StreamSupport.stream(getUsers().spliterator(),false)// Creează un stream din Iterable
                .map(Utilizator::getId)
                .max(Long::compare)
                .map(id -> id + 1)
                .orElse(1L);

    }

    public Iterable<Utilizator> getUsers(){
        return repository.findAll();
    }

    public Optional<Utilizator> getUser(long id){
        return repository.findOne(id);
    }

    public void addUtilizator(Utilizator utilizator) {
        utilizator.setId(getNewID());
        repository.save(utilizator);
    }

    public Utilizator removeUtilizator(Long id) {
        try {
            Utilizator user = getUser(id).orElseThrow(() -> new IllegalArgumentException("The user doesn't exist!"));

            Vector<Long> toDelete = new Vector<>();
            StreamSupport.stream(getFriendships().spliterator(),false)
                    .filter(friendship -> Objects.equals(friendship.getUserId2(), id) || Objects.equals(friendship.getUserId1(), id))
                    .map(Friendship::getId)
                    .forEach(toDelete::add);

            toDelete.forEach(friends::delete);
            return repository.delete(id).orElse(null);
        }
        catch (IllegalArgumentException | ValidationException e) {
            System.out.println("Invalid user!");
        }
        return null;
    }

    public Long getNewFriendshipID(){
        return StreamSupport.stream(getFriendships().spliterator(),false)
                .map(Friendship::getId)
                .max(Long::compare)
                .map(id -> id + 1)
                .orElse(1L);
    }

    public Iterable<Friendship> getFriendships(){
        return friends.findAll();
    }

    public List<Utilizator> getUserFriendsPaginated(Long id, int page, int limitPage){
        List<Utilizator> prieteni = new ArrayList<>();
        friends.findAllPaginated(id, page, limitPage).forEach(friendship -> {
            if (friendship.getUserId1().equals(id)) {
                getUser(friendship.getUserId2()).ifPresent(prieteni::add);
            } else if (friendship.getUserId2().equals(id)) {
                getUser(friendship.getUserId1()).ifPresent(prieteni::add);
            }
        });
        return prieteni;
    }

    public List<Utilizator> getUserFriends(Long id) {
        List<Utilizator> friends = new ArrayList<>();

        Optional<Utilizator> userOptional = getUser(id);
        if (userOptional.isEmpty()) {
            return friends;
        }
        getFriendships().forEach(friendship -> {
            if (friendship.getUserId1().equals(id)) {
                getUser(friendship.getUserId2()).ifPresent(friends::add);
            } else if (friendship.getUserId2().equals(id)) {
                getUser(friendship.getUserId1()).ifPresent(friends::add);
            }
        });

        return friends;

    }


    public void addFriendship(Long id1, Long id2) {
        if(getUser(id1).isEmpty() || getUser(id2).isEmpty()) {
            throw new ValidationException("User not found");
        }
        Utilizator utilizator1 = getUser(id1).get();
        Utilizator utilizator2 = getUser(id2).get();

        boolean friendshipB = StreamSupport.stream(getFriendships().spliterator(),false)
                .anyMatch(friendship -> (Objects.equals(friendship.getUserId1(), id1) && Objects.equals(friendship.getUserId2(), id2))
                        || (Objects.equals(friendship.getUserId1(), id2) && Objects.equals(friendship.getUserId2(), id1)));
        if(friendshipB) {
                throw new ValidationException("The friendship already exists");
            }
        Friendship friend = new Friendship(utilizator1.getId(), utilizator2.getId(), LocalDateTime.now());
        friend.setId(getNewFriendshipID());
        friends.save(friend);
    }


    public void removeFriendship(Long id1, Long id2) {
        if(getUser(id1).isEmpty() || getUser(id2).isEmpty()){
            throw new ValidationException("User not found");
        }

        Long id = StreamSupport.stream(getFriendships().spliterator(),false)
                .filter(friendship -> (Objects.equals(friendship.getUserId1(), id1) || Objects.equals(friendship.getUserId2(), id2))
                        || (Objects.equals(friendship.getUserId1(), id2) && Objects.equals(friendship.getUserId2(), id1)))
                .map(Friendship::getId)
                .findFirst()
                .orElseThrow(() -> new ValidationException("The friendship doesn't exist!"));

        friends.delete(id);
    }




}
