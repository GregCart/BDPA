/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Classroom
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLt1 {
    
    private static String jdbc;
    private ResultSet rs;
    PreparedStatement statement;
    Connection con;
    String userName;
    String passCode;
    String FName;
    String LName;
    Date DOB;
    public static int instance;
    double userID;
    private ArrayList<Integer> usedNumbers;
    Random r;
    static int doing; //0 = login, 1 = Regester, 2 = verify
    boolean logged;
    boolean exists;
    
    public SQLt1(String userName, String passCode, String FName, String LName, Date DOB) {
        this.userName = userName;
        this.passCode = passCode;
        this.FName = FName;
        this.LName = LName;
        this.DOB = DOB;
        r = new Random();
        usedNumbers = new ArrayList();
        userID = randNum();
        logged = false;
        exists = false;
    }
    
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static void main(String args[]) throws ClassNotFoundException {
        SQLt1 test = new SQLt1("admin", "gabi2001", "Kom", "One", new Date(2016, 10, 8));
        if (instance == instance) {
            instance++;
        } else {
            instance = 0000;
        }
        switch (doing) {
            case 0: 
                test.connectSQL();
                test.checkExist();
                test.closeAll();
                break;
            case 1:
                test.connectSQL();
                test.insertUserData();
                test.closeAll();
                break;
            case 2:
                test.connectSQL();
                test.checkExist();
                test.closeAll();
                break;
        }
    };
    
    public Connection connectSQL() {
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println(e.toString());
            }
            String url = "jdbc:mysql://localhost:3306/";
            String usrName = "root";
            String passKey = "p@ssw0rd";
            con = DriverManager.getConnection(url , usrName , passKey);            
        } catch (SQLException e) {
            Logger.getLogger(SQLt1.class.getName()).log(Level.SEVERE, null, e);
        }
        return con;
    };
    
    public ResultSet getUserData() {
        try {
            statement = con.prepareStatement("SELECT * FROM sqltest1.test_login1 WHERE usrName = " + userName + ";");
            rs = statement.executeQuery();
        } catch (SQLException e) {
            Logger.getLogger(SQLt1.class.getName()).log(Level.SEVERE, null, e);
        }
        logged = true;
        return rs;
    };
    
    public void insertUserData() {
        try {
            checkExist();
            getUserData();
            statement = con.prepareStatement("INSERT INTO (usrName, passWord, name, DOB, usrID) VALUES ( '" + userName + "', '" + passCode + "', '" + (LName + "," + FName) + "', '" + DOB + "', '" + userID + "')");
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(SQLt1.class.getName()).log(Level.SEVERE, null, e);
        }
        logged = true;
    };
    
    public void checkExist() {
        try {
            getUserData();
            if (rs.getString("usrName") == null ? userName != null : !rs.getString("usrName").equals(userName)) {
                logged = false;
                exists = false;
            } else if (rs.getString("usrName") == null ? userName != null : rs.getString("usrName").equals(userName) && logged == false) {
                exists = true;
                logged = true;
            } else {
                exists = true;
                logged = false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLt1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void closeAll() {
        try {
            rs.close();
            statement.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLt1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int randNum() {
        int res = r.nextInt(10000) + 1;
        if (usedNumbers.contains(res)) {
            randNum();
        } else {
            usedNumbers.add(res);
        }
        return res;
    }
}