package com.mytvbatch.app.handler;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mytvbatch.app.exception.ResourceNotFoundException;
import com.mytvbatch.app.model.User;

public class UserHelper {
	
	private static final String regEx = "^[1-9]\\d*$";
	
	public static boolean isNullOrBlank(String input) {
		if(input.isBlank() || null == input) {
			return true;
		}
		return false;
	}
	
	public static boolean isPositiveInt(int id) {
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(id + "");
		return matcher.matches();
	}
	
	public static void isListEmpty(List<User> userList) {
		if (userList.isEmpty()) {
            throw new ResourceNotFoundException("Users not found.");
        }
	}
}
