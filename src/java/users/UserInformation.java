/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package users;

import repository.UserRepository;


import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author buse
 */
@ManagedBean(name = "userinfo")
@ApplicationScoped
public class UserInformation {

    String work, university, highschool, email;
    String live, from, relationship;
    String team, music, singer, film, series, book;

    List<UserInformation> queryResultWorkandEdu;
    List<UserInformation> queryResultIntroduceYourself;
    List<UserInformation> queryResultUserFavorites;
    
    String followersCount, followingCount;

    public String getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(String followersCount) {
        this.followersCount = followersCount;
    }

    public String getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(String followingCount) {
        this.followingCount = followingCount;
    }

    public List<UserInformation> getQueryResultWorkandEdu() {
        return queryResultWorkandEdu;
    }

    public void setQueryResultWorkandEdu(List<UserInformation> queryResultWorkandEdu) {
        this.queryResultWorkandEdu = queryResultWorkandEdu;
    }

    public List<UserInformation> getQueryResultIntroduceYourself() {
        return queryResultIntroduceYourself;
    }

    public void setQueryResultIntroduceYourself(List<UserInformation> queryResultIntroduceYourself) {
        this.queryResultIntroduceYourself = queryResultIntroduceYourself;
    }

    public List<UserInformation> getQueryResultUserFavorites() {
        return queryResultUserFavorites;
    }

    public void setQueryResultUserFavorites(List<UserInformation> queryResultUserFavorites) {
        this.queryResultUserFavorites = queryResultUserFavorites;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getLive() {
        return live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getHighschool() {
        return highschool;
    }

    public void setHighschool(String highschool) {
        this.highschool = highschool;
    }

    public String addWorkAndEdu(User user) {
        if (new UserRepository().saveWorkAndEdu(this, user) == true) {
            return "seeInformationPage?faces-redirect=true";
        }
        return "profilPage?faces-redirect=true";
    }

    public String addIntrYou(User user) {
        if (new UserRepository().saveIntroduceYourself(this, user) == true) {
            return "seeInformationPage?faces-redirect=true";
        }
        return "profilPage?faces-redirect=true";
    }

    public String addUserFavorite(User user) {
        if (new UserRepository().saveUserFavorites(this, user) == true) {
            return "seeInformationPage?faces-redirect=true";
        }
        return "profilPage?faces-redirect=true";
    }

    public UserInformation getWorkAndEducationFromtable(User user) {
        UserRepository ur = new UserRepository();
        queryResultWorkandEdu = ur.getWorkandEdufromTable();
        for (UserInformation info : queryResultWorkandEdu) {
            if (user.getEmail().equals(info.getEmail())) {
                return info;
            }
        }
        return null;
    }

    public UserInformation getFriendWorkAndEducationFromtable(FriendBean user) {
        UserRepository ur = new UserRepository();
        queryResultWorkandEdu = ur.getWorkandEdufromTable();
        for (UserInformation info : queryResultWorkandEdu) {
            if (user.getEmail().equals(info.getEmail())) {
                return info;
            }
        }
        return null;
    }

    public UserInformation getIntroduceYourselfFromtable(User user) {
        UserRepository ur = new UserRepository();
        queryResultIntroduceYourself = ur.getIntroduceYourselffromTable();
        for (UserInformation info : queryResultIntroduceYourself) {
            if (user.getEmail().equals(info.getEmail())) {
                return info;
            }
        }
        return null;
    }

    public UserInformation getFriendIntroduceYourselfFromtable(FriendBean user) {
        UserRepository ur = new UserRepository();
        queryResultIntroduceYourself = ur.getIntroduceYourselffromTable();
        for (UserInformation info : queryResultIntroduceYourself) {
            if (user.getEmail().equals(info.getEmail())) {
                return info;
            }
        }
        return null;
    }

    public UserInformation getUserFavoriteFromtable(User user) {
        UserRepository ur = new UserRepository();
        queryResultUserFavorites = ur.getUserFavoritefromTable();
        for (UserInformation info : queryResultUserFavorites) {
            if (user.getEmail().equals(info.getEmail())) {
                return info;
            }
        }
        return null;
    }

    public UserInformation getFriendFavoriteFromtable(FriendBean user) {
        UserRepository ur = new UserRepository();
        queryResultUserFavorites = ur.getUserFavoritefromTable();
        for (UserInformation info : queryResultUserFavorites) {
            if (user.getEmail().equals(info.getEmail())) {
                return info;
            }
        }
        return null;
    }
    
    
    public String getFollowingCount(String email){
        followingCount = String.valueOf(new UserRepository().getFollowingCountFromTable(email));
        return followingCount;
    }
    public String getFollowersCount(String email){
        followersCount = String.valueOf(new UserRepository().getFollowersCountFromTable(email));
        return followersCount;
    }
}
