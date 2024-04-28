package com.example.dataloader.loader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command(command = "azais", description = "CLI for interacting with an Azure AI Search instance", group = "Application Commands")
public class CLIEntry {
    private static final Logger LOGGER = LoggerFactory.getLogger(CLIEntry.class);
    private final IndexDocuments indexDocuments;
    private final SearchIndexManager searchIndexManager;

    public CLIEntry(IndexDocuments indexDocuments, SearchIndexManager searchIndexManager) {
        this.indexDocuments = indexDocuments;
        this.searchIndexManager = searchIndexManager;
    }

    @Command(command = "load", description = "Index documents to the Vector Store")
    public void loadData(
            @Option(longNames = "path", shortNames = 'p', required = true, label = "the path of the directory or file") String path,
            @Option(longNames = "index", shortNames = 'i', required = true, label = "the index name to load the documents") String name)
            throws IOException {
        Path folderPath = Path.of(path);
        LOGGER.info("Loading documents from the path {}", folderPath.toAbsolutePath());
        validateFolderPath(folderPath, name);
    }

    @Command(command = "list", description = "List existing index names in the Azure AI Search instance")
    public String listIndexes(){
        try {
            final var collections = this.searchIndexManager.listIndexNames();
            if (collections.isEmpty()) {
                return "No indexes found in the Vector Store";
            }
            return String.format("Indexes in the Vector Store: %s", String.join(", ", collections));
        } catch (Exception e) {
            LOGGER.error("Error occurred while listing indexes. Error - {}", e.getLocalizedMessage());
            return "There was an error while listing the index names";
        }
    }

    @Command(command = "create", description = "Create an index in the Azure AI Search instance")
    public String createIndex(
            @Option(longNames = "index", shortNames = 'i', required = true, label = "the index name to create") String name) {
        return searchIndexManager.createIndex(name) ? String.format("Index %s successfully created", name) : String.format("Error occurred while creating index %s", name);
    }

    @Command(command = "delete", description = "Delete an index from the Azure AI Search instance")
    public String deleteIndex(
            @Option(longNames = "index", shortNames = 'i', required = true, label = "the index name to delete") String name) {
        return searchIndexManager.deleteIndex(name) ? String.format("Index %s deleted successfully", name) : String.format("Error occurred while deleting index %s", name);
    }

    private void validateFolderPath(Path folderPath, String indexName) throws IOException {
        if (Files.isDirectory(folderPath)) {
            LOGGER.info("The path provided is a directory");
            processDirectory(folderPath, indexName);
        } else {
            if (Files.exists(folderPath)) {
                loadDocuments(folderPath, indexName);
            } else {
                throw new FileNotFoundException(
                        String.format("The file path provided does not exist. File Path %s", folderPath.toString()));
            }
        }
    }

    private void processDirectory(Path folderPath, String indexName) throws IOException {
        try(Stream<Path> pathStream = Files.list(folderPath)){
            final var files = pathStream.toList();
            LOGGER.info("Found {} files in the directory. Indexing each file separately. \n", files.size());
            files.forEach(file -> loadDocuments(file, indexName));
        }
        
    }

    private void loadDocuments(Path folderPath, String indexName) {
        LOGGER.info("Processing file {}", folderPath.getFileName().toString());
            indexDocuments.load(folderPath, indexName);
    }
}
