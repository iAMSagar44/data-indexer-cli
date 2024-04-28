package com.example.dataloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@SpringBootApplication(exclude = {org.springframework.ai.autoconfigure.vectorstore.azure.AzureVectorStoreAutoConfiguration.class})
@CommandScan
public class DataLoaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataLoaderApplication.class, args);
	}

}
