package com.example.dataloader;

import com.example.dataloader.config.ApplicationHints;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.shell.command.annotation.CommandScan;

@ImportRuntimeHints(ApplicationHints.class)
@SpringBootApplication(exclude = {org.springframework.ai.autoconfigure.vectorstore.azure.AzureVectorStoreAutoConfiguration.class})
@CommandScan
public class DataLoaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataLoaderApplication.class, args);
	}

}
