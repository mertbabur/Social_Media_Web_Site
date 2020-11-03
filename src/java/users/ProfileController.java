/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package users;

import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import org.hibernate.validator.constraints.Email;
import repository.StatusAndPhotosRepository;
import repository.UserRepository;
import tableClasses.tableStatusAndPhotos;

/**
 *
 * @author Mert
 */
@ManagedBean(name = "controlProfile")
@ApplicationScoped
public class ProfileController {

    @ManagedProperty(value = "#{user}")
    User user;

    @ManagedProperty(value = "#{photos}")
    photoBean photos;

    @ManagedProperty(value = "#{friend}")
    FriendBean friend;

    public ArrayList<tableStatusAndPhotos> getProfileArr() {

        ArrayList<tableStatusAndPhotos> statusAndPhotosArr;

        int userId = new UserRepository().findUserId(user.email);

        statusAndPhotosArr = new StatusAndPhotosRepository().get_statusAndPhotos_from_database_for_profile(userId);

        return statusAndPhotosArr;
    }

    public ArrayList<tableStatusAndPhotos> getProfileArrOfAnotherUser(String nickname) {

        ArrayList<tableStatusAndPhotos> statusAndPhotosArr;

        int userId = new UserRepository().findUserIdFromNickname(nickname);

        statusAndPhotosArr = new StatusAndPhotosRepository().get_statusAndPhotos_from_database_for_profile(userId);

        return statusAndPhotosArr;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public photoBean getPhotos() {
        return photos;
    }

    public void setPhotos(photoBean photos) {
        this.photos = photos;
    }

    public FriendBean getFriend() {
        return friend;
    }

    public void setFriend(FriendBean friend) {
        this.friend = friend;
    }

    public String getFileName(Part part) {
        for (String cd : photos.photo.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename;
            }
        }

        return "";

    }

    public void save_db_profilePhoto() {

        int id = new UserRepository().findUserId(user.getEmail());
        String url = getFileName(photos.photo);

        if (new StatusAndPhotosRepository().uploadProfilePhoto_db(url, id)) {

            FacesMessage msg = new FacesMessage("The photo is upload, successfully !", "ERROR MSG");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage("The photo is not upload :|", "ERROR MSG");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public String showProfilePhoto() {

        int userId = new UserRepository().findUserId(user.email);

        return (new StatusAndPhotosRepository().profilePhoto_to_localFile(userId));

    }

    public String showProfilePhoto(int userId) {

        return (new StatusAndPhotosRepository().profilePhoto_to_localFile(userId));

    }

    public String showProfilePhotos(String nickname) {

        return (new StatusAndPhotosRepository().profilePhoto_to_localFile(new UserRepository().findUserIdFromNickname(nickname)));

    }

    public String showProfilePhotosInChatPage(Message message) {
        if (message.fromUserId == new UserRepository().findUserId(user.getEmail())) {
            return (new StatusAndPhotosRepository().profilePhoto_to_localFile(message.toUserId));
        } else {
            return (new StatusAndPhotosRepository().profilePhoto_to_localFile(message.fromUserId));
        }

    }

    public String showFriendProfilePhoto() {

        int userId = new UserRepository().findUserId(friend.email);

        return (new StatusAndPhotosRepository().profilePhoto_to_localFile(userId));

    }

}
