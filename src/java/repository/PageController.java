/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Elif
 */
@ManagedBean(name = "pageController")
@RequestScoped
public class PageController {

    public String moveToFindUsersPage() {
        return "findUsersPage?faces-redirect=true";
    }

    public String moveToMainPage() {
        return "mainPage?faces-redirect=true";
    }

    public String moveToProfilePage() {
        return "profilPage?faces-redirect=true";
    }

    public String moveToSettingsPage() {
        return "settingsPage?faces-redirect=true";
    }

    public String moveToWorkAndEducationPage() {
        return "workAndEducationPage?faces-redirect=true";
    }

    public String moveToIntroduceYourselfPage() {
        return "introduceYourselfPage?faces-redirect=true";
    }

    public String moveToUsersFavoritePage() {
        return "usersFavoritePage?faces-redirect=true";
    }

    public String moveToSeeInformationPage() {
        return "seeInformationPage?faces-redirect=true";
    }

    public String moveToChatPage() {
        return "chatPage?faces-redirect=true";
    }

    public String moveToFollowingPage() {
        return "followingPage?faces-redirect=true";
    }
    public String moveToFollowersPage() {
        return "followersPage?faces-redirect=true";
    }
    public String moveToChangePasswordPage() {
        return "changePasswordPage?faces-redirect=true";
    }
    public String moveToNotificationsPage() {
        return "notificationsPage?faces-redirect=true";
    }
}
