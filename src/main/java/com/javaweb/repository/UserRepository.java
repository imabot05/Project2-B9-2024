package com.javaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaweb.repository.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{

}
