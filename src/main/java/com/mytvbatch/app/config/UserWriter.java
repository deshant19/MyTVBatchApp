package com.mytvbatch.app.config;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;

import com.mytvbatch.app.model.User;

public class UserWriter extends FlatFileItemWriter<User> {

	public UserWriter() {
		super.setHeaderCallback(new FlatFileHeaderCallback() {

        public void writeHeader(Writer writer) throws IOException {
            	writer.write("id,fname,lname,email,service,addedOn,updatedOn");
        	}
		});
	}
}