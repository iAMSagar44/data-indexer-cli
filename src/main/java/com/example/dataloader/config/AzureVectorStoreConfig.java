package com.example.dataloader.config;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.search.documents.indexes.SearchIndexClient;
import com.azure.search.documents.indexes.SearchIndexClientBuilder;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ AzureAISearchProperties.class })
public class AzureVectorStoreConfig {
    @Bean
    public SearchIndexClient searchIndexClient(AzureAISearchProperties properties) {
        return new SearchIndexClientBuilder().endpoint(properties.getUrl())
                .credential(new AzureKeyCredential(properties.getApiKey()))
                .buildClient();
    }

    @Bean
    public AzureAISearchVectorStore azVectorStore(SearchIndexClient searchIndexClient, EmbeddingClient embeddingClient) {
        return new AzureAISearchVectorStore(searchIndexClient, embeddingClient);
    }
}
