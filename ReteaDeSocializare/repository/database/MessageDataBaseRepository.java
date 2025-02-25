package src.lab7.repository.database;

import src.lab7.domain.FriendshipRequest;
import src.lab7.domain.Message;
import src.lab7.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MessageDataBaseRepository implements Repository<Long, Message> {

    private Connection connectionn;
    public MessageDataBaseRepository(Connection connectionn) {
        this.connectionn = connectionn;
    }

    @Override
    public Optional<Message> findOne(Long aLong) {
        String query = "SELECT * FROM messages WHERE \"id\" = ?";
        Message mess = null;
        try(PreparedStatement preparedStatement = connectionn.prepareStatement(query)) {
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                String message = resultSet.getString("message");
                LocalDateTime date = resultSet.getTimestamp("datetime").toLocalDateTime();
                String reply = resultSet.getString("reply");
                mess = new Message(id1, id2, message, date);
                mess.setId(aLong);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(mess);
    }

    @Override
    public Iterable<Message> findAll() {
        Map<Long, Message> friendships = new HashMap<>();
        try(PreparedStatement preparedStatement = connectionn.prepareStatement("SELECT * FROM messages");
            ResultSet resultSet = preparedStatement.executeQuery()) {

            while(resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idFriend1 = resultSet.getLong("id1");
                Long idFriend2 = resultSet.getLong("id2");
                Timestamp timestamp = resultSet.getTimestamp("datetime");
                LocalDateTime friendsFrom = new Timestamp(timestamp.getTime()).toLocalDateTime();
                String mesaj = resultSet.getString("message");
                String reply = resultSet.getString("reply");
                Message message = new Message(idFriend1, idFriend2, mesaj, friendsFrom, reply);
                message.setId(id);
                friendships.put(id, message);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return friendships.values();
    }

    @Override
    public Optional<Message> save(Message entity) {
        if(entity == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        String query = "INSERT INTO messages(\"id\", \"id1\", \"id2\", \"message\" , \"datetime\", \"reply\") VALUES (?,?,?,?,?,?)";
        try(PreparedStatement preparedStatement = connectionn.prepareStatement(query)){
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setLong(2,entity.getId1());
            preparedStatement.setLong(3,entity.getId2());
            preparedStatement.setString(4,entity.getMessage());
            preparedStatement.setTimestamp(5,Timestamp.valueOf(entity.getDatetime()));
            preparedStatement.setString(6,entity.getReply());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Message> delete(Long aLong) {
        Message friendshipToDelete = null;
        for(Message friendship : findAll()) {
            if(friendship.getId().equals(aLong)) {
                friendshipToDelete = friendship;
            }
        }
        if(friendshipToDelete == null) {
            return Optional.empty();
        }
        String query = "DELETE FROM messages WHERE \"id\" = ?";
        try(PreparedStatement preparedStatement = connectionn.prepareStatement(query)){
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(friendshipToDelete);
    }

    public Optional<Message> updateReply(Message entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        String query = "UPDATE messages SET \"reply\" = ? WHERE \"id\" = ?";
        try (PreparedStatement preparedStatement = connectionn.prepareStatement(query)) {
            preparedStatement.setString(1, entity.getReply());
            preparedStatement.setLong(2, entity.getId());
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                return Optional.of(entity);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<Message> update(Message entity) {
        return Optional.empty();
    }
}
