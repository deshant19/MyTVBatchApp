package com.mytvbatch.app.config;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.mytvbatch.app.model.User;
import com.mytvbatch.app.repository.UserRepository;

public class UserProcessor implements ItemProcessor<User, User> {
	
	@Autowired
    private UserRepository userRepository;

    public User process(User user) {
    	
    	List<User> users = userRepository.findAll();
    	
    	if(users.contains(user)) {
    		return null;
    	}
    	
    	return user;
    }
}
