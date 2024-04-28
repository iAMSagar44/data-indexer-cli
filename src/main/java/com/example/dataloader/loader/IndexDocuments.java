package com.example.dataloader.loader;

import java.nio.file.Path;

import com.example.dataloader.config.AzureAISearchVectorStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class IndexDocuments {

    private final AzureAISearchVectorStore azVectorStore;
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexDocuments.class);

    public IndexDocuments(AzureAISearchVectorStore azVectorStore) {
        Assert.notNull(azVectorStore, "VectorStore must not be null.");
        this.azVectorStore = azVectorStore;
    }

    void load(Path folderPath, String indexName){
        LOGGER.debug("Using vector store: {} to index and store documents. \n", azVectorStore.getClass().getSimpleName());
        LOGGER.info("reading documents");
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(
                folderPath.toUri().toString(),
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfBottomTextLinesToDelete(3)
                                .withNumberOfTopPagesToSkipBeforeDelete(1)
                                .build())
                        .withPagesPerDocument(1)
                        .build());

        var tokenTextSplitter = new TokenTextSplitter();

        LOGGER.info("Parsing document, splitting, creating embeddings and storing in index name {}....  this will take a while.", indexName);
        azVectorStore.setIndexName(indexName);
        this.azVectorStore.add(
                tokenTextSplitter.apply(
                        pdfReader.get()));
        LOGGER.info("Done parsing document, splitting and creating embeddings. The document {} is stored in the Vector Store", folderPath.getFileName().toString());
    }
    
}
