package com.example.dataloader.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.azure.ai.documentintelligence.DocumentIntelligenceClient;
import com.azure.ai.documentintelligence.DocumentIntelligenceClientBuilder;
import com.azure.core.credential.AzureKeyCredential;

@Configuration
public class AzDocumentIntelligenceConfig {

    @Bean
    public DocumentIntelligenceClient documentIntelligenceClient() {
        return new DocumentIntelligenceClientBuilder()
                .credential(
                        new AzureKeyCredential(System.getenv("AZURE_DOCUMENT_INTELLIGENCE_KEY")))
                .endpoint(System.getenv("AZURE_DOCUMENT_INTELLIGENCE_ENDPOINT")).buildClient();
    }

}
