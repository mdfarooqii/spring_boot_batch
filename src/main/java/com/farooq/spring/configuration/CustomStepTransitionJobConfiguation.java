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
public class CustomStepTransitionJobConfiguation {

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
				
				System.out.println("Transition step job in step1/tasklet");
				
				return RepeatStatus.FINISHED;
			}
		}).build();
		
	}
	
	
	@Bean
	public Step step2(){
		return stepBuilderFactory.get("step2").tasklet((stepContribution,chunkContext) -> {
				
			System.out.println("Transition step job in step2/tasklet");
			
			return RepeatStatus.FINISHED;
			}).build();
	}
	
	
	@Bean
	public Step step3(){
		return stepBuilderFactory.get("step3").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				// TODO Auto-generated method stub
				
				System.out.println("Transition step job in step3/tasklet");
				
				return RepeatStatus.FINISHED;
			}
		}).build();
		
	}
	
	/**
	 * Normal way of organizing the steps
	 */
	/*@Bean
	public Job transitionStepJob(){
		return jobBuilderFactory.get("transitionStepJob").start(step1()).next(step2()).next(step3()).build();
	}*/
	
	
	/**
	 * Another way of organizing the flows/step in the job.
	 * @return
	 */
	@Bean
	public Job transitionStepJob1(){
		return jobBuilderFactory.get("transitionStepJob").
				start(step1())
				.on("COMPLETED").to(step2())
				.from(step2()).on("COMPLETED").to(step3()).end().build();
	}
	
}
