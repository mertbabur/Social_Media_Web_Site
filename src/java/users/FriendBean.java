/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package users;

import java.util.ArrayList;
import repository.UserRepository;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import repository.FriendRepository;
import tableClasses.tableStatusAndPhotos;

/**
 *
 * @author Elif
 */
@ManagedBean(name = "friend")
@ApplicationScoped
public class FriendBean {

    String email;
    String nickname;
    String firstName;
    String lastName;
    String gender;

    @ManagedProperty(value = "#{user}")
    User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public FriendBean() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String addFriend(String userEmail, String friendNickname) {
        FriendRepository fr = new FriendRepository();
        fr.addFriend(userEmail, friendNickname);
        return null;

    }

    public ArrayList<User> getFriendsList(String userEmail) {
        ArrayList<User> friends = new ArrayList<>();
        FriendRepository fr = new FriendRepository();
        friends = fr.getFriendsList(userEmail);
        return friends;
    }

    public String deleteFriend(String userEmail, String friendNickname) {
        FriendRepository friend = new FriendRepository();
        friend.unfollow(userEmail, friendNickname);
        return null;
    }

    public String moveToProfilePageOfAnotherUser(User user) {
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.nickname = user.nickname;
        this.email = user.email;
        return "profilePageOfAnotherUser?faces-redirect=true";
    }

    public String moveToProfilePageOfAnotherUser_wthNick(String nickname) {

        User us = new UserRepository().getUser_withNickname(nickname);
        if (us.email.equals(user.getEmail())) {
            return "profilPage?faces-redirect=true";
        }
        this.firstName = us.firstName;
        this.lastName = us.lastName;
        this.nickname = us.nickname;
        this.email = us.email;
        return "profilePageOfAnotherUser?faces-redirect=true";
    }

    public String moveToProfilePageOfFriends(User user) {
        UserRepository ur = new UserRepository();
        ur.findEmailFromId(ur.findUserIdFromNickname(user.nickname));
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.nickname = user.nickname;
        this.email = ur.findEmailFromId(ur.findUserIdFromNickname(user.nickname));
        return "profilePageOfAnotherUser?faces-redirect=true";
    }

    public boolean isFollowing(String email, String nickname) {
        return (new FriendRepository().isFollowing(email, nickname));
    }
}
