package users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Elif
 */
public class DBHelper {

    String url = "jdbc:mysql://localhost:3306/?user=root";
    private static DBHelper instance;
    private Connection connection;

    private DBHelper() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(url);
            this.connection.setCatalog("fbook");
        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DBHelper getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBHelper();
        } else if (instance.getConnection().isClosed()) {
            instance = new DBHelper();
        }

        return instance;
    }
}

