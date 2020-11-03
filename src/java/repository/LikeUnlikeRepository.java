/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import users.DBHelper;

/**
 *
 * @author Mert
 */
public class LikeUnlikeRepository {
    
    public boolean doubleLike_save_db(int type, int photoStatusId){
        
        
        
        String query = null;
        
        if(type == 1){
            
            query = "update t_statusandphotos set doublelikedcount = doublelikedcount +1 where picStatusid = ?";
            
            
        }
        if(type == 2){

            query = "update t_statusandphotos set likedcount = likedcount +1 where picStatusid = ?";
            
        }
        if(type == 3){

            query = "update t_statusandphotos set unlikedcount = unlikedcount +1 where picStatusid = ?";
            
        }
       
        try{
            
            Connection con = DBHelper.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement(query);
            
            
            
            st.setInt(1, photoStatusId);
           
            st.executeUpdate();
            
            return true;
            
        }catch(Exception ex){
            
            return false;
            
        }
        
        
    }
    
    
}
