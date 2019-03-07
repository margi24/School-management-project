package dataB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBhandler extends Config {
    Connection dbConection;
    public Connection getConnection(){
        String connection="jdbc:mysql://localhost/" + Config.dbname + "?user=" + Config.dbuser + "&password=" + Config.dbpass + "&useUnicode=true&characterEncoding=UTF-8";
        //"jdbc:mysql://"+ Config.dbhost+"/"+Config.dbport+"/"+Config.dbname+"?autoReconnect=true&useSSL=false";
        try {
            Class.forName("com.mysql.jdbc.Driver");
           // dbConection=DriverManager.getConnection("jdbc:mysql://127.0.0.1/3306/Scoala",Config.dbuser,Config.dbpass);
           // dbConection=DriverManager.getConnection(connection,Config.dbuser,Config.dbpass);
            dbConection=DriverManager.getConnection(connection);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dbConection;
    }
}
