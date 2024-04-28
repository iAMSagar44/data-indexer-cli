package com.example.dataloader.config;

import com.alibaba.fastjson2.JSONObject;
import com.azure.search.documents.SearchClient;
import com.azure.search.documents.SearchDocument;
import com.azure.search.documents.indexes.SearchIndexClient;
import com.azure.search.documents.models.IndexDocumentsResult;
import com.azure.search.documents.models.IndexingResult;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class AzureAISearchVectorStore {
    private final SearchIndexClient searchIndexClient;
    private final EmbeddingClient embeddingClient;
    private SearchClient searchClient;
    private String indexName;

    private static final String ID_FIELD_NAME = "id";
    private static final String CONTENT_FIELD_NAME = "content";
    private static final String EMBEDDING_FIELD_NAME = "embedding";
    private static final String METADATA_FIELD_NAME = "metadata";

    public AzureAISearchVectorStore(SearchIndexClient searchIndexClient, EmbeddingClient embeddingClient) {
        this.searchIndexClient = searchIndexClient;
        this.embeddingClient = embeddingClient;
    }

    public void add(List<Document> documents) {
        Assert.notNull(documents, "The document list should not be null.");
        if (CollectionUtils.isEmpty(documents)) {
            return; // nothing to do;
        }

        final var searchDocuments = documents.stream().map(document -> {
            final var embeddings = this.embeddingClient.embed(document);
            SearchDocument searchDocument = new SearchDocument();
            searchDocument.put(ID_FIELD_NAME, document.getId());
            searchDocument.put(EMBEDDING_FIELD_NAME, embeddings);
            searchDocument.put(CONTENT_FIELD_NAME, document.getContent());
            searchDocument.put(METADATA_FIELD_NAME, new JSONObject(document.getMetadata()).toJSONString());
            return searchDocument;
        }).toList();

        IndexDocumentsResult result = getSearchClient().uploadDocuments(searchDocuments);

        for (IndexingResult indexingResult : result.getResults()) {
            Assert.isTrue(indexingResult.isSucceeded(),
                    String.format("Document with key %s upload is not successfully", indexingResult.getKey()));
        }

    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    private SearchClient getSearchClient() {
        return this.searchIndexClient.getSearchClient(this.indexName);
    }
}
