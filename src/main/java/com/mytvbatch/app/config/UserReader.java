package com.mytvbatch.app.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.mytvbatch.app.model.User;

@Component
public class UserReader extends JdbcCursorItemReader<User> implements ItemReader<User>{
	
	public UserReader(@Autowired DataSource dataSource) {
		setDataSource(dataSource);
		setSql("SELECT * FROM user");
		setFetchSize(100);
		setRowMapper(new UserRowMapper());
	}
	
	public class UserRowMapper implements RowMapper<User> {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user  = new User();
			user.setId(rs.getInt("id"));
			user.setFname(rs.getString("name"));
			user.setLname(rs.getString("fname"));
			user.setEmailId(rs.getString("lname"));
			user.setService(rs.getString("service"));
			user.setAddedOn(rs.getString("addedOn"));
			user.setUpdatedOn(rs.getString("updateOn"));
			return user;
		}
	}
}