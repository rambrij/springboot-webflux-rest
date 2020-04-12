package com.javacoderhint;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.javacoderhint.model.Employee;
import com.javacoderhint.repository.EmployeeRepository;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringbootWebfluxRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootWebfluxRestApplication.class, args);
	}

	@Bean
	CommandLineRunner init(EmployeeRepository repository) {
		return args -> {
			Flux<Employee> empFlux = Flux
					.just(new Employee(null, "Ram", 32), new Employee(null, "Ram", 32), new Employee(null, "Ram", 32))
					.flatMap(repository::save);

			empFlux.thenMany(repository.findAll()).subscribe(System.out::println);
		};

	}
}
