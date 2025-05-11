package com.example.dataloader.loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.azure.ai.documentintelligence.DocumentIntelligenceClient;
import com.azure.ai.documentintelligence.models.AnalyzeDocumentOptions;
import com.azure.ai.documentintelligence.models.AnalyzeOperationDetails;
import com.azure.ai.documentintelligence.models.AnalyzeResult;
import com.azure.ai.documentintelligence.models.DocumentContentFormat;
import com.azure.core.util.polling.SyncPoller;

@Service
public class IndexDocuments {

    private final VectorStore vectorStore;
    private final DocumentIntelligenceClient documentIntelligenceClient;
    private static final Logger logger = LoggerFactory.getLogger(IndexDocuments.class);

    public IndexDocuments(VectorStore vectorStore,
            DocumentIntelligenceClient documentIntelligenceClient) {
        Assert.notNull(vectorStore, "VectorStore must not be null.");
        this.vectorStore = vectorStore;
        this.documentIntelligenceClient = documentIntelligenceClient;
    }

    void load(Path folderPath) throws IOException {
        logger.info("Using vector store: {} to index and store documents. \n",
                vectorStore.getClass().getSimpleName());
        logger.info("indexing documents");
        var documents = analyseDocument(folderPath);

        var tokenTextSplitter = new TokenTextSplitter();

        logger.info(
                "Parsing document, splitting, creating embeddings and storing in vector store.... this will take a while.");
        this.vectorStore.add(tokenTextSplitter.apply(documents));
        logger.info(
                "Done parsing document, splitting and creating embeddings. The document {} is stored in the Vector Store",
                folderPath.getFileName().toString());
    }

    List<Document> analyseDocument(Path filePath) throws IOException {
        SyncPoller<AnalyzeOperationDetails, AnalyzeResult> analyzeLayoutResultPoller =
                documentIntelligenceClient.beginAnalyzeDocument("prebuilt-layout",
                        new AnalyzeDocumentOptions(Files.readAllBytes(filePath))
                                .setOutputContentFormat(DocumentContentFormat.MARKDOWN));

        AnalyzeResult analyzeLayoutResult = analyzeLayoutResultPoller.getFinalResult();
        logger.info("Markdown output");
        logger.info("------------------------------------------------");
        logger.info(analyzeLayoutResult.getContent());

        return List.of(
                toDocument(analyzeLayoutResult.getContent(), filePath.getFileName().toString()));
    }

    private Document toDocument(String docText, String resourceFileName) {
        docText = Objects.requireNonNullElse(docText, "");
        Document doc = new Document(docText);
        doc.getMetadata().put("file_name", resourceFileName);
        return doc;
    }


}
