package me.michalwozniak.noteswebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@SpringBootApplication
public class NotesWebserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesWebserviceApplication.class, args);
	}
}
