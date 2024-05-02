package com.example.dataloader.loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.tika.TikaDocumentReader;
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

    void load(Path folderPath, boolean isParagraphReader){
        LOGGER.debug("Using vector store: {} to index and store documents. \n", vectorStore.getClass().getSimpleName());
        String extension = getFileExtension(folderPath);
        DocumentReader documentReader = switch (extension) {
            case "pdf" -> pdfReader(folderPath, isParagraphReader);
            case "docx", "doc" -> docReader(folderPath);
            default -> throw new IllegalArgumentException("Unsupported file extension: " + extension);
        };
        LOGGER.debug("Using document reader : {} to read documents", documentReader.getClass().getSimpleName());

        var tokenTextSplitter = new TokenTextSplitter();

        LOGGER.info("Parsing document, splitting, creating embeddings and storing in vector store....  this will take a while.");
        this.vectorStore.accept(
                tokenTextSplitter.apply(
                        documentReader.get()));
        LOGGER.info("Done parsing document, splitting and creating embeddings. The document {} is stored in the Vector Store", folderPath.getFileName().toString());
    }

    private DocumentReader docReader(Path folderPath) {
        return new TikaDocumentReader(folderPath.toUri().toString());
    }

    private DocumentReader pdfReader(Path folderPath, boolean isParagraphReader) {
        LOGGER.info("reading documents by {}", isParagraphReader ? "paragraph" : "page");
        if(isParagraphReader){
            return new ParagraphPdfDocumentReader(
                    folderPath.toUri().toString(),
                    PdfDocumentReaderConfig.builder()
                            .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                    .withNumberOfBottomTextLinesToDelete(0)
                                    .withNumberOfTopPagesToSkipBeforeDelete(0)
                                    .build())
                            .withPagesPerDocument(1)
                            .build());
        }
        return new PagePdfDocumentReader(
                folderPath.toUri().toString(),
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfBottomTextLinesToDelete(0)
                                .withNumberOfTopPagesToSkipBeforeDelete(0)
                                .build())
                        .withPagesPerDocument(1)
                        .build());
    }

    private String getFileExtension(Path folderPath) {
        try {
            String mimeType = Files.probeContentType(folderPath);
            LOGGER.debug("Mime type of the file is: {}", mimeType);
            if (mimeType == null) {
                return "";
            }
            return switch (mimeType) {
                case "application/pdf" -> "pdf";
                case "text/plain" -> "txt";
                case "application/msword" -> "doc";
                case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> "docx";
                // Add more cases as needed
                default -> "";
            };
        } catch (IOException e) {
            LOGGER.error("Error while getting file extension: {}", e.getMessage());
            return "";
        }

    }

}
