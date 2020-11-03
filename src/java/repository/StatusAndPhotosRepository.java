/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import tableClasses.tableStatusAndPhotos;
import users.DBHelper;
import users.User;

/**
 *
 * @author Mert
 */
public class StatusAndPhotosRepository {

     public ArrayList<tableStatusAndPhotos> getStatusAndPhotosOfFollowingsFromDB(int userId) {

        String query;
        ArrayList<tableStatusAndPhotos> statusesAndPhotos = new ArrayList<>();

        query = "select * from t_statusandphotos where  id = ? or id in(select friendid from t_user_friendmapping  where userid = ?)   order by `dateTime` desc;";

        try {

            Connection con = DBHelper.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, userId);
            st.setInt(2, userId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                tableStatusAndPhotos temp = new tableStatusAndPhotos();

                temp.setId(rs.getInt("id"));
                temp.setStatus(rs.getString("status"));

                if (rs.getInt("type") == 1) {
                    Blob blob = rs.getBlob("photos");
                    temp.setUrl(getImage(blob));
                    temp.setIsPhoto(true);
                    temp.setIsStatus(false);
                } else {
                    temp.setIsStatus(true);
                    temp.setIsPhoto(false);
                }

                temp.setDateTime(rs.getString("dateTime"));
                temp.setType(rs.getInt("type"));
                temp.setPicStatusId(rs.getInt("picStatusId"));

                User user = this.getUser(rs.getInt("id"));

                temp.setNickname(user.getNickname());

                temp.setDoubleLikeCount(rs.getInt("doublelikedcount"));
                temp.setLikeCount(rs.getInt("likedcount"));
                temp.setUnlikeCount(rs.getInt("unlikedcount"));

                statusesAndPhotos.add(temp);

            }

            return statusesAndPhotos;

        } catch (Exception ex) {
            ex.printStackTrace();

            return null;
        }

    }
    
    
    public boolean delete_profilePhoto(int userId) {

        String query = "update t_profilephoto set softdelete = 1 where userid = ?";

        try {

            Connection con = DBHelper.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, userId);
            st.executeUpdate();

            con.close();

            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public boolean uploadProfilePhoto_db(String url, int userId) {

        String query1 = "select * from t_profilephoto where userid = ? and softdelete = 0";
        String query2 = "insert into t_profilephoto (userid, profilePhoto) values(?,?) ";
        String query3 = "update t_profilephoto set profilePhoto = ? where userid=?";

        int count = 0;

        try {

            Connection con = DBHelper.getInstance().getConnection();
            PreparedStatement st1 = con.prepareStatement(query1);
            st1.setInt(1, userId);
            ResultSet rs = st1.executeQuery();

            while (rs.next()) {
                count++;
            }

            File theFile = new File(url);
            FileInputStream input = new FileInputStream(theFile);

            if (count == 0) { // ekle ...

                PreparedStatement st_add = con.prepareStatement(query2);
                st_add.setInt(1, userId);
                st_add.setBinaryStream(2, input);

                st_add.executeUpdate();

                return true;

            } else { // guncelle ...

                PreparedStatement st_update = con.prepareStatement(query3);
                st_update.setBinaryStream(1, input);
                st_update.setInt(2, userId);

                st_update.executeUpdate();

                return true;

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public User getUser(int id) {

        String query = "select * from t_user where id = ?";
        User user = null;
        try {

            Connection con = DBHelper.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                user = new User(rs.getString("firstname"), rs.getString("lastname"),
                        rs.getString("email"), rs.getString("phonenumber"), rs.getString("password"),
                        rs.getString("gender"), rs.getString("nickname"));

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return user;
    }

   

    public ArrayList<tableStatusAndPhotos> get_statusAndPhotos_from_database_for_profile(int userId) {

        ArrayList<tableStatusAndPhotos> arr = new ArrayList<>();
        String query = "select * from t_statusandphotos where softdelete = 0 and id= ? order by dateTime desc";

        try {

            Connection con = DBHelper.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                tableStatusAndPhotos temp = new tableStatusAndPhotos();

                temp.setId(rs.getInt("id"));
                temp.setStatus(rs.getString("status"));

                if (rs.getInt("type") == 1) {
                    Blob blob = rs.getBlob("photos");
                    temp.setUrl(getImage(blob));
                    temp.setIsPhoto(true);
                    temp.setIsStatus(false);
                } else {
                    temp.setUrl("a");

                    temp.setIsStatus(true);
                    temp.setIsPhoto(false);
                }

                temp.setDateTime(rs.getString("dateTime"));
                temp.setType(rs.getInt("type"));
                temp.setPicStatusId(rs.getInt("picStatusId"));

                User user = this.getUser(rs.getInt("id"));

                temp.setNickname(user.getNickname());

                temp.setDoubleLikeCount(rs.getInt("doublelikedcount"));
                temp.setLikeCount(rs.getInt("likedcount"));
                temp.setUnlikeCount(rs.getInt("unlikedcount"));

                arr.add(temp);

            }
            con.close();
            return arr;

        } catch (Exception ex) {
            ex.printStackTrace();

            return null;
        }

    }

    public ArrayList<tableStatusAndPhotos> get_statusAndPhotos_from_database() {

        ArrayList<tableStatusAndPhotos> arr = new ArrayList<>();
        String query = "select * from t_statusandphotos where softdelete = 0 order by dateTime desc";

        try {

            Connection con = DBHelper.getInstance().getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {

                tableStatusAndPhotos temp = new tableStatusAndPhotos();

                temp.setId(rs.getInt("id"));
                temp.setStatus(rs.getString("status"));

                if (rs.getInt("type") == 1) {
                    Blob blob = rs.getBlob("photos");
                    temp.setUrl(getImage(blob));
                    temp.setIsPhoto(true);
                    temp.setIsStatus(false);
                } else {
                    temp.setUrl("a");

                    temp.setIsStatus(true);
                    temp.setIsPhoto(false);
                }

                temp.setDateTime(rs.getString("dateTime"));
                temp.setType(rs.getInt("type"));
                temp.setPicStatusId(rs.getInt("picStatusId"));

                User user = this.getUser(rs.getInt("id"));

                temp.setNickname(user.getNickname());

                temp.setDoubleLikeCount(rs.getInt("doublelikedcount"));
                temp.setLikeCount(rs.getInt("likedcount"));
                temp.setUnlikeCount(rs.getInt("unlikedcount"));

                arr.add(temp);

            }

            return arr;

        } catch (Exception ex) {
            ex.printStackTrace();

            return null;
        }

    }

    public void putPicToLocalFile(int picStatusId, String filepath) {
        String query = "select photos from t_statusandphotos where picStatusId=? and type = 1";
        try {
            Connection conn = DBHelper.getInstance().getConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, picStatusId);
            ResultSet resultset = statement.executeQuery();

            File file = new File(filepath);
            FileOutputStream fos = new FileOutputStream(file);

            while (resultset.next()) {
                InputStream input = resultset.getBinaryStream("photos");
                byte[] buffer = new byte[1024];
                while (input.read(buffer) > 0) {
                    fos.write(buffer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String profilePhoto_to_localFile(int userId) {
        String url = null;
        String query = "select * from t_profilephoto where userid = ?";

        try {

            Connection con = DBHelper.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement(query);
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Blob image = rs.getBlob("profilephoto");
                url = getImage(image);

            }

        } catch (Exception ex) {

            ex.printStackTrace();

        }
        return url;
    }

    public String getImage(Blob image) {
        byte[] blobAsBytes = null;
        ByteArrayOutputStream byteArrayImageOutputStream = new ByteArrayOutputStream();
        String imageByteArrayInString = null;
        try {

            int blobLength = (int) image.length();
            blobAsBytes = image.getBytes(1, blobLength);
            ByteArrayInputStream imageInputStream = new ByteArrayInputStream(blobAsBytes);
            ImageIO.write(ImageIO.read(imageInputStream), "jpg", byteArrayImageOutputStream);

            byteArrayImageOutputStream.flush();
            imageByteArrayInString = DatatypeConverter.printBase64Binary(byteArrayImageOutputStream.toByteArray());
            image.free();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageByteArrayInString;
    }

}
