package src.lab7.repository.database;

import src.lab7.domain.Friendship;
import src.lab7.domain.validators.ValidationFriendship;
import src.lab7.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FriendshipDatabaseRepository implements Repository<Long, Friendship> {

    ValidationFriendship validationFriendship = new ValidationFriendship();
    private Connection connectionn;
    public FriendshipDatabaseRepository(ValidationFriendship validationFriendship, Connection connectionn) {
        this.validationFriendship = validationFriendship;
        this.connectionn = connectionn;
    }

    public Iterable<Friendship> findAllPaginated(long userId, int page, int pageSize) {
        Map<Long, Friendship> friendships = new HashMap<>();
        String query = "SELECT * FROM prietenie WHERE \"idfriend1\" = ? OR \"idfriend2\" = ? LIMIT ? OFFSET ?";
        //LIMIT = cate sa ia, OFFSET= incepand de unde
        try (PreparedStatement preparedStatement = connectionn.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, userId);
            preparedStatement.setInt(3, pageSize); // LIMIT
            preparedStatement.setInt(4, (page - 1) * pageSize); // OFFSET
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idFriend1 = resultSet.getLong("idfriend1");
                Long idFriend2 = resultSet.getLong("idfriend2");
                Timestamp timestamp = resultSet.getTimestamp("friendsfrom");
                LocalDateTime friendsFrom = new Timestamp(timestamp.getTime()).toLocalDateTime();
                Friendship friendship = new Friendship(idFriend1, idFriend2, friendsFrom);
                friendship.setId(id);
                friendships.put(id, friendship);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return friendships.values();
    }


    @Override
    public Optional<Friendship> findOne(Long aLong) {
        String query = "SELECT * FROM prietenie WHERE \"id\" = ?";
        Friendship friendship = null;
        try(PreparedStatement preparedStatement = connectionn.prepareStatement(query)) {
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long idFriend1 = resultSet.getLong("idfriend1");
                Long idFriend2 = resultSet.getLong("idfriend2");
                Timestamp timestamp = resultSet.getTimestamp("friendsfrom");
                LocalDateTime friendsFrom = new Timestamp(timestamp.getTime()).toLocalDateTime();
                friendship = new Friendship(idFriend1, idFriend2, friendsFrom);
                friendship.setId(aLong);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(friendship);
    }

    @Override
    public Iterable<Friendship> findAll() {
        Map<Long, Friendship> friendships = new HashMap<>();
        try(PreparedStatement preparedStatement = connectionn.prepareStatement("SELECT * FROM prietenie");
            ResultSet resultSet = preparedStatement.executeQuery()) {

            while(resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idFriend1 = resultSet.getLong("idfriend1");
                Long idFriend2 = resultSet.getLong("idfriend2");
                Timestamp timestamp = resultSet.getTimestamp("friendsfrom");
                LocalDateTime friendsFrom = new Timestamp(timestamp.getTime()).toLocalDateTime();
                Friendship friendship = new Friendship(idFriend1, idFriend2, friendsFrom);
                friendship.setId(id);
                friendships.put(id, friendship);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return friendships.values();
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        if(entity == null) {
            throw new IllegalArgumentException("Friendship cannot be null");
        }
        String query = "INSERT INTO prietenie(\"id\", \"idfriend1\", \"idfriend2\", \"friendsfrom\") VALUES (?,?,?,?)";
        try(PreparedStatement preparedStatement = connectionn.prepareStatement(query)){
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setLong(2,entity.getUserId1());
            preparedStatement.setLong(3,entity.getUserId2());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getDateTime()));
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Friendship> delete(Long aLong) {
        Friendship friendshipToDelete = null;
        for(Friendship friendship : findAll()) {
            if(friendship.getId().equals(aLong)) {
                friendshipToDelete = friendship;
            }
        }

        if(friendshipToDelete == null) {
            return Optional.empty();
        }

        String query = "DELETE FROM prietenie WHERE \"id\" = ?";
        try(PreparedStatement preparedStatement = connectionn.prepareStatement(query)){
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(friendshipToDelete);
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        return Optional.empty();
    }
}
