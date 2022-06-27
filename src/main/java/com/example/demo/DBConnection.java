package com.example.demo;

import java.sql.*;
import java.util.HashMap;
import java.util.Random;

public class DBConnection { // all the database-accesses and database-modifications are being made here


    private Connection con = null;
    private PreparedStatement pst = null;
    private String userSql = "postgres",
                   password = "260795",
                    url = "jdbc:postgresql://localhost:5432/user";

    public DBConnection() {}

    public void deleteFromDatabaseById(Integer id) {

        String stm = "DELETE FROM \"user\" WHERE ID=" + id;

        try {
            con = DriverManager.getConnection(url, userSql, password);
            pst = con.prepareStatement(stm);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Some error" + e.toString());
        }
    }

    public void insertElementInDatabaseById(Integer id, String address, String firstName, String lastName, String photo) {

        String stm = "INSERT INTO \"user\"(ID,address,first_name,last_name,photo) VALUES(?,?,?,?,?) WHERE ID=" + id;

        try {
            con = DriverManager.getConnection(url, userSql, password);
            pst = con.prepareStatement(stm);
            pst.setInt(1, id);
            pst.setString(2, address);
            pst.setString(3, firstName);
            pst.setString(4, lastName);
            pst.setString(5, photo);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error" + e.toString());
        }
    }

    public void insertElementInDatabase(Integer id, String address, String firstName, String lastName, String photo){

        String stm = "INSERT INTO \"user\"(ID,address,first_name,last_name,photo) VALUES(?,?,?,?,?)";

        try {
            con = DriverManager.getConnection(url, userSql, password);
            pst = con.prepareStatement(stm);
            pst.setInt(1, id);
            pst.setString(2, address);
            pst.setString(3, firstName);
            pst.setString(4, lastName);
            pst.setString(5, photo);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error" + e.toString());
        }
    }

    public boolean isAccountDatabaseEmpty(String username) {

        String stm = "SELECT * FROM \"account\" WHERE user_name='" + username + "'";
        try {
            con = DriverManager.getConnection(url, userSql, password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(stm);
            while (rs.next()) {
                rs.close();
                con.close();
                return true;
            }
            rs.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Error" + e.toString());
        }
        return false;


    }

    public void insertIntoAccountDatabase(Integer id, String userName, String userPassword) {

        String stm = "INSERT INTO \"account\"(ID,user_name,password) VALUES(?,?,?)";

        try {
            con = DriverManager.getConnection(url, userSql, password);
            pst = con.prepareStatement(stm);
            pst.setInt(1, new Random().nextInt(0, 1000000));
            pst.setString(2, userName);
            pst.setString(3, userPassword);
            pst.executeUpdate();
            con.close();
            pst.close();
        } catch (Exception e) {
            System.out.println("Error" + e.toString());
        }
    }

    public HashMap<String, String> getPairUserNameAndPasswordFromAccountDatabase() {

        HashMap<String, String> userAndPasswords = new HashMap<>();

        String stm = "SELECT user_name, password FROM \"account\"";
        try {
            con = DriverManager.getConnection(url, userSql, password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(stm);
            while (rs.next()) {
                userAndPasswords.put(rs.getString(1), rs.getString(2));
            }
            rs.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Error" + e.toString());
        }

        return userAndPasswords;
    }
}
