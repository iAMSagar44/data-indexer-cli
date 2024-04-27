package com.example.dataloader.loader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import io.qdrant.client.QdrantClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command(command = "qdrant", description = "CLI for interacting with a Qdrant Vector Store", group = "Application Commands")
public class CLIEntry {
    private static final Logger LOGGER = LoggerFactory.getLogger(CLIEntry.class);
    private final IndexDocuments indexDocuments;
    private final QdrantClient qdrantClient;

    public CLIEntry(IndexDocuments indexDocuments, QdrantClient qdrantClient) {
        this.indexDocuments = indexDocuments;
        this.qdrantClient = qdrantClient;
    }

    @Command(command = "load", description = "Index documents to the Vector Store")
    public void loadData(
            @Option(longNames = "path", shortNames = 'p', required = true, label = "the path of the directory or file") String path)
            throws IOException {
        Path folderPath = Path.of(path);
        LOGGER.info("Loading documents from the path {}", folderPath.toAbsolutePath());
        validateFolderPath(folderPath);
    }

    @Command(command = "list", description = "List existing collections in Qdrant Vector Store")
    public String listCollections(){
        try {
            return String.join(", ", this.qdrantClient.listCollectionsAsync().get());
        } catch (Exception e) {
            LOGGER.error("Error occurred while listing collections {}", e.getLocalizedMessage());
            return "Error occurred while listing collections";
        }
    }

    @Command(command = "delete", description = "Delete a collection from Qdrant Vector Store")
    public String deleteCollection(
            @Option(longNames = "collection", shortNames = 'c', required = true, label = "the collection to delete") String name) {
        try {
            if (!isCollectionExists(name)) {
                return String.format("The collection %s does not exist", name);
            }
            final var result = this.qdrantClient.deleteCollectionAsync(name).get().getResult();
            if (result) {
                return String.format("Successfully deleted the collection %s", name);
            } else {
                LOGGER.error("Error occurred while deleting the collection {}.", name);
                return String.format("There was an error while deleting the collection %s", name);
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while deleting the collection {}. Error - {}", name, e.getLocalizedMessage());
            return String.format("There was an error while deleting the collection %s", name);
        }
    }

    private void validateFolderPath(Path folderPath) throws IOException {
        if (Files.isDirectory(folderPath)) {
            LOGGER.info("The path provided is a directory");
            processDirectory(folderPath);
        } else {
            if (Files.exists(folderPath)) {
                loadDocuments(folderPath);
            } else {
                throw new FileNotFoundException(
                        String.format("The file path provided does not exist. File Path %s", folderPath.toString()));
            }
        }
    }

    private void processDirectory(Path folderPath) throws IOException {
        try(Stream<Path> pathStream = Files.list(folderPath)){
            final var files = pathStream.toList();
            LOGGER.info("Found {} files in the directory. Indexing each file separately. \n", files.size());
            files.forEach(this::loadDocuments);
        }
        
    }

    private void loadDocuments(Path folderPath) {
        LOGGER.info("Processing file {}", folderPath.getFileName().toString());
            indexDocuments.load(folderPath);
    }

    private boolean isCollectionExists(String collectionName) {
        try {
            return ((List)this.qdrantClient.listCollectionsAsync().get()).stream().anyMatch((c) -> c.equals(collectionName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
