package com.mytvbatch.app.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.mytvbatch.app.model.User;
import com.mytvbatch.app.repository.UserRepository;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	
	private static final int chunkSize = 10;
	private static final String outputFilePath = "output/outputData.csv";
	private static final String inputFilePath = "src/main/resources/mytvUsers.csv";

	@Autowired
    private JobBuilderFactory jobBuilderFactory;

	@Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private UserRepository userRepository;
    
    private Resource outputResource = new FileSystemResource(outputFilePath);


    @Bean
    public FlatFileItemReader<User> readFromCSV() {
        FlatFileItemReader<User> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource(inputFilePath));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<User> lineMapper() {
        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "fname", "lname", "email", "service", "addedOn", "updatedOn");

        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }
    
    

    @Bean
    public UserProcessor processor() {
        return new UserProcessor();
    }

    @Bean
    public RepositoryItemWriter<User> writeToDB() {
        RepositoryItemWriter<User> writer = new RepositoryItemWriter<>();
        writer.setRepository(userRepository);
        writer.setMethodName("save");
        return writer;
    }
    
    @Bean
    public FlatFileItemWriter<User> writeDeltaToCSV() {
      UserWriter writer = new UserWriter();
      
      writer.setResource(outputResource);
      
      writer.setAppendAllowed(false);
       
      writer.setLineAggregator(new DelimitedLineAggregator<User>() {
        {
          setDelimiter(",");
          setFieldExtractor(new BeanWrapperFieldExtractor<User>() {
            {
              setNames(new String[] { "id", "fname", "lname", "email", "service", "addedOn", "updatedOn" });
            }
          });
        }
      });
      return writer;
    }

    @Bean
    public Step stepForDB() {
        return stepBuilderFactory.get("write-db").<User, User>chunk(chunkSize)
                .reader(readFromCSV())
                .processor(processor())
                .writer(writeToDB())
                .taskExecutor(taskExecutor())
                .build();
    }
    
    @Bean
    public Step stepForCSV() {
        return stepBuilderFactory.get("write-csv").<User, User>chunk(chunkSize)
                .reader(readFromCSV())
                .processor(processor())
                .writer(writeDeltaToCSV())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job runJob() {
        return jobBuilderFactory.get("importUsers")
                .flow(stepForCSV()).next(stepForDB()).end().build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(chunkSize);
        return asyncTaskExecutor;
    }

}