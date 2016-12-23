/**
 * 
 */
package com.farooq.spring.configuration;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

/**
 * @author faroooq
 *
 */
@Configuration
@EnableBatchProcessing
public class BaseFlowConfiguration {
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
		
	@Bean
	@Scope("step")
	public Step positionStep(){
		return stepBuilderFactory.get("positionStep").tasklet(
				(stepContribution, chunkContext) -> {
					System.out.println("Position file created in tasklet Thread Name is "+Thread.currentThread().getName());
					
					return RepeatStatus.FINISHED;
					
				}).allowStartIfComplete(true).build();
	}
	
	
	@Bean
	@Scope("step")
	public Step mdsStep(){
		return stepBuilderFactory.get("mdsStep").tasklet(
				(stepContribution, chunkContext) -> {
					System.out.println("mds  file created in tasklet Thread Name is "+Thread.currentThread().getName());
					
					return RepeatStatus.FINISHED;
					
				}).allowStartIfComplete(true).build();
	}
	
	
	@Bean
	@Scope("step")
	public Step posIndStep(){
		return stepBuilderFactory.get("posIndStep").tasklet(
				(stepContribution, chunkContext) -> {
					System.out.println("Position Indicative file created in tasklet is Thread Name is "+Thread.currentThread().getName());
					
					return RepeatStatus.FINISHED;
					
				}).allowStartIfComplete(true).build();
	}
	
	
	@Bean
	@Scope("step")
	public Flow positionFlow(){
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("positionFlow");
		flowBuilder.start(positionStep()).end();
		return flowBuilder.build();
	}
	
	
	@Bean
	@Scope("step")
	public Flow mdsFlow(){
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("mdsFlow");
		flowBuilder.start(mdsStep()).end();
		return flowBuilder.build();
	}
	
	@Bean
	@Scope("step")
	public Flow posIndFlow(){
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("posIndFlow");
		flowBuilder.start(posIndStep()).end();
		return flowBuilder.build();
	}
	
	
	
	
	
	@Bean
	@Qualifier("commonFlow")
	@Scope("step")
	public Flow commonFlow(){
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("commonFlow1");
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
		asyncTaskExecutor.setConcurrencyLimit(5);
		
		//we have to make sure to end the flow here
		flowBuilder.start(positionStep()).split(asyncTaskExecutor).add(mdsFlow(),posIndFlow()).end(); 
		return flowBuilder.build();
	}

}
