package com.klu.springmvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klu.springmvc.model.User;

@Repository
public interface User_Repository extends JpaRepository<User, Integer> {

User findByUsername(String user);
}
