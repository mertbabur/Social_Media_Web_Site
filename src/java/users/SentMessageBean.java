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
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import repository.FriendRepository;
import repository.MessageRepository;
import repository.UserRepository;

/**
 *
 * @author Elif
 */
@ManagedBean(name = "sentmessage")
@RequestScoped
public class SentMessageBean {

    String dateSent;
    int fromUserId;
    int toUserId;
    String userNicknameFrom;
    String userNicknameTo;
    String message;

    public SentMessageBean() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date timeDate = new Date();
        dateSent = dateFormat.format(timeDate);
    }

    public SentMessageBean(int fromUserId, int toUserId, String message) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.message = message;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date timeDate = new Date();
        dateSent = dateFormat.format(timeDate);
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserNicknameFrom() {
        this.userNicknameFrom = new FriendRepository().findNicknameFromId(fromUserId);
        return userNicknameFrom;
    }

    public void setUserNicknameFrom(String userNicknameFrom) {
        this.userNicknameFrom = userNicknameFrom;
    }

    public String getUserNicknameTo() {
        this.userNicknameTo = new FriendRepository().findNicknameFromId(toUserId);
        return userNicknameTo;
    }

    public void setUserNicknameTo(String userNicknameTo) {
        this.userNicknameTo = userNicknameTo;
    }

    public String saveMessage(String fromEmail, String toNickname) {
        
        if(toNickname.equals("")){
            FacesMessage msg1 = new FacesMessage("Select friend to sent the message first!");
            msg1.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, msg1);
            return null;
        }
        UserRepository ur = new UserRepository();
        int fromId = ur.findUserId(fromEmail);
        int toId = ur.findUserIdFromNickname(toNickname);
        this.toUserId = toId;
        this.fromUserId = fromId;
        MessageRepository mr = new MessageRepository();
        if (mr.saveMessage(this) == true) {
            FacesMessage msg1 = new FacesMessage("Message sent!");
            msg1.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, msg1);
            return null;
        } else {
            FacesMessage msg1 = new FacesMessage("Message couldn't send!");
            msg1.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, msg1);
            return null;
        }
    }


    public ArrayList<Message> findMessages(String userEmail){
        ArrayList<Message> messages = new MessageRepository().findMessages(userEmail);
        return messages;
    }
    
    public String moveToChatWithFriendPage(Message message){
        toUserId = message.toUserId;
        return "chatWithFriendPage?faces-redirect=true"; 
    }
    
}
