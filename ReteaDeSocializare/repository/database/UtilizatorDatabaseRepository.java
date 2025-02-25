package src.lab7.repository.database;

import src.lab7.domain.Utilizator;
import src.lab7.domain.validators.UtilizatorValidator;
import src.lab7.repository.Repository;
import src.lab7.repository.file.UtilizatorRepository;

import java.sql.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class UtilizatorDatabaseRepository implements Repository<Long, Utilizator> {
    UtilizatorValidator utilizatorValidator;
    private Connection connectionn;
    public UtilizatorDatabaseRepository(UtilizatorValidator utilizatorValidator, Connection connectionn) {
        this.utilizatorValidator = utilizatorValidator;
        this.connectionn = connectionn;

    }

    @Override
    public Optional<Utilizator> findOne(Long aLong) {
        String query = "SELECT * FROM utilizatori WHERE \"id\"= ?";
        Utilizator utilizator = null;
        try (PreparedStatement preparedStatement = connectionn.prepareStatement(query)) {

            preparedStatement.setLong(1, aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String firstName = resultSet.getString("nume");
                String lastName = resultSet.getString("prenume");
                String parola = resultSet.getString("parola");
                utilizator = new Utilizator(firstName, lastName, parola);
                utilizator.setId(aLong);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(utilizator);
    }

    @Override
    public Iterable<Utilizator> findAll() {
        HashMap<Long, Utilizator> utilizatori = new HashMap<>();
        try (PreparedStatement preparedStatement = connectionn.prepareStatement("select * from utilizatori");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String parola = resultSet.getString("parola");
                Utilizator user = new Utilizator(nume, prenume, parola);
                user.setId(id);
                utilizatori.put(user.getId(), user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utilizatori.values();
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        if (entity == null) {
            throw new IllegalArgumentException("User can't be null!");
        }
        String query = "INSERT INTO utilizatori(\"id\", \"prenume\", \"nume\", \"parola\") VALUES (?,?,?,?)";

        try (PreparedStatement preparedStatement = connectionn.prepareStatement(query)) {
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setString(2, entity.getFirstName());
            preparedStatement.setString(3, entity.getLastName());
            preparedStatement.setString(4,entity.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(entity);
    }

    @Override
    public Optional<Utilizator> delete(Long aLong) {

        Utilizator userToDelete = findOne(aLong).orElse(null);

        if (userToDelete == null) {
            return Optional.empty();
        }

        String query = "DELETE FROM utilizatori WHERE \"id\" = ?";

        try (PreparedStatement preparedStatement = connectionn.prepareStatement(query)) {
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(userToDelete);
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        return Optional.empty();
    }
}
