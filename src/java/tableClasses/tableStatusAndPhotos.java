/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tableClasses;

/**
 *
 * @author Mert
 */
public class tableStatusAndPhotos {
    
    private int id;
    private String status;
    private int picStatusId;
    private String url;
    private String dateTime;
    private int type;
    private String nickname;
    private String profilePhotoUrl;
    
    private boolean isPhoto;
    private boolean isStatus;
    
    private int doubleLikeCount;
    private int likeCount;
    private int unlikeCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPicStatusId() {
        return picStatusId;
    }

    public void setPicStatusId(int picId) {
        this.picStatusId = picId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }
    
    
    public boolean getIsPhoto() {
        return isPhoto;
    }

    public void setIsPhoto(boolean isPhoto) {
        this.isPhoto = isPhoto;
    }

    public boolean getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(boolean isStatus) {
        this.isStatus = isStatus;
    }

    public int getDoubleLikeCount() {
        return doubleLikeCount;
    }

    public void setDoubleLikeCount(int doubleLikeCount) {
        this.doubleLikeCount = doubleLikeCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getUnlikeCount() {
        return unlikeCount;
    }

    public void setUnlikeCount(int unlikeCount) {
        this.unlikeCount = unlikeCount;
    }
    
    
    
    
    
    
    
    
}
