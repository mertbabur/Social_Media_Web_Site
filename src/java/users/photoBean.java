/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package users;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.servlet.http.Part;

/**
 *
 * @author Mert
 */
@ManagedBean(name = "photos")
@ApplicationScoped
public class photoBean {
    
    
    Part photo;
    
    @ManagedProperty(value = "#{user}")
    User user;

    

    public Part getPhoto() {
        return photo;
    }

    public void setPhoto(Part photo) {
        this.photo = photo;
    }

    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
}
    
    
    
    
    
    

