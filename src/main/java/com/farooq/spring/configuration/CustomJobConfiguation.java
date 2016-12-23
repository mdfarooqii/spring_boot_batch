/**
 * 
 */
package com.farooq.spring.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author faroooq
 *
 */

@Configuration
@EnableBatchProcessing
public class CustomJobConfiguation {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	
	
	@Bean
	public Step step1(){
		return stepBuilderFactory.get("step1").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				// TODO Auto-generated method stub
				
				System.out.println("Hello world in step1/tasklet");
				
				return RepeatStatus.FINISHED;
			}
		}).build();
		
	}
	
	@Bean
	public Job helloWorldJob(){
		return jobBuilderFactory.get("myJob").start(step1()).build();
	}
	
}
