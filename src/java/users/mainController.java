/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package users;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.Part;
import repository.LikeUnlikeRepository;
import repository.StatusAndPhotosRepository;
import repository.UserRepository;
import tableClasses.tableStatusAndPhotos;

/**
 *
 * @author Mert
 */
@ManagedBean(name = "control")
@ApplicationScoped

public class mainController{

    private ArrayList<tableStatusAndPhotos> statusAndPhotosArr;

    @ManagedProperty(value = "#{user}")
    User user;

    @ManagedProperty(value = "#{photos}")
    photoBean photos;
    
    

    
   
 
    public mainController(){
        
        statusAndPhotosArr = new StatusAndPhotosRepository().get_statusAndPhotos_from_database();
        
       
    }

    public ArrayList<tableStatusAndPhotos> getStatusAndPhotosArr() {
        return statusAndPhotosArr;
    }

    public void setStatusAndPhotosArr(ArrayList<tableStatusAndPhotos> statusAndPhotosArr) {
        this.statusAndPhotosArr = statusAndPhotosArr;
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

   
    public ArrayList<tableStatusAndPhotos> getStatusAndPhotosOfFollowingsFromDB(String email){
        return new StatusAndPhotosRepository().getStatusAndPhotosOfFollowingsFromDB(new UserRepository().findUserId(email));
    }
    
    public String getFileName(Part part)
    {
        for(String cd:photos.photo.getHeader("content-disposition").split(";"))
            if(cd.trim().startsWith("filename")){
                String filename=cd.substring(cd.indexOf('=')+1).trim().replace("\"", "");
                return filename;
            }
        
        return "";

    }

    public String save_db_photoStatus(ActionEvent event) {

        if (event.getComponent().getId().equals("share")) {

            int id = new UserRepository().findUserId(user.email);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date timeDate = new Date();
            String s_timeDate = dateFormat.format(timeDate);

            if (new UserRepository().save_database_status(id, user.status, s_timeDate)) {
               
                this.statusAndPhotosArr = new StatusAndPhotosRepository().get_statusAndPhotos_from_database();
                return "mainPage?faces-redirect=true";
            } else {
                FacesMessage msg = new FacesMessage("You cannot share empty status !", "ERROR MSG");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }

        } else if (event.getComponent().getId().equals("upload")) {

            int id = new UserRepository().findUserId(user.getEmail());
            String url = getFileName(photos.photo);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date timeDate = new Date();
            String s_timeDate = dateFormat.format(timeDate);

            if (new UserRepository().uploadPhoto_databse(url, id, s_timeDate)) {

                FacesMessage msg = new FacesMessage("The photo is upload, successfully !", "ERROR MSG");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, msg);

                this.statusAndPhotosArr = new StatusAndPhotosRepository().get_statusAndPhotos_from_database();

            } else {

                FacesMessage msg = new FacesMessage("The photo is not upload :|", "ERROR MSG");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, msg);

            }

        } else {

        }
        return "mainPage?faces-redirect=true";
}
    
    public void saveDBlikeunlike( int likeType, int photoStatusId){
        
        
        if(new LikeUnlikeRepository().doubleLike_save_db(likeType, photoStatusId)){
            this.statusAndPhotosArr = new StatusAndPhotosRepository().get_statusAndPhotos_from_database();
        }
        
    }


}   
