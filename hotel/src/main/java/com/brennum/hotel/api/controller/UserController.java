package com.brennum.hotel.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.brennum.hotel.api.model.User;
import com.brennum.hotel.db.UserRepository;

@Controller 
@RequestMapping(path="/user") 
public class UserController {
  @Autowired 
  private UserRepository userRepository;

  @PostMapping(path="/add") 
  @PreAuthorize("hasAuthority('SCOPE_writeuser')")
  public @ResponseBody String addNewUser (
      @RequestParam String name, 
      @RequestParam String email,
      @RequestParam String password
      ) {
    User n = new User();
    n.setName(name);
    n.setEmail(email);
    userRepository.save(n);
    return "Saved";
  }



  @GetMapping(path="/{id}")
  @PreAuthorize("hasAuthority('SCOPE_readuser')")
    public @ResponseBody User getUser(@PathVariable int id) {
    return userRepository.findById(id).get();
  }
}

