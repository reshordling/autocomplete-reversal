package org.fibo.seo.autocomplete;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class AutocompleteApp {

	public static void main(String[] args) {
		SpringApplication.run(AutocompleteApp.class, args);
	}

}
