/**
 * 
 */
package com.farooq.spring.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;

import com.farooq.spring.listener.CustomJobListener;

/**
 * @author faroooq
 *
 */
@Configuration
@EnableBatchProcessing
public class FeedGenJobConfiguration {
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	private JavaMailSender javaMailSender;
	
	
	@Bean
	public Step riskValueStep(){
		return stepBuilderFactory.get("riskvalue1").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				System.out.println("riskvalue in step1/tasklet");
				/*if(true){
					throw new Exception("Simple Test exception");
				}*/
				return RepeatStatus.FINISHED;
			}
		}).allowStartIfComplete(true).build();
	}
	
	
	@Bean
	public Step lastStep(){
		return stepBuilderFactory.get("lastStep").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				System.out.println("lastStep in step1/tasklet");
				
				return RepeatStatus.FINISHED;
			}
		}).allowStartIfComplete(true).build();
	}
	
	
	/**
	 * Step called first and then the flow
	 * @param flow
	 * @return
	 */
	/*@Bean
	public Job feedGenJob(Flow flow){
		return jobBuilderFactory.get("feedGenJob1")
				.start(riskValueStep())
				.on("COMPLETED").to(flow)
				.end()
				.build();
	}*/
	
	
	public static class CustomDecider implements JobExecutionDecider {

		@Override
		public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
			return new FlowExecutionStatus("COMPLETED");
		}
	}
	
	
	@Bean
	public JobExecutionDecider decider() {
		return new CustomDecider();
	}	
	
	
	/**
	 * Step called first and then the flow
	 * working fine with the decider
	 * @param flow
	 * @return
	 */
	@Bean
	public Job feedGenJobSplit(@Qualifier("commonFlow") Flow flow){
		return jobBuilderFactory.get("feedGenJobSplit4").listener(new CustomJobListener(javaMailSender))
				.start(riskValueStep())
				.next(decider())
				.from(decider()).on("COMPLETED").to(flow)
				.from(decider()).on("FAILED").to(lastStep())
				.from(flow).next(lastStep())
				.end()
				.build();
	}

}
