/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import users.DBHelper;
import users.User;
import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import users.UserInformation;

/**
 *
 * @author Elif
 */
public class UserRepository {

    public boolean changePassword(User user) {
        String query = "update t_user set password=? where email=?";
        int result = 0;
        ArrayList<User> users = getUsersfromTable();
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement(query);
                for (User item : users) {
                    if ((item.getEmail()).equals(user.getEmail())) {
                        statement.setString(1, user.getPassword());
                        statement.setString(2, user.getEmail());
                    }
                }
                result = statement.executeUpdate();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<User> findValuesFromSearch(String info, String userEmail) {
        ArrayList<User> users = new ArrayList<>();
        String query;
        String email;
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            if (connection != null) {
                query = "Select * from t_user inner join t_introduceyourself on t_user.id = t_introduceyourself.id where t_user.firstname  like ? and t_user.softdelete =0;";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, "%" + info + "%");
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    email = resultSet.getString("email");
                    if (email.equals(userEmail) == false) {
                        User user = new User(resultSet.getString("firstname"), resultSet.getString("lastname"),
                                email, resultSet.getString("phonenumber"), resultSet.getString("password"),
                                resultSet.getString("gender"), resultSet.getString("nickname"));
                        user.setLive(resultSet.getString("live"));
                        users.add(user);
                    }
                }
                query = "Select * from t_user inner join t_introduceyourself on t_user.id = t_introduceyourself.id where t_user.lastname  like ? and t_user.softdelete =0;";
                statement = connection.prepareStatement(query);
                statement.setString(1, "%" + info + "%");
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    email = resultSet.getString("email");
                    if (findIfArraylistContainsTheUser(users, email) == false) {
                        if (email.equals(userEmail) == false) {
                            User user = new User(resultSet.getString("firstname"), resultSet.getString("lastname"),
                                    email, resultSet.getString("phonenumber"), resultSet.getString("password"),
                                    resultSet.getString("gender"), resultSet.getString("nickname"));
                            user.setLive(resultSet.getString("live"));
                            users.add(user);
                        }
                    }
                }
                query = "Select * from t_user inner join t_introduceyourself on t_user.id = t_introduceyourself.id where t_user.nickname  like ? and t_user.softdelete =0;";
                statement = connection.prepareStatement(query);
                statement.setString(1, "%" + info + "%");
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    email = resultSet.getString("email");
                    if (findIfArraylistContainsTheUser(users, email) == false) {
                        if (email.equals(userEmail) == false) {
                            User user = new User(resultSet.getString("firstname"), resultSet.getString("lastname"),
                                    email, resultSet.getString("phonenumber"), resultSet.getString("password"),
                                    resultSet.getString("gender"), resultSet.getString("nickname"));
                            user.setLive(resultSet.getString("live"));
                            users.add(user);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public ArrayList<User> getUserToUniversity(String email) {
        int userid = findUserId(email);
        String university = null;
        ArrayList<User> users = new ArrayList<>();
        ArrayList<UserInformation> userinformations = getWorkandEdufromTable();
        for (UserInformation userinfo : userinformations) {
            if (email.equals(userinfo.getEmail())) {
                university = userinfo.getUniversity();
            }
        }
        try {
            String query = "select * from t_user inner join t_workandeducation on t_user.id=t_workandeducation.id where userUniversity in('" + university + "') and t_user.softdelete = 0 and t_workandeducation.softdelete = 0";
            Connection connection = DBHelper.getInstance().getConnection();
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    if (resultSet.getInt("id") != userid) {
                        User us = new User(resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getString("nickname"));
                        users.add(us);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public ArrayList<User> getUserToHighschool(String email) {
        int userid = findUserId(email);
        String highschool = null;
        ArrayList<User> users = new ArrayList<>();
        ArrayList<UserInformation> userinformations = getWorkandEdufromTable();
        for (UserInformation userinfo : userinformations) {
            if (email.equals(userinfo.getEmail())) {
                highschool = userinfo.getHighschool();
            }
        }
        try {
            String query = "select * from t_user inner join t_workandeducation on t_user.id=t_workandeducation.id where userHighSchool in('" + highschool + "') and t_user.softdelete = 0 and t_workandeducation.softdelete = 0";
            Connection connection = DBHelper.getInstance().getConnection();
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    if (resultSet.getInt("id") != userid) {
                        User us = new User(resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getString("nickname"));
                        users.add(us);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

     public int getFollowingCountFromTable(String email) {
        int userid = findUserId(email);
        int numberOfFollowing = 0;
        try {
            String query = "Select count(*) from t_user_friendmapping where userid =" + userid + " and softdelete=0";
            Connection connection = DBHelper.getInstance().getConnection();
            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                resultSet.next();
                numberOfFollowing = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numberOfFollowing;
    }

    public int getFollowersCountFromTable(String email) {
        int userid = findUserId(email);
        int numberOfFollowers = 0;
        try {
            String query = "Select count(*) from t_user_friendmapping where friendid =" + userid + " and softdelete=0";
            Connection connection = DBHelper.getInstance().getConnection();
            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                resultSet.next();
                numberOfFollowers = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numberOfFollowers;
    }

    
    public ArrayList getUsersfromTable() {
        ArrayList<User> users = new ArrayList<>();
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            if (connection != null) {
                String query = "Select * from t_user where softdelete = 0";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    User user = new User(resultSet.getString("firstname"), resultSet.getString("lastname"),
                            resultSet.getString("email"), resultSet.getString("phonenumber"), resultSet.getString("password"),
                            resultSet.getString("gender"), resultSet.getString("nickname"));
                    users.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public int findUserId(String email) {

        try {
            Connection con = DBHelper.getInstance().getConnection();
            String query = "select * from t_user where email = ?";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                return rs.getInt("id");

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }

        return 0;

    }

    public int findUserIdFromNickname(String nickname) {

        try {
            Connection con = DBHelper.getInstance().getConnection();
            String query = "select * from t_user where nickname = ?";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, nickname);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                return rs.getInt("id");

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }

        return 0;

    }

    public boolean save(User user) {
        ArrayList<User> users = getUsersfromTable();
        int result = 0;
        String query = "insert into t_user(firstname, lastname, email, phonenumber,password,gender,nickname) values(?,?,?,?,?,?,?)";
        try {
            for (User item : users) {
                if ((item.getEmail()).equals(user.getEmail())) {
                    FacesMessage msg1 = new FacesMessage("This mail is used!!", "ERROR MSG");
                    msg1.setSeverity(FacesMessage.SEVERITY_ERROR);
                    FacesContext.getCurrentInstance().addMessage(null, msg1);
                    return false;
                }
                if ((item.getNickname()).equals(user.getNickname())) {
                    FacesMessage msg1 = new FacesMessage("This nickname is used!!", "ERROR MSG");
                    msg1.setSeverity(FacesMessage.SEVERITY_ERROR);
                    FacesContext.getCurrentInstance().addMessage(null, msg1);
                    return false;
                }
            }
            Connection connection = DBHelper.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhoneNumber());
            statement.setString(5, user.getPassword());
            statement.setString(6, user.getGender());
            statement.setString(7, user.getNickname());
            result = statement.executeUpdate();
            if (result > 0) {
                query = "insert into t_introduceyourself (mail , id, live) values (?,?,?)";
                statement = connection.prepareStatement(query);
                statement.setString(1, user.getEmail());
                statement.setInt(2, this.findUserId(user.getEmail()));
                statement.setString(3, user.getLive());
                result = statement.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    
    public void deleteAccountFromMessages(int userid){
        String query = "update t_message set softdelete=1 where fromuserid = ? or touserid = ?";
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, userid);
                statement.setInt(2, userid);
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean deleteAccount(User user) {
        String query = "update t_user set softdelete=1 where email=?";
        int result = 0;
        ArrayList<User> users = getUsersfromTable();
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement(query);
                for (User item : users) {
                    if ((item.getEmail()).equals(user.getEmail())) {
                        statement.setString(1, user.getEmail());
                    }
                }
                result = statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result > 0) {
            new FriendRepository().deleteAccountFromFriendsTable(findUserId(user.getEmail()));
            deleteAccountFromMessages(findUserId(user.getEmail()));
            return true;
        } else {
            return false;
        }
    }

    public String getInformation(User user, String info) {

        ArrayList<User> users = getUsersfromTable();
        for (User item : users) {
            if ((item.getEmail()).equals(user.getEmail())) {

                if (info.equals("firstName")) {
                    return item.getFirstName();
                } else if (info.equals("lastName")) {
                    return item.getLastName();
                } else if (info.equals("gender")) {
                    return item.getGender();
                } else if (info.equals("phoneNumber")) {
                    return item.getPhoneNumber();
                } else if (info.equals("nickName")) {
                    return item.getNickname();
                } else if (info.equals("email")) {
                    return item.getEmail();
                }

            }
        }
        return "false";
    }

    public boolean findIfArraylistContainsTheUser(ArrayList<User> users, String email) {
        for (User item : users) {
            if (item.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public boolean isThereUsersData(ArrayList<UserInformation> usersInfo, User user) {
        for (UserInformation info : usersInfo) {
            if (info.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

    public boolean saveWorkAndEdu(UserInformation userInfo, User user) {
        String query;
        ArrayList<UserInformation> usersInfo = getWorkandEdufromTable();
        int result = 0;
        int id = findUserId(user.getEmail());
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            if (isThereUsersData(usersInfo, user) == true) {
                query = "update t_workandeducation set userWork = ? ,userUniversity = ?,userHighSchool = ? where userEmail =? and id=?";
            } else {
                query = "insert into t_workandeducation(userWork, userUniversity, userHighSchool, userEmail, id) values(?,?,?,?,?)";
            }
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, userInfo.getWork());
            statement.setString(2, userInfo.getUniversity());
            statement.setString(3, userInfo.getHighschool());
            statement.setString(4, user.getEmail());
            statement.setInt(5, id);
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

    public boolean saveIntroduceYourself(UserInformation userInfo, User user) {
        String query;
        ArrayList<UserInformation> usersInfo = getIntroduceYourselffromTable();
        int result = 0;
        int id = findUserId(user.getEmail());

        try {
            Connection connection = DBHelper.getInstance().getConnection();
            if (isThereUsersData(usersInfo, user) == true) {
                query = "update t_introduceyourself set live = ? ,hometown = ?,relationship = ? where mail =? and id=?";
            } else {
                query = "insert into t_introduceyourself(live, hometown, relationship, mail, id) values(?,?,?,?,?)";
            }
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userInfo.getLive());
            statement.setString(2, userInfo.getFrom());
            statement.setString(3, userInfo.getRelationship());
            statement.setString(4, user.getEmail());
            statement.setInt(5, id);
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

    public boolean uploadPhoto_databse(String url, int id, String s_timeDate) { //***

        String query = "insert into t_statusandphotos(id, photos, dateTime, type, softdelete) values(?,?,?,1,0)";

        try {

            Connection con = DBHelper.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, id);

            File theFile = new File(url);
            FileInputStream input = new FileInputStream(theFile);

            st.setInt(1, id);
            st.setBinaryStream(2, input);
            st.setString(3, s_timeDate);

            st.executeUpdate();

            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public boolean save_database_status(int id, String status, String s_timeDate) { // ***

        if (status.equals("")) {
            return false;
        }

        String query = "insert into fbook.t_statusAndPhotos(id,status,dateTime,type,softdelete) values(?,?,?,?,0)";

        try {
            Connection con = DBHelper.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, id);
            statement.setString(2, status);
            statement.setString(3, s_timeDate);
            statement.setInt(4, 0); // 0 ise durum oldugunu belirtir ...
            statement.executeUpdate();

            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete_database_statusAndPhotos(int id) { //***

            String query = "delete from t_statusandphotos where id = ?";

        try {

            Connection con = DBHelper.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, id);
            st.executeUpdate();

            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public boolean saveUserFavorites(UserInformation userInfo, User user) {
        String query;
        ArrayList<UserInformation> usersInfo = getUserFavoritefromTable();
        int result = 0;
        int id = findUserId(user.getEmail());
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            if (isThereUsersData(usersInfo, user) == true) {
                query = "update t_userfavorites set team = ? ,music = ?,singer = ?, film = ?, series = ?, book = ? where email =? and id=?";
            } else {
                query = "insert into t_userfavorites(team, music, singer, film, series, book ,email, id) values(?,?,?,?,?,?,?,?)";
            }

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userInfo.getTeam());
            statement.setString(2, userInfo.getMusic());
            statement.setString(3, userInfo.getSinger());
            statement.setString(4, userInfo.getFilm());
            statement.setString(5, userInfo.getSeries());
            statement.setString(6, userInfo.getBook());
            statement.setString(7, user.getEmail());
            statement.setInt(8, id);
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

    public boolean deleteInformation(int id) {

        String query;
        try {
            query = "update t_userfavorites set softdelete = 1 where id = ?";
            Connection con = DBHelper.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, id);
            st.executeUpdate();

            query = "update t_introduceyourself set softdelete = 1 where id = ?";
            con = DBHelper.getInstance().getConnection();
            st = con.prepareStatement(query);
            st.setInt(1, id);
            st.executeUpdate();

            query = "update t_workandeducation set softdelete = 1 where id = ?";
            con = DBHelper.getInstance().getConnection();
            st = con.prepareStatement(query);
            st.setInt(1, id);
            st.executeUpdate();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public ArrayList<UserInformation> getWorkandEdufromTable() {
        ArrayList<UserInformation> usersInfo = new ArrayList<>();
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            if (connection != null) {
                String query = "Select * from t_workandeducation where softdelete = 0";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    UserInformation info = new UserInformation();
                    info.setEmail(resultSet.getString("userEmail"));
                    info.setWork(resultSet.getString("userWork"));
                    info.setUniversity(resultSet.getString("userUniversity"));
                    info.setHighschool(resultSet.getString("userHighSchool"));
                    usersInfo.add(info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usersInfo;
    }

    public ArrayList<UserInformation> getIntroduceYourselffromTable() {
        ArrayList<UserInformation> usersInfo = new ArrayList<>();
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            if (connection != null) {
                String query = "Select * from t_introduceyourself where softdelete = 0";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    UserInformation info = new UserInformation();
                    info.setEmail(resultSet.getString("mail"));
                    info.setLive(resultSet.getString("live"));
                    info.setFrom(resultSet.getString("hometown"));
                    info.setRelationship(resultSet.getString("relationship"));
                    usersInfo.add(info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usersInfo;
    }

    public ArrayList<UserInformation> getUserFavoritefromTable() {
        ArrayList<UserInformation> usersInfo = new ArrayList<>();
        try {
            Connection connection = DBHelper.getInstance().getConnection();
            if (connection != null) {
                String query = "Select * from t_userfavorites where softdelete = 0";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    UserInformation info = new UserInformation();
                    info.setEmail(resultSet.getString("email"));
                    info.setTeam(resultSet.getString("team"));
                    info.setMusic(resultSet.getString("music"));
                    info.setSinger(resultSet.getString("singer"));
                    info.setFilm(resultSet.getString("film"));
                    info.setSeries(resultSet.getString("series"));
                    info.setBook(resultSet.getString("book"));
                    usersInfo.add(info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usersInfo;
    }

    public String findEmailFromId(int id) {
        try {
            Connection con = DBHelper.getInstance().getConnection();
            String query = "select * from t_user where id = ?";
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return rs.getString("email");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public ArrayList<User> getFollowingFromTable(String email) {
        int userid = findUserId(email);
        ArrayList<User> following = new ArrayList<>();
        ArrayList<Integer> followingId = new ArrayList<>();
        String query;
        try {
            query = "Select * from t_user_friendmapping where userid=? and softdelete=0";
            Connection connection = DBHelper.getInstance().getConnection();
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, userid);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    followingId.add(resultSet.getInt("friendid"));
                }

                query = "Select * from t_user where id=?";
                statement = connection.prepareStatement(query);
                for (int id : followingId) {
                    statement.setInt(1, id);
                    resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        User us = new User(resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getString("nickname"));
                        following.add(us);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return following;
    }

    public ArrayList<User> getFollowersFromTable(String email) {
        int userid = findUserId(email);
        ArrayList<User> followers = new ArrayList<>();
        ArrayList<Integer> followersId = new ArrayList<>();
        String query;
        try {
            query = "Select * from t_user_friendmapping where friendid=? and softdelete=0";
            Connection connection = DBHelper.getInstance().getConnection();
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, userid);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    followersId.add(resultSet.getInt("userid"));
                }
                query = "Select * from t_user where id=?";
                statement = connection.prepareStatement(query);
                for (int id : followersId) {
                    statement.setInt(1, id);
                    resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        User us = new User(resultSet.getString("firstname"), resultSet.getString("lastname"), resultSet.getString("nickname"));
                        followers.add(us);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return followers;
    }

   
    public User getUser(String email) {

        int userId = this.findUserId(email);

        String query = "select * from t_user where id = ?";

        try {

            Connection con = DBHelper.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                User us = new User();
                us.setFirstName(rs.getString("firstname"));
                us.setLastName(rs.getString("lastname"));
                us.setGender(rs.getString("gender"));
                us.setPassword(rs.getString("password"));
                us.setNickname(rs.getString("nickname"));
                us.setPhoneNumber(rs.getString("phonenumber"));

                return us;

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return null;
    }

    public User getUser_withNickname(String nickname) {

        String query = "select * from t_user where nickname = ?";

        try {

            Connection con = DBHelper.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, nickname);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                User us = new User();

                us.setFirstName(rs.getString("firstname"));
                us.setLastName(rs.getString("lastname"));
                us.setNickname(rs.getString("nickname"));
                us.setEmail(rs.getString("email"));

                return us;

            }

        } catch (Exception ex) {

            ex.printStackTrace();
            return null;
        }

        return null;

    }

}
