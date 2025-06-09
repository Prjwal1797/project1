package com.abcBank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
		info= @Info(
				title = "AbcBank Application",
				description = "Backend Rest APIs for Abc bank",
				version = "V1.0",
				contact = @Contact(
						name = "Prajwal",
						email = "abc@abc.com",
						url = "https://github.com/Prjwal1797/project1"
						
						),
				license = @License(name = "abc bank",
						url = "https://github.com/Prjwal1797/project1"
						)
				
				),
		externalDocs = @ExternalDocumentation(
				description = " The abc bank documentation",
				url = "https://github.com/Prjwal1797/project1"
				)
		)
public class AbcBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbcBankApplication.class, args);
	}

}

//uhkv avxt onwu hguy
// added to test_Branch