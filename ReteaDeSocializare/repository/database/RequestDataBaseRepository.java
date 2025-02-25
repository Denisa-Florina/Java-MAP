package src.lab7.repository.database;

import src.lab7.connection.Connectionn;
import src.lab7.domain.Friendship;
import src.lab7.domain.FriendshipRequest;
import src.lab7.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RequestDataBaseRepository implements Repository<Long, FriendshipRequest> {

    private Connection connectionn;
    public RequestDataBaseRepository(Connection connectionn) {
       this.connectionn = connectionn;
    }

    @Override
    public Optional<FriendshipRequest> findOne(Long aLong) {
        String query = "SELECT * FROM request WHERE \"id\" = ?";
        FriendshipRequest friendship = null;
        try(PreparedStatement preparedStatement = connectionn.prepareStatement(query)) {
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long idFriend1 = resultSet.getLong("idfriend1");
                Long idFriend2 = resultSet.getLong("idfriend2");
                Timestamp timestamp = resultSet.getTimestamp("friendsfrom");
                LocalDateTime friendsFrom = new Timestamp(timestamp.getTime()).toLocalDateTime();
                friendship = new FriendshipRequest(idFriend1, idFriend2, friendsFrom);
                friendship.setId(aLong);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(friendship);
    }

    @Override
    public Iterable<FriendshipRequest> findAll() {
        Map<Long, FriendshipRequest> friendships = new HashMap<>();
        try(PreparedStatement preparedStatement = connectionn.prepareStatement("SELECT * FROM request");
            ResultSet resultSet = preparedStatement.executeQuery()) {

            while(resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idFriend1 = resultSet.getLong("idfriend1");
                Long idFriend2 = resultSet.getLong("idfriend2");
                Timestamp timestamp = resultSet.getTimestamp("friendsfrom");
                LocalDateTime friendsFrom = new Timestamp(timestamp.getTime()).toLocalDateTime();
                FriendshipRequest friendship = new FriendshipRequest(idFriend1, idFriend2, friendsFrom);
                friendship.setId(id);
                friendships.put(id, friendship);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return friendships.values();
    }

    @Override
    public Optional<FriendshipRequest> save(FriendshipRequest entity) {
        if(entity == null) {
            throw new IllegalArgumentException("Friendship cannot be null");
        }
        String query = "INSERT INTO request(\"id\", \"idfriend1\", \"idfriend2\", \"friendsfrom\") VALUES (?,?,?,?)";
        try(PreparedStatement preparedStatement = connectionn.prepareStatement(query)){
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setLong(2,entity.getUserIdR1());
            preparedStatement.setLong(3,entity.getUserIdR2());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(entity.getDateTimeR()));
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<FriendshipRequest> delete(Long aLong) {
        FriendshipRequest friendshipToDelete = null;
        for(FriendshipRequest friendship : findAll()) {
            if(friendship.getId().equals(aLong)) {
                friendshipToDelete = friendship;
            }
        }

        if(friendshipToDelete == null) {
            return Optional.empty();
        }

        String query = "DELETE FROM request WHERE \"id\" = ?";
        try(PreparedStatement preparedStatement = connectionn.prepareStatement(query)){
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(friendshipToDelete);
    }

    @Override
    public Optional<FriendshipRequest> update(FriendshipRequest entity) {
        return Optional.empty();
    }
}
