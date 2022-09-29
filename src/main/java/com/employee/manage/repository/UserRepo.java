package com.employee.manage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import com.employee.manage.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public User findByEmail(String email);
     

}
