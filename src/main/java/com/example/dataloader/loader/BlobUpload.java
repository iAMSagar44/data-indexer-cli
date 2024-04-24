package com.example.dataloader.loader;

import com.azure.storage.blob.BlobContainerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.azure.storage.blob.BlobClient;
import java.nio.file.Path;

@Service
public class BlobUpload {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlobUpload.class);
    private final BlobContainerClient blobContainerClient;

    public BlobUpload(BlobContainerClient blobContainerClient) {
        this.blobContainerClient = blobContainerClient;
    }

    public void uploadFile(String localFilePath) {
        final var path = Path.of(localFilePath);
        LOGGER.info("Uploading file {} to Blob Storage container {}", path.getFileName().toString(), blobContainerClient.getBlobContainerName());
        BlobClient blobClient = blobContainerClient.getBlobClient(path.getFileName().toString());
        blobClient.uploadFromFile(localFilePath);
    }
}