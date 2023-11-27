package ubb_221.toysocialnetworkgui.repository;

import ubb_221.toysocialnetworkgui.domain.Friendship;
import ubb_221.toysocialnetworkgui.domain.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipDBRepository implements RepositoryInterface<Long, Friendship> {

    private String url;
    private String username;
    private String password;
    private DBRepository UserDBRepository;
    public FriendshipDBRepository(String url, String username, String password, DBRepository UserDBRepository) {

        this.url = url;
        this.username = username;
        this.password = password;
        this.UserDBRepository = UserDBRepository;

    }

    public Friendship findOne(Long longID){

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from friendships " +
                    "where id = ?");

        ) {
            statement.setInt(1, Math.toIntExact(longID));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {

                Long id = resultSet.getLong("id");
                Long user1ID = resultSet.getLong("id1");
                Long user2ID = resultSet.getLong("id2");
                java.sql.Date date = resultSet.getDate("friendsfrom");
                LocalDate localdate = date.toLocalDate();
                User u1, u2;
                if(this.UserDBRepository.findOne(user1ID) != null)
                   u1 = this.UserDBRepository.findOne(user1ID);
                else
                   u1 = null;
                if(this.UserDBRepository.findOne(user2ID) != null)
                   u2 = this.UserDBRepository.findOne(user2ID);
                else
                   u2 = null;
                Friendship friendship = new Friendship(u1, u2);
                friendship.setId(id);
                friendship.set_date_of_friendship(localdate);
                return friendship;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;

    }
    public Iterable<Friendship> getAll(){

        Set<Friendship> friendships = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from friendships");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next())
            {

                Long id = resultSet.getLong("id");
                Long user1ID = resultSet.getLong("id1");
                Long user2ID = resultSet.getLong("id2");
                java.sql.Date date = resultSet.getDate("friendsfrom");
                LocalDate localdate = date.toLocalDate();
                User u1, u2;
                if(this.UserDBRepository.findOne(user1ID) != null)
                    u1 = this.UserDBRepository.findOne(user1ID);
                else
                    u1 = null;
                if(this.UserDBRepository.findOne(user2ID) != null)
                    u2 = this.UserDBRepository.findOne(user2ID);
                else
                    u2 = null;
                Friendship friendship = new Friendship(u1, u2);
                friendship.setId(id);

                friendship.set_date_of_friendship(localdate);
                friendships.add(friendship);

            }
            return friendships;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Friendship save(Friendship entity){

        if(entity==null)
            throw new IllegalArgumentException("Entity must not be null!");

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from friendships " +
                    "where id1 = ? and id2 = ?");

        ) {

            statement.setLong(1, entity.get_e1().getId());
            statement.setLong(2, entity.get_e2().getId());
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {

                Long id = resultSet.getLong("id");
                Long user1ID = resultSet.getLong("id1");
                Long user2ID = resultSet.getLong("id2");
                java.sql.Date date = resultSet.getDate("friendsfrom");
                LocalDate localdate = date.toLocalDate();
                User u1, u2;
                if(this.UserDBRepository.findOne(user1ID) != null)
                    u1 = this.UserDBRepository.findOne(user1ID);
                else
                    u1 = null;
                if(this.UserDBRepository.findOne(user2ID) != null)
                    u2 = this.UserDBRepository.findOne(user2ID);
                else
                    u2 = null;
                Friendship friendship = new Friendship(u1, u2);
                friendship.setId(id);
                friendship.set_date_of_friendship(localdate);
                return friendship;

            }
            else{

                try(Connection connection1 = DriverManager.getConnection(url, username, password);
                    PreparedStatement statement1 = connection.prepareStatement("INSERT INTO friendships(id, id1, id2, friendsfrom)" +
                            "VALUES (?, ?, ?, ?)");

                ){
                    statement1.setLong(1, entity.getId());
                    statement1.setLong(2, entity.get_e1().getId());
                    statement1.setLong(3, entity.get_e2().getId());
                    Date date = java.sql.Date.valueOf(entity.get_date_of_friendship());
                    java.sql.Date sqldate = new java.sql.Date(date.getTime());
                    statement1.setDate(4, sqldate);
                    int res = statement1.executeUpdate();
                    return null;
                }

            }

        } catch (SQLException e) {

            throw new RuntimeException(e);

        }

    }

    public Friendship delete(Long longID){

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM friendships\n" +
                    "WHERE ID=? ");

        ) {

            statement.setInt(1, Math.toIntExact(longID));
            statement.executeUpdate();
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Friendship update(Friendship entity){

        return null;

    }

}
