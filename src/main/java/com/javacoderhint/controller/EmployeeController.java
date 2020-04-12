package com.javacoderhint.controller;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.javacoderhint.model.Employee;
import com.javacoderhint.model.EmployeeEvent;
import com.javacoderhint.repository.EmployeeRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employeee")
public class EmployeeController {

	EmployeeRepository repository;

	public EmployeeController(EmployeeRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	Flux<Employee> getEmpList() {
		return repository.findAll();
	}

	@GetMapping("{id}")
	Mono<ResponseEntity<Employee>> getEmp(@PathVariable String id) {
		return repository.findById(id).map(emp -> ResponseEntity.ok(emp))
				.defaultIfEmpty((ResponseEntity.notFound().build()));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	Mono<Employee> saveEmp(@RequestBody Employee employee) {
		return repository.save(employee);
	}

	@PutMapping("{id}")
	Mono<ResponseEntity<Employee>> updateEmp(@PathVariable(value = "id") String id, @RequestBody Employee employee) {
		return repository.findById(id).
				flatMap(existingEmp -> {
					existingEmp.setName(employee.getName());
					existingEmp.setAge(employee.getAge());
					return repository.save(existingEmp);
				})
				.map(existingEmp -> ResponseEntity.ok(existingEmp))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("{id}")
	Mono<ResponseEntity<Void>> deleteEmp(@PathVariable(value = "id") String id) {
		return repository.findById(id)
				.flatMap(emp -> repository.delete(emp).then(Mono.just(ResponseEntity.ok().<Void>build())))
				.defaultIfEmpty(ResponseEntity.notFound().build());

	}

	@GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	Flux<EmployeeEvent> getEmpEvents() {
		return Flux.interval(Duration.ofSeconds(1)).map(val -> new EmployeeEvent(val, "Emp Events"));

	}

}