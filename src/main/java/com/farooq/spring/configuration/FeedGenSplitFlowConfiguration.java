/**
 * 
 */
package com.farooq.spring.configuration;

import java.nio.file.attribute.AclEntry.Builder;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * @author faroooq
 *
 */
public class FeedGenSplitFlowConfiguration {
	
	
	/*@Autowired
	private StepBuilderFactory stepBuilderFactory; 
	
	
	@Bean
	public Step riskValueStep(){
		
		return stepBuilderFactory.get("riskValueStep").tasklet((stepContribution, chunkContext) ->{ 
			System.out.println("!!!       RiskvalueStepCalled       !!!");
			return RepeatStatus.FINISHED;
		}).build();
	}*/
	
	
	/*@Bean
	public Flow riskValueFlow(){
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("riskValueFlow");
		flowBuilder.start(riskValueStep())
		.on("FAILED").end()
		.from(riskValueStep()).split(new SimpleAsyncTaskExecutor()).add(flow)
		
		
		return null;
	}*/

}
