/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import users.DBHelper;
import users.Message;
import users.SentMessageBean;

/**
 *
 * @author Elif
 */
public class MessageRepository {

    
    
    public ArrayList<Message> findMessagesFromFriend(String userEmail, int friendId) {
        UserRepository ur = new UserRepository();
        int userId = ur.findUserId(userEmail);
        ArrayList<Message> messages = new ArrayList<>();

        String query = "select * from t_message where fromuserid in (?,?) and touserid in (?,?) and softdelete=0 order by datesent asc";
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, friendId);
            statement.setInt(3, userId);
            statement.setInt(4, friendId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Message message = new Message(resultSet.getInt("fromuserid"), resultSet.getInt("toUserId"), resultSet.getString("message"));
                message.setDateSent(resultSet.getString("datesent"));
                messages.add(message);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return messages;
    }
    
    
    public boolean isThereAnyMessageFromThisUser(ArrayList<Message> messages, int userId) {
        for (Message message : messages) {
            if (new Integer(message.getToUserId()).equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isThereAlreadyThisChat(ArrayList<Message> messages, int fromUserId, int toUserId) {
        for (Message message : messages) {
            if (message.getFromUserId() == toUserId) {
                if (message.getToUserId() == fromUserId) {
                    return true;
                }

            }
        }
        return false;
    }

    public ArrayList<Message> findMessages(String userEmail) {
        UserRepository ur = new UserRepository();
        int userId = ur.findUserId(userEmail);
        ArrayList<Message> messages = new ArrayList<>();

        String query = "select * from t_message where (fromuserid=? or touserid=?) and softdelete=0 order by datesent desc";
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int toUserId = resultSet.getInt("toUserId");
                int fromUserId = resultSet.getInt("fromuserid");
                if (isThereAnyMessageFromThisUser(messages, toUserId) == false) {
                    if (isThereAlreadyThisChat(messages, fromUserId, toUserId) == false) {
                        Message message = new Message(fromUserId, toUserId, resultSet.getString("message"));
                        message.setDateSent(resultSet.getString("datesent"));
                        messages.add(message);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return messages;
    }


    public boolean saveMessage(Message message) {

        int result = 0;
        String query = "insert into t_message(fromuserid, touserid, message, datesent) values(?,?,?,?)";
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, message.getFromUserId());
            statement.setInt(2, message.getToUserId());
            statement.setString(3, message.getMessage());
            statement.setString(4, message.getDateSent());
            result = statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean saveMessage(SentMessageBean message) {

        int result = 0;
        String query = "insert into t_message(fromuserid, touserid, message, datesent) values(?,?,?,?)";
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, message.getFromUserId());
            statement.setInt(2, message.getToUserId());
            statement.setString(3, message.getMessage());
            statement.setString(4, message.getDateSent());
            result = statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean deleteChat(int fromid, int toid){
        int result = 0;
        String query = "delete from t_message where fromuserid in (?,?) and touserid in (?,?)";
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, fromid);
            statement.setInt(2, toid);
            statement.setInt(3, fromid);
            statement.setInt(4, toid);
            result = statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(result >0)
            return true;
        return false;
    }
    
}
