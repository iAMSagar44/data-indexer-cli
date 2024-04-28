package com.example.dataloader.loader;

import com.azure.core.http.rest.Response;
import com.azure.core.util.Context;
import com.azure.search.documents.indexes.SearchIndexClient;
import com.azure.search.documents.indexes.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SearchIndexManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchIndexManager.class);
    private final SearchIndexClient searchIndexClient;

    public SearchIndexManager(SearchIndexClient searchIndexClient) {
        this.searchIndexClient = searchIndexClient;
    }

    public boolean deleteIndex(String indexName) {
        final var statusCode = searchIndexClient.deleteIndexWithResponse(searchIndexClient.getIndex(indexName), true, Context.NONE).getStatusCode();
        LOGGER.debug("The status code is - {}", statusCode);
        return statusCode == 204;
    }

    public List<String> listIndexNames() {
        return searchIndexClient.listIndexNames().stream().toList();
    }

public boolean createIndex(String indexName) {
    final var searchFields = createSearchFields();
    final var index = new SearchIndex(indexName, searchFields);
    index.setVectorSearch(new VectorSearch()
            .setProfiles(Collections
                    .singletonList(new VectorSearchProfile("embedding_config", "default")))
            .setAlgorithms(Collections.singletonList(new HnswAlgorithmConfiguration("default")
                    .setParameters(new HnswParameters().setM(4)
                            .setEfConstruction(400)
                            .setEfSearch(1000)
                            .setMetric(VectorSearchAlgorithmMetric.COSINE)))));

    final Response<SearchIndex> response = searchIndexClient.createIndexWithResponse(index, Context.NONE);
    LOGGER.debug("The status code is - {}", response.getStatusCode());
    return response.getStatusCode() == 201;
}

    private List<SearchField> createSearchFields() {
        List<SearchField> fields = new ArrayList<>();

        fields.add(new SearchField("id", SearchFieldDataType.STRING).setKey(true)
                .setFilterable(true)
                .setSortable(true));
        fields.add(new SearchField("embedding", SearchFieldDataType.collection(SearchFieldDataType.SINGLE))
                .setSearchable(true)
                .setVectorSearchDimensions(1536)
                // This must match a vector search configuration name.
                .setVectorSearchProfileName("embedding_config"));
        fields.add(new SearchField("content", SearchFieldDataType.STRING).setSearchable(true)
                .setFilterable(true));
        fields.add(new SearchField("metadata", SearchFieldDataType.STRING).setSearchable(true)
                .setFilterable(true));

        return fields;
    }

}
