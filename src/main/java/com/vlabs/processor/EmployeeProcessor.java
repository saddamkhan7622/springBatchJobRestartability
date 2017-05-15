package com.vlabs.processor;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;

import com.vlabs.bean.Employee;

public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {
	int count = 0;
	int exceptionCount = 0;
	private ExecutionContext executionContext;

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		this.executionContext = stepExecution.getExecutionContext();
		// this.executionContext.putInt( "i_ThoseRows",
		// this.executionContext.getInt( "i_ThoseRows", 0 ) + 1 );
	}

	@Override
	public Employee process(Employee employee) throws Exception {

		System.out.println("Processing : " + employee);
		//System.out.println(exceptionCount);
		if (employee.getEmpName().equals("prabhakar") && exceptionCount == 0) {
			exceptionCount++;
			System.out.println("EXCEPTION COUNT:- " + exceptionCount);
			throw new Exception("generating exception to check");
		}
		count++;
		executionContext.putLong("READ_COUNT", count);
		System.out.println("RECORD COUNT :- " + count);
		return employee;
	}

}
