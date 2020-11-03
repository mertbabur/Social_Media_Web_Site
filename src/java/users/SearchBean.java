/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package users;
import java.util.ArrayList;
import javax.faces.bean.ApplicationScoped;
import repository.UserRepository;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Elif
 */
@ManagedBean(name = "search")
@ApplicationScoped
public class SearchBean {

    @ManagedProperty(value = "#{User}")
    String valueToSearch;

    public SearchBean() {
    }

    public String getValueToSearch() {
        return valueToSearch;
    }

    public void setValueToSearch(String valueToSearch) {
        this.valueToSearch = valueToSearch;
    }

    public ArrayList<User> findUsers(String userEmail){
        ArrayList<User> users;
        UserRepository ur = new UserRepository();
        users = ur.findValuesFromSearch(valueToSearch, userEmail);
        return users;
    }
    
}
