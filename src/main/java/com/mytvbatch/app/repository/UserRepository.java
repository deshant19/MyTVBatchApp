package com.mytvbatch.app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.mytvbatch.app.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	
	List<User> findAll();
}
