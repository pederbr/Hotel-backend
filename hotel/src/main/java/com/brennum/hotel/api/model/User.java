package com.brennum.hotel.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class User {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Integer id;
  private String name;
  private String email;
  private String password;
  private boolean admin;
  private int loyaltyPoints;

  public User() {
    this.loyaltyPoints = 0;
    this.admin = false;
  }


  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  public int getLoyaltyPoints() {

    return loyaltyPoints;
  }

  public void setLoyaltyPoints(Integer loyaltyPoints) {
    if (this.loyaltyPoints - loyaltyPoints < 0) {
      throw new IllegalArgumentException("Loyalty points cannot be negative");
    }
    this.loyaltyPoints = loyaltyPoints;
  }
}