/**
 * 
 */
package com.farooq.spring.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author faroooq
 *
 */
public class CustomJobListener implements JobExecutionListener{
	
	private static final String JOB_SUBJECT = "Important Feed Generator Job Nofications";
	

	private JavaMailSender javaMailSender;
	
	
	public CustomJobListener(JavaMailSender javaMailSender){
		super();
		this.javaMailSender = javaMailSender;
		
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		String jobName = jobExecution.getJobInstance().getJobName();
		SimpleMailMessage simpleMailMessage = getSimpleMailMessage(JOB_SUBJECT, String.format("This is to notify you that job with name [%s] has been started now", jobName)); 
		//javaMailSender.send(simpleMailMessage);
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		String jobName = jobExecution.getJobInstance().getJobName();
		
		if(jobExecution.getExitStatus().equals(ExitStatus.FAILED)){
			SimpleMailMessage simpleMailMessage = getSimpleMailMessage(JOB_SUBJECT , String.format("This is to notify job failure with job name [%s].Kindly contact the support/development team", jobName)); 
			//javaMailSender.send(simpleMailMessage);
		}
		
	}
	
	
	private SimpleMailMessage getSimpleMailMessage(String subject, String text) {
		SimpleMailMessage mail = new SimpleMailMessage();

		mail.setTo("****@gmail.com");
		mail.setSubject(subject);
		mail.setText(text);
		return mail;
	}
	

}
