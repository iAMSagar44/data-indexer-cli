package com.example.dataloader.loader;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class IndexDocuments {

    private final VectorStore vectorStore;
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexDocuments.class);

    public IndexDocuments(VectorStore vectorStore) {
        Assert.notNull(vectorStore, "VectorStore must not be null.");
        this.vectorStore = vectorStore;
    }

    void load(Path folderPath){
        LOGGER.info("indexing documents");
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

        LOGGER.info("Parsing document, splitting, creating embeddings and storing in vector store....  this will take a while.");
        this.vectorStore.accept(
                tokenTextSplitter.apply(
                        pdfReader.get()));
        LOGGER.info("Done parsing document, splitting and creating embeddings. The document {} is stored in the Vector Store", folderPath.getFileName().toString());
    }
    
}
