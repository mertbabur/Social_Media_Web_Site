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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import users.DBHelper;
import users.User;

/**
 *
 * @author Elif
 */
public class FriendRepository {


    
    public boolean unfollow(String userEmail, String friendNickname) {
        UserRepository ur = new UserRepository();
        int userId = ur.findUserId(userEmail);
        int friendId = ur.findUserIdFromNickname(friendNickname);
        int result = 0;
        try {
            String query = "delete from t_user_friendmapping where userid = ? and friendid = ?";
            Connection connection = DBHelper.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, friendId);
            result = statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result > 0) {
            FacesMessage msg1 = new FacesMessage("Removed from your following list.");
            msg1.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, msg1);
            return true;
        }
        return false;
    }

    public boolean isFollowing(String email, String nickname) {

        String query = "select * from t_user_friendmapping where t_user_friendmapping.userid = (select id from t_user where email = ?) and t_user_friendmapping.friendid = (select id from t_user where nickname = ?)";
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, nickname);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public boolean addFriend(String userEmail, String friendNickname) {
        UserRepository ur = new UserRepository();
        int userId = ur.findUserId(userEmail);
        int friendId = ur.findUserIdFromNickname(friendNickname);
        int result = 0;

        String query = "insert into t_user_friendmapping(userid, friendid) values(?,?)";
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, friendId);
            result = statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result > 0) {
            FacesMessage msg1 = new FacesMessage("Added to your following list.");
            msg1.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, msg1);
            return true;
        } else {
            return false;
        }

    }

    public boolean deleteAccountFromFriendsTable(int userid) {

        String query;
        try {
            query = "update t_user_friendmapping set softdelete = 1 where userid = ?";
            Connection connection = DBHelper.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userid);
            statement.executeUpdate();

            query = "update t_user_friendmapping set softdelete = 1 where friendid = ?";
            connection = DBHelper.getInstance().getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, userid);
            statement.executeUpdate();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public ArrayList<User> getFriendsList(String userEmail) {
        UserRepository ur = new UserRepository();
        ArrayList<User> friends = new ArrayList<>();
        ArrayList<Integer> friendids = new ArrayList<>();
        int userid = ur.findUserId(userEmail);
        String query = "Select * from t_user_friendmapping where userid=? and softdelete=0";
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userid);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                friendids.add(resultSet.getInt("friendid"));
            }
            query = "Select * from t_user where id=?";
            statement = connection.prepareStatement(query);
            for (int id : friendids) {
                statement.setInt(1, id);
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    friends.add(new User(resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getString("nickname")));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return friends;
    }

    public String findNicknameFromId(int id) {
        try {
            Connection con = DBHelper.getInstance().getConnection();
            String query = "select * from t_user where id = ?";
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                return rs.getString("nickname");

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return null;
    }

}
