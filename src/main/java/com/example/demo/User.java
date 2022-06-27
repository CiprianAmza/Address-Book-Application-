package com.example.demo;


import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class User { // class for storing the addressBookUsers informations

    private String firstName, lastName, address, photo;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer ID;

    User() {}

    User(String firstName, String lastName, String address, String photo, Integer ID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.photo = photo;
        this.ID = ID;
    }

    public Integer getID() {
        return ID;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", photo='" + photo + '\'' +
                ", ID=" + ID +
                '}';
    }
}
