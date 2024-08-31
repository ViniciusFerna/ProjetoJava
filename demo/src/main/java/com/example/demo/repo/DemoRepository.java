package com.example.demo.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Demo;


	public interface DemoRepository extends JpaRepository<Demo, Long> {
		
	
	
}
