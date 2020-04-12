package com.javacoderhint.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.javacoderhint.model.Employee;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String>{

}
