package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Account { // class for storing logIn and AccountCreation informations

    @Id
    private Integer Id;

    private String userName, password, role = "USER";
    public static Integer totalUsers = 0;

    Account() {}

    public Account(String userName, String password, Integer Id) {
        totalUsers++;
        this.userName = userName;
        this.password = password;
        this.Id = Id;
    }

    public String getRole() { return role; }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Integer getId() {
        return Id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Integer id) {
        Id = id;
    }
}
