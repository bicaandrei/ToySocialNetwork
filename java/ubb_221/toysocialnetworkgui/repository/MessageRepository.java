package ubb_221.toysocialnetworkgui.repository;

import ubb_221.toysocialnetworkgui.domain.Friendship;
import ubb_221.toysocialnetworkgui.domain.Message;
import ubb_221.toysocialnetworkgui.domain.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MessageRepository implements RepositoryInterface<Long, Message> {

    private String url;
    private String username;
    private String password;
    private DBRepository UserDBRepository;
    public MessageRepository(String url, String username, String password, DBRepository UserDBRepository) {

        this.url = url;
        this.username = username;
        this.password = password;
        this.UserDBRepository = UserDBRepository;

    }

    public Message findOne(Long longID){

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from messages " +
                    "where id = ?");

        ) {
            statement.setInt(1, Math.toIntExact(longID));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {

                Long id = resultSet.getLong("id");
                Long from_id = resultSet.getLong("from_id");
                Long to_id = resultSet.getLong("to_id");
                String message = resultSet.getString("message");
                java.sql.Timestamp timestamp = resultSet.getTimestamp("date");
                LocalDateTime dateTime = timestamp.toLocalDateTime();
                User user1 = this.UserDBRepository.findOne(from_id);
                User user2 = this.UserDBRepository.findOne(to_id);
                Message m = new Message(user1, user2, message, dateTime);
                m.setId(id);
                return m;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;

    }

    public Message save(Message m){

        if(m.getMessage().isEmpty())
            throw new IllegalArgumentException("Message must not be empty!");

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement1 = connection.prepareStatement("INSERT INTO public.messages(\n" +
                    "\tid, from_id, to_id, message, date)" +
                    "VALUES (?, ?, ?, ?, ?)");

        ){
            statement1.setLong(1, m.getId());
            statement1.setLong(2, m.getFrom().getId());
            statement1.setLong(3, m.getTo().getId());
            statement1.setString(4, m.getMessage());
            java.sql.Timestamp timestamp = Timestamp.valueOf(m.getDate());
            statement1.setTimestamp(5, timestamp);
            int res = statement1.executeUpdate();
            return null;
        }
        catch (SQLException e){

            throw new RuntimeException(e);

        }
    }

    public Message delete(Long id){

        return null;

    }

    public Message update(Message m){

        return null;

    }
    public Iterable<Message> getAll(){

        Set<Message> messages = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from messages");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next())
            {
                Long id= resultSet.getLong("id");
                Long from_id=resultSet.getLong("from_id");
                Long to_id=resultSet.getLong("to_id");
                String message = resultSet.getString("message");
                java.sql.Timestamp timestamp = resultSet.getTimestamp("date");
                LocalDateTime dateTime = timestamp.toLocalDateTime();
                User user1 = this.UserDBRepository.findOne(from_id);
                User user2 = this.UserDBRepository.findOne(to_id);
                Message m = new Message(user1, user2, message, dateTime);
                m.setId(id);
                messages.add(m);

            }
            return messages;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}

