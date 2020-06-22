package com.kadioglumf.primefaces.models;

import com.kadioglumf.primefaces.utils.AddressType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "adress")
public class Address implements Cloneable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phoneNumber;
    private int postalCode;
    private String country;
    private String city;
    private String detail;
    private AddressType addressType;
/*
    @OneToOne(mappedBy = "address")
    private User user;

 */
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    public Address() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", postalCode=" + postalCode +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", detail='" + detail + '\'' +
                ", addressType=" + addressType +
                ", user=" + user +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return (Address) super.clone();
    }
}
