package com.vlabs.app;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
	public static void main(String... s) {
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("job");
		JobExplorer explorer = (JobExplorer) context.getBean("jobExplorer");
		JobOperator operator = (JobOperator) context.getBean("jobOperator");
		JobRegistry registry = (JobRegistry) context.getBean("jobRegistry");
		try {
			JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
					.addString("fail", "true").toJobParameters();
			JobExecution execution = jobLauncher.run(job, jobParameters);
			System.out.println("Exit Status : " + execution.getStatus());

			List<JobInstance> instances = explorer.getJobInstances("job", 0, 10);
			System.out.println("Exploler Size : " + instances.size());
			for (JobInstance instance : instances) {
				System.out.println("instance Id : " + instance.getId());
				List<JobExecution> executions = explorer.getJobExecutions(instance);
				System.out.println("Executions size : " + executions.size());
				if (executions.size() > 0) {
					JobExecution jobExecution = executions.get(executions.size() - 1);
					if (jobExecution.getStatus().name().equals("FAILED")) {
						long a = operator.restart(instance.getId());
						System.out.println("Exit Status : " + a);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
