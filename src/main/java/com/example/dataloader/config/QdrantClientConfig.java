package com.example.dataloader.config;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import org.springframework.ai.autoconfigure.vectorstore.qdrant.QdrantVectorStoreProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(QdrantVectorStoreProperties.class)
public class QdrantClientConfig {

    @Bean
    public QdrantClient qdrantClient(QdrantVectorStoreProperties qdrantVectorStoreProperties) {
        QdrantGrpcClient.Builder grpcClientBuilder = QdrantGrpcClient.newBuilder(qdrantVectorStoreProperties.getHost(), qdrantVectorStoreProperties.getPort(), qdrantVectorStoreProperties.isUseTls());
        grpcClientBuilder.withApiKey(qdrantVectorStoreProperties.getApiKey());
        return new QdrantClient(grpcClientBuilder.build());
    }
}
