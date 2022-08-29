package com.mytvbatch.app.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
public class UserController {
	
	private static final String fiveMinCronJob = "0 */1 * ? * *";
	
	@Autowired
    private JobLauncher jobLauncher;
	
    @Autowired
    private Job job;

    @Scheduled(cron = fiveMinCronJob)
    @PostMapping("/importUsers")
    public ResponseEntity<String> importCsvToDBJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("JobID", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
            return new ResponseEntity<String>("Job Executed!!", HttpStatus.OK);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
        	return new ResponseEntity<String>("Job Failed!!", HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e) {
        	return new ResponseEntity<String>("Job Failed!!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
