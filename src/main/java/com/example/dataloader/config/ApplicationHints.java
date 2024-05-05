package com.example.dataloader.config;

import com.azure.search.documents.implementation.DocumentsImpl;
import com.azure.search.documents.indexes.implementation.*;
import com.azure.search.documents.indexes.implementation.models.ListIndexesResult;
import com.azure.search.documents.indexes.implementation.models.SearchError;
import com.azure.search.documents.indexes.implementation.models.SearchErrorException;
import com.azure.search.documents.indexes.implementation.models.VectorSearchAlgorithmKind;
import com.azure.search.documents.indexes.models.*;
import com.azure.search.documents.models.IndexDocumentsResult;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApplicationHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.proxies().registerJdkProxy(IndexersImpl.IndexersService.class);
        hints.proxies().registerJdkProxy(DataSourcesImpl.DataSourcesService.class);
        hints.proxies().registerJdkProxy(SkillsetsImpl.SkillsetsService.class);
        hints.proxies().registerJdkProxy(SynonymMapsImpl.SynonymMapsService.class);
        hints.proxies().registerJdkProxy(IndexesImpl.IndexesService.class);
        hints.proxies().registerJdkProxy(SearchServiceClientImpl.SearchServiceClientService.class);
        hints.proxies().registerJdkProxy(DocumentsImpl.DocumentsService.class);

        var memberCategories = MemberCategory.values();

        for (var c : new Class[] { ListIndexesResult.class, SearchIndex.class, SearchFieldDataType.class, SearchErrorException.class,
                SearchError.class, IndexDocumentsResult.class, VectorSearchAlgorithmKind.class, VectorSearchAlgorithmMetric.class})
            hints.reflection().registerType(c, memberCategories);

    }
}
