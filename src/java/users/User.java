/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package users;

import repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import repository.StatusAndPhotosRepository;

/**
 *
 * @author Elif
 */
@ManagedBean(name = "user")
@ApplicationScoped
public class User {

    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String password;
    String gender;
    String status;
    String nickname;
    String live;

    public String getLive() {
        return live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {
    }

    public User(String firstName, String lastName, String email, String phoneNumber, String password, String gender, String nickname) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.gender = gender;
        this.nickname = nickname;
    }

    public User(String firstName, String lastName, String nickname) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String register() {
        if (new UserRepository().save(this) == true) {

            new StatusAndPhotosRepository().uploadProfilePhoto_db("C:\\dev\\nypProje\\web\\resources\\profilePhotos\\nonPic.png", new UserRepository().findUserId(this.email));
            return "loginPage?faces-redirect=true";
        } else {
            return "registerPage?faces-redirect=true";
        }
    }

    public String login() {
        UserRepository ur = new UserRepository();
        ArrayList<User> users = ur.getUsersfromTable();
        for (User user : users) {
            if ((user.email).equals(this.email)) {
                if ((user.password).equals(this.password)) {
                    return "mainPage?faces-redirect=true";
                }
            }
        }

        FacesMessage msg = new FacesMessage("E-mail or password is wrong, please enter again!", "ERROR MSG");
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, msg);

        return "loginPage?faces-redirect=true";
    }

    public void deleteStatusAndPhotos() {

        int id = new UserRepository().findUserId(email);

        if (new UserRepository().delete_database_statusAndPhotos(id)) {
            return;
        } else {

            FacesMessage msg = new FacesMessage("Your status is not deleted !", "ERROR MSG");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public void deleteProfilePhoto() {

        int userId = new UserRepository().findUserId(this.email);

        if (new StatusAndPhotosRepository().delete_profilePhoto(userId)) {

            FacesMessage msg = new FacesMessage("The photo is deleted, successfuly !", "ERROR MSG");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);

        }

    }

    public String deleteAccount() {
        UserRepository ur = new UserRepository();
        if (ur.deleteAccount(this) == true) {
            deleteStatusAndPhotos();
            deleteInfo();
            deleteProfilePhoto();
            return "loginPage?faces-redirect=true";
        } else {
            return null;
        }
    }

    public String returnChangePasswordPage() {
        return "changePasswordPage?faces-redirect=true";
    }

    public ArrayList<User> getUsersList() {
        return (new UserRepository().getUsersfromTable());
    }

    /*
    public ArrayList<String> getStatusList(){
        return(new UserRepository().getStatusList());
    }
     */
    public String changePassword() {

        UserRepository ur = new UserRepository();
        if (ur.changePassword(this) == true) {
            return "mainPage?faces-redirect=true";
        } else {
            return "changePasswordPage?faces-redirect=true";
        }

    }

    public void save_status() {

        int id = new UserRepository().findUserId(getEmail());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date timeDate = new Date();
        String s_timeDate = dateFormat.format(timeDate);

        if (new UserRepository().save_database_status(id, status, s_timeDate)) {
            return;
        } else {
            FacesMessage msg = new FacesMessage("You cannot share empty status !", "ERROR MSG");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public String information(String a) {

        UserRepository ur = new UserRepository();
        return (ur.getInformation(this, a));

    }

    public void deleteInfo() {

        if (new UserRepository().deleteInformation(new UserRepository().findUserId(email))) {
            return;
        } else {
            System.out.println("silinemdi");
        }
    }

    public ArrayList<User> getNotifications(String email) {
        UserRepository ur = new UserRepository();
        ArrayList<User> users = ur.getFollowersFromTable(email);
        ArrayList<User> reverseusers = new ArrayList<>();
        for (int i = users.size() - 1; i >= 0; i--) {
            reverseusers.add(users.get(i));
        }

        return reverseusers;
    }

    public ArrayList<User> getUsersToUni(String email) {
        ArrayList<User> users = new UserRepository().getUserToUniversity(email);
        return users;
    }

    public ArrayList<User> getUsersToHighSch(String email) {
        ArrayList<User> users = new UserRepository().getUserToHighschool(email);
        return users;
    }

    public String showUserInfo() {

        User us = new UserRepository().getUser(this.email);

        this.firstName = us.getFirstName();
        this.lastName = us.getLastName();
        this.gender = us.getGender();
        this.nickname = us.getNickname();
        this.phoneNumber = us.getPhoneNumber();

        return "userInfo";

    }

    public ArrayList<User> getFollowing(String email) {
        UserRepository ur = new UserRepository();
        ArrayList<User> users = ur.getFollowingFromTable(email);
        return users;
    }

    public ArrayList<User> getFollowers(String email) {
        UserRepository ur = new UserRepository();
        ArrayList<User> users = ur.getFollowersFromTable(email);
        return users;
    }

}
