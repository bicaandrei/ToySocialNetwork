package ubb_221.toysocialnetworkgui.repository;

import ubb_221.toysocialnetworkgui.domain.*;
import ubb_221.toysocialnetworkgui.repository.paging.*;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DBRepository implements PagingRepository<Long, User> {

    private String url;
    private String username;
    private String password;

//    private Validator<User> validator;


    public DBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public User findOne(Long longID) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from users " +
                    "where id = ?");

        ) {
            statement.setInt(1, Math.toIntExact(longID));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User u = new User(firstName,lastName);
                u.setUsername(username);
                u.setPassword(password);
                u.setId(longID);
                return u;
            }
            else {

                return null;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Iterable<User> getAll() {
        Set<User> users = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next())
            {
                Long id= resultSet.getLong("id");
                String firstName=resultSet.getString("first_name");
                String lastName=resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User u = new User(firstName,lastName);
                u.setUsername(username);
                u.setPassword(password);
                u.setId(id);
                users.add(u);

            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public User save(User entity) {

        if(entity==null)
            throw new IllegalArgumentException("Entity must not be null!");

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from users " +
                    "where first_name = ? and last_name = ?");

        ) {

            statement.setString(1, entity.getFirst_name());
            statement.setString(2, entity.getLast_name());
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {

                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User u = new User(firstName,lastName);
                u.setUsername(username);
                u.setPassword(password);
                u.setId(id);
                return u;

            }
            else{

                try(Connection connection1 = DriverManager.getConnection(url, username, password);
                    PreparedStatement statement1 = connection.prepareStatement("INSERT INTO users(id, first_name, last_name, username, password)" +
                            "VALUES (?, ?, ?, ?, ?)");

                ){
                    statement1.setLong(1, entity.getId());
                    statement1.setString(2, entity.getFirst_name());
                    statement1.setString(3, entity.getLast_name());
                    statement1.setString(4, entity.getUsername());
                    statement1.setString(5, entity.getPassword());
                    int res = statement1.executeUpdate();
                    return null;
                }

            }

        } catch (SQLException e) {

            throw new RuntimeException(e);

        }

    }

    @Override
    public User delete(Long longID) {

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM users\n " +
                    "WHERE id = ?");

        ) {
            statement.setInt(1, Math.toIntExact(longID));
            User foundUser = findOne(longID);
            if (foundUser != null) {
                statement.executeUpdate();
                return foundUser;
            }
            else return null;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public User update(User entity) {

        if(entity==null)
            throw new IllegalArgumentException("Entity must not be null!");

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from users " +
                    "where id = ?");

        ) {

            statement.setLong(1, entity.getId());
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {

                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User u = new User(firstName,lastName);
                u.setUsername(username);
                u.setPassword(password);
                if(entity.getFirst_name().equals(u.getFirst_name()) && entity.getLast_name().equals(u.getLast_name())) {

                     return u;

                }else {

                    try (Connection connection1 = DriverManager.getConnection(url, username, password);
                         PreparedStatement statement1 = connection.prepareStatement("UPDATE users\n" +
                                 "SET first_name = ?, last_name = ?\n" + "WHERE id = ?");

                    ) {
                        statement1.setString(1, entity.getFirst_name());
                        statement1.setString(2, entity.getLast_name());
                        statement1.setLong(3, entity.getId());
                        int res = statement1.executeUpdate();
                        return null;
                    }
                    catch (SQLException e) {

                        throw new RuntimeException(e);

                    }
                }

            }
            else throw new IllegalArgumentException("Entity id does not exist in database!");

        } catch (SQLException e) {

            throw new RuntimeException(e);

        }
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        Paginator<User> paginator = new Paginator<User>(pageable, this.getAll());
        return paginator.paginate();
    }
}
