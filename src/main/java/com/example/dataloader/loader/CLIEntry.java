package com.example.dataloader.loader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command(command = "data")
public class CLIEntry {
    private static final Logger LOGGER = LoggerFactory.getLogger(CLIEntry.class);
    private final IndexDocuments indexDocuments;
    private final JdbcClient jdbcClient;

    public CLIEntry(IndexDocuments indexDocuments, JdbcClient jdbcClient) {
        this.indexDocuments = indexDocuments;
        this.jdbcClient = jdbcClient;
    }

    @Command(command = "load", description = "Index documents to the Vector Store")
    public void loadData(
            @Option(longNames = "path", shortNames = 'p', required = true, label = "the path of the directory or file") String path)
            throws IOException {
        Path folderPath = Path.of(path);
        LOGGER.info("Loading documents from the path {}", folderPath.toAbsolutePath());
        validateFolderPath(folderPath);
    }

    @Command(command = "delete", description = "Deletes all the documents from the Vector Store")
    public void deleteAllData()
            throws IOException {
        LOGGER.info("Deleting all documents from the Vector Store");
        jdbcClient.sql("DELETE FROM vector_store").update();
    }

    private void validateFolderPath(Path folderPath) throws IOException {
        if (Files.isDirectory(folderPath)) {
            LOGGER.info("The path provided is a directory");
            processDirectory(folderPath);
        } else {
            if (Files.exists(folderPath)) {
                LOGGER.info("The File path is {}", folderPath.toUri().toString());
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
}
